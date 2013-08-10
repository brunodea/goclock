package br.brunodea.goclock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import br.brunodea.goclock.db.DBStructure.PresetTable;
import br.brunodea.goclock.db.DBStructure.TimeRulesTable;
import br.brunodea.goclock.timerule.AbsoluteTimeRule;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;

public class GoClockDBOpenHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "goclock.db";
	public static final int DB_VERSION = 1;
	
	public GoClockDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase db) {
		for(String script : DBStructure.createTablesScript()) {
			db.execSQL(script);
		}
		/* insert time rules keys */
		db.execSQL("INSERT INTO "+TimeRulesTable.TABLE_NAME+" VALUES(0,'"+ByoYomiTimeRule.BYOYOMI_KEY+"');");
		db.execSQL("INSERT INTO "+TimeRulesTable.TABLE_NAME+" VALUES(1,'"+CanadianTimeRule.CANADIAN_KEY+"');");
		db.execSQL("INSERT INTO "+TimeRulesTable.TABLE_NAME+" VALUES(2,'"+AbsoluteTimeRule.ABSOLUTE_KEY+"');");
		/* insert default time rules */
		insertDefaultTimeRules("World Amateur Go Championship", 0, "01:00:00", "00:00:30", "3", db);
		insertDefaultTimeRules("American Go Association", 1, "00:30:00", "00:05:00", "20", db);
		insertDefaultTimeRules("Lighting Go", 2, "00:15:00", "00:00:00", "0", db);
	}
	
	private void insertDefaultTimeRules(String name, int time_rule, String maintime, String extratime,
			String extrainfo, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(PresetTable.NAME, name);
		values.put(PresetTable.TIME_RULE_COLUMN, time_rule);
		values.put(PresetTable.MAIN_TIME, maintime);
		values.put(PresetTable.EXTRA_TIME, extratime);
		values.put(PresetTable.EXTRA_INFO, extrainfo);
		db.insert(PresetTable.TABLE_NAME, null, values);
	}
	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
