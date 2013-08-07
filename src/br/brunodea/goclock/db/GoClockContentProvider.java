package br.brunodea.goclock.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import br.brunodea.goclock.db.DBStructure.AbsolutePresetTable;
import br.brunodea.goclock.db.DBStructure.BasePresetTable;
import br.brunodea.goclock.db.DBStructure.BaseTable;
import br.brunodea.goclock.db.DBStructure.ByoYomiPresetTable;
import br.brunodea.goclock.db.DBStructure.CanadianPresetTable;
import br.brunodea.goclock.db.DBStructure.TimeRulesTable;
import br.brunodea.goclock.timerule.AbsoluteTimeRule;

public class GoClockContentProvider extends ContentProvider {
	public static final String PROVIDER_NAME = "br.brunodea.provider.goclock";

	public static final Uri CONTENT_URI_TIME_RULES =
			Uri.parse("content://"+PROVIDER_NAME+"/"+TimeRulesTable.TABLE_NAME);
	public static final Uri CONTENT_URI_BYOYOMI_PRESETS =
			Uri.parse("content://"+PROVIDER_NAME+"/"+ByoYomiPresetTable.TABLE_NAME);
	public static final Uri CONTENT_URI_CANADIAN_PRESETS =
			Uri.parse("content://"+PROVIDER_NAME+"/"+CanadianPresetTable.TABLE_NAME);
	public static final Uri CONTENT_URI_ABSOLUTE_PRESETS =
			Uri.parse("content://"+PROVIDER_NAME+"/"+AbsolutePresetTable.TABLE_NAME);
	public static final Uri CONTENT_URI_ALL =
			Uri.parse("content://"+PROVIDER_NAME+"/all");
	
	private static final int TIME_RULES = 1;
	private static final int TIME_RULES_ID = 2;
	private static final int BYOYOMI_PRESETS = 3;
	private static final int BYOYOMI_PRESETS_ID = 4;
	private static final int CANADIAN_PRESETS = 5;
	private static final int CANADIAN_PRESETS_ID = 6;
	private static final int ABSOLUTE_PRESETS = 7;
	private static final int ABSOLUTE_PRESETS_ID = 8;
	private static final int ALL_RULES = 9;
	
	private static final UriMatcher URI_MATCHER;
	
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(PROVIDER_NAME, TimeRulesTable.TABLE_NAME, TIME_RULES);
		URI_MATCHER.addURI(PROVIDER_NAME, TimeRulesTable.TABLE_NAME+"/#", TIME_RULES_ID);
		URI_MATCHER.addURI(PROVIDER_NAME, ByoYomiPresetTable.TABLE_NAME, BYOYOMI_PRESETS);
		URI_MATCHER.addURI(PROVIDER_NAME, ByoYomiPresetTable.TABLE_NAME+"/#", BYOYOMI_PRESETS_ID);
		URI_MATCHER.addURI(PROVIDER_NAME, CanadianPresetTable.TABLE_NAME, CANADIAN_PRESETS);
		URI_MATCHER.addURI(PROVIDER_NAME, CanadianPresetTable.TABLE_NAME+"/#", CANADIAN_PRESETS_ID);
		URI_MATCHER.addURI(PROVIDER_NAME, AbsolutePresetTable.TABLE_NAME, ABSOLUTE_PRESETS);
		URI_MATCHER.addURI(PROVIDER_NAME, AbsolutePresetTable.TABLE_NAME+"/#", ABSOLUTE_PRESETS_ID);
		URI_MATCHER.addURI(PROVIDER_NAME, "all", ALL_RULES);
	}
	
	private GoClockDBOpenHelper mDBOpenHelper;
	private SQLiteDatabase mDB;
	
	private interface OnUriMatcherMatchInterface {
		public Object onUriMatcherMatch(String table_name, boolean is_single_item,
				boolean all_rules, Uri content_uri);
	}
	
	@Override
	public boolean onCreate() {
		mDBOpenHelper = new GoClockDBOpenHelper(getContext(), 
				GoClockDBOpenHelper.DB_NAME, null, 
				GoClockDBOpenHelper.DB_VERSION);
		mDB = mDBOpenHelper.getWritableDatabase();
		if(mDB == null) {
			return false;
		}
		if(mDB.isReadOnly()) {
			mDB.close();
			mDB = null;
			return false;
		}
		return true;
	}
	
	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		int deleteCount = (Integer) matchUri(uri, new OnUriMatcherMatchInterface() {
			@Override
			public Object onUriMatcherMatch(String table_name, boolean is_single_row,
					boolean all_rules, Uri content_uri) {
				String s = "1";
				if(selection != null) {
					s = new String(selection);
				}
				if(is_single_row) {
					String rowID = uri.getPathSegments().get(1);
					s = BaseTable.ID_COLUMN+"="+rowID
							+ (!TextUtils.isEmpty(s) ? "AND ("+s+")" : "");
				}
				int deleteCount = mDB.delete(table_name, s, selectionArgs);
				return Integer.valueOf(deleteCount);
			}
		});
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		String type = (String)matchUri(uri, new OnUriMatcherMatchInterface() {
			@Override
			public Object onUriMatcherMatch(String table_name, boolean is_single_item,
					boolean all_rules, Uri content_uri) {
				return "vnd.android.cursor."+(is_single_item ? "item" : "dir")+
						"/vnd.brunodea."+table_name;
			}
		});
		return type;
	}

	@Override
	public Uri insert(Uri uri, final ContentValues values) {
		Object obj = matchUri(uri, new OnUriMatcherMatchInterface() {
			@Override
			public Object onUriMatcherMatch(String table_name, boolean is_single_item,
					boolean all_rules, Uri content_uri) {
				// To add empty rows to your database by passing in an empty
				// Content Values object you must use the null column hack
				// parameter to specify the name of the column that can be
				// set to null.
				String nullColumnHack = null;
				long id = mDB.insert(table_name, nullColumnHack, values);
				if(id > -1) {
					Uri insertedId = ContentUris.withAppendedId(content_uri, id);
					GoClockContentProvider.this.getContext().getContentResolver()
						.notifyChange(insertedId, null);
					return insertedId;
				}
				
				return null;
			}
		});
		return (obj == null ? null : (Uri) obj);
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection,
			final String[] selectionArgs, final String sortOrder) {
		return (Cursor) matchUri(uri, new OnUriMatcherMatchInterface() {
			@Override
			public Object onUriMatcherMatch(String table_name, boolean is_single_row,
					boolean all_rules, Uri content_uri) {
				SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
				if(is_single_row) {
					String rowID = uri.getPathSegments().get(1);
					qb.appendWhere(BaseTable.ID_COLUMN+"="+rowID);
				}
				String so = new String(sortOrder);
				if(all_rules) {
					so = BasePresetTable.PRESET_NAME_COLUMN;
				}
				qb.setTables(table_name);
				return qb.query(mDB, projection, selection, selectionArgs, null, null, so);
			}
		});
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection,
			final String[] selectionArgs) {
		int updateCount = (Integer) matchUri(uri, new OnUriMatcherMatchInterface() {
			@Override
			public Object onUriMatcherMatch(String table_name, boolean is_single_item,
					boolean all_rules, Uri content_uri) {
				String s = new String(selection);
				if(is_single_item) {
					String rowID = uri.getPathSegments().get(1);
					s = BaseTable.ID_COLUMN+"="+rowID
							+(!TextUtils.isEmpty(selection) ? 
									" AND ("+selection+")" : "");
				}
				int updateCount = mDB.update(table_name, values, s, selectionArgs);
				
				return updateCount;
			}
		});
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}
	
	private Object matchUri(Uri uri, OnUriMatcherMatchInterface on_uri_match) {
		String table_name = "";
		boolean is_single_item = false;
		boolean all_rules = false;
		Uri content_uri = null;
		switch(URI_MATCHER.match(uri)) {
		case TIME_RULES:
			table_name = TimeRulesTable.TABLE_NAME;
			content_uri = CONTENT_URI_TIME_RULES;
			break;
		case TIME_RULES_ID:
			table_name = TimeRulesTable.TABLE_NAME;
			is_single_item = true;
			content_uri = CONTENT_URI_TIME_RULES;
			break;
		case BYOYOMI_PRESETS:
			table_name = ByoYomiPresetTable.TABLE_NAME;
			content_uri = CONTENT_URI_BYOYOMI_PRESETS;
			break;
		case BYOYOMI_PRESETS_ID:
			table_name = ByoYomiPresetTable.TABLE_NAME;
			is_single_item = true;
			content_uri = CONTENT_URI_BYOYOMI_PRESETS;
			break;
		case CANADIAN_PRESETS:
			table_name = CanadianPresetTable.TABLE_NAME;
			content_uri = CONTENT_URI_CANADIAN_PRESETS;
			break;
		case CANADIAN_PRESETS_ID:
			table_name = CanadianPresetTable.TABLE_NAME;
			is_single_item = true;
			content_uri = CONTENT_URI_CANADIAN_PRESETS;
			break;
		case ABSOLUTE_PRESETS:
			table_name = AbsolutePresetTable.TABLE_NAME;
			content_uri = CONTENT_URI_ABSOLUTE_PRESETS;
			break;
		case ABSOLUTE_PRESETS_ID:
			table_name = AbsolutePresetTable.TABLE_NAME;
			is_single_item = true;
			content_uri = CONTENT_URI_ABSOLUTE_PRESETS;
			break;
		case ALL_RULES:
			table_name = ByoYomiPresetTable.TABLE_NAME+","+CanadianPresetTable.TABLE_NAME+","+AbsolutePresetTable.TABLE_NAME;
			all_rules = true;
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: "+uri);
		}
		return on_uri_match.onUriMatcherMatch(table_name, is_single_item, 
				all_rules, content_uri);
	}
}
