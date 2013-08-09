package br.brunodea.goclock;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.brunodea.goclock.db.DBStructure.PresetTable;
import br.brunodea.goclock.db.DBStructure.TimeRulesTable;
import br.brunodea.goclock.db.GoClockContentProvider;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.preferences.TimePreferenceActivity;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;
import br.brunodea.goclock.timerule.TimeRule;
import br.brunodea.goclock.util.AddPresetDialog;
import br.brunodea.goclock.util.AddPresetDialog.OnPresetAddedListener;
import br.brunodea.goclock.util.Util;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PresetsFragmentActivity extends SherlockFragmentActivity
	implements ActionMode.Callback, OnPresetAddedListener {
	private static final int PRESETS_SHOW_PREFERENCES = 2;
	private ActionMode mActionMode;
	
	private ListView mListView;
	private PresetCursorAdapter mPresetCursorAdapter;
	private int mSelectedItemPos;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		Util.adjustActivityFullscreenMode(this);
		
		setContentView(R.layout.preset_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initGUI();
		initListeners();

		mActionMode = null;
		mSelectedItemPos = -1;
		
		Cursor cursor = createQueryCursor();
		cursor.setNotificationUri(getContentResolver(), GoClockContentProvider.CONTENT_URI_PRESETS);
		
		mPresetCursorAdapter = new PresetCursorAdapter(this, cursor, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mListView.setAdapter(mPresetCursorAdapter);
	}
	
	private void initGUI(){

		mListView = (ListView)findViewById(R.id.listview_presets);
		
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setSelector(android.R.color.darker_gray);
	}
	
	private Cursor createQueryCursor() {
		return getContentResolver().query(GoClockContentProvider.CONTENT_URI_PRESETS, 
				null, null, null, PresetTable.NAME);
	}
	
	private void initListeners() {

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				if(mActionMode != null) {
					return false;
				}
				mSelectedItemPos = position;
				mActionMode = startActionMode(PresetsFragmentActivity.this);
				v.setSelected(true);
				return true;
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Cursor c = (Cursor)mListView.getItemAtPosition(position);
				TimeRule t = Util.timeRuleFromCursor(c, getContentResolver());
				GoClockPreferences.setTimeRule(t);
				c.close();
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
	}
	
	private boolean presetNameAlreadyTaken(String name) {
		Cursor cursor = getContentResolver().query(GoClockContentProvider.CONTENT_URI_PRESETS,
				new String[]{PresetTable.NAME}, PresetTable.NAME+"=?", 
				new String[] {name}, null);
		boolean taken = cursor.moveToFirst();
		
		cursor.close();
		return taken;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.preset_action_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_add_preset:
			AddPresetDialog preset_dialog = (AddPresetDialog) AddPresetDialog.instantiate(this, 
					"br.brunodea.goclock.util.AddPresetDialog");
			preset_dialog.setNoticeDialogListener(this);
			preset_dialog.show(getSupportFragmentManager(), 
					"br.brunodea.goclock.util.AddPresetDialog");
			return true;
		case R.id.action_settings:
			Intent i = new Intent(this, TimePreferenceActivity.class);
			startActivityForResult(i, PRESETS_SHOW_PREFERENCES);
			//TODO: actionbar em settings.
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PRESETS_SHOW_PREFERENCES) {
			if(resultCode == RESULT_OK) {
				Util.adjustActivityFullscreenMode(this);
			}
		}
	}
	
	// Called when the action mode is created; startActionMode() was called
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.preset_context_menu, menu);
		return true;
	}
	// Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_delete_preset:
			Cursor c = (Cursor) mListView.getAdapter().getItem(mSelectedItemPos);
			int id = c.getInt(c.getColumnIndex(PresetTable.ID_COLUMN));
			getContentResolver().delete(GoClockContentProvider.CONTENT_URI_PRESETS, 
					PresetTable.ID_COLUMN+"=?", new String[]{String.valueOf(id)});
			mode.finish();
			return true;
		default:
			return false;
		}
	}

	// Called when the user exits the action mode
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mActionMode = null;
		mSelectedItemPos = -1;
		mPresetCursorAdapter.changeCursor(createQueryCursor());
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String preset_name) {
		String name = preset_name;
		String msg = "";
		if(name == null || name.equals("")) {
			msg = getResources().getString(R.string.name_cannot_be_empty);
		} else if(presetNameAlreadyTaken(name)) {
			msg = getResources().getString(R.string.preset_name_already_taken);
		}
		if(msg.equals("")) {
			TimeRule curr = GoClockPreferences.getTimeRule();
			String extra = "0";
			String time_rule_key = curr.getTimeRuleKey();
			
			Cursor curs = getContentResolver().query(GoClockContentProvider.CONTENT_URI_TIME_RULES,
					new String[]{TimeRulesTable.ID_COLUMN}, TimeRulesTable.TIME_RULE_COLUMN+"=?", 
					new String[]{time_rule_key}, null);
			int time_rule_id = curs.moveToFirst() ? curs.getInt(0) : 0;
			curs.close();
			if(time_rule_key.equals(ByoYomiTimeRule.BYOYOMI_KEY)) {
				ByoYomiTimeRule b = (ByoYomiTimeRule) curr;
				extra = b.getPeriods()+"";
			} else if(time_rule_key.equals(CanadianTimeRule.CANADIAN_KEY)) {
				CanadianTimeRule c = (CanadianTimeRule) curr;
				extra = c.getStones()+"";
			}
			String maintime = Util.formattedTime(curr.getMainTime());
			String extratime = Util.formattedTime(curr.getByoYomiTime());
			
			ContentValues values = new ContentValues();
			values.put(PresetTable.NAME, name);
			values.put(PresetTable.MAIN_TIME, maintime);
			values.put(PresetTable.EXTRA_TIME, extratime);
			values.put(PresetTable.EXTRA_INFO, extra);
			values.put(PresetTable.TIME_RULE_COLUMN, time_rule_id);
			
			getContentResolver().insert(GoClockContentProvider.CONTENT_URI_PRESETS, values);
			mPresetCursorAdapter.changeCursor(createQueryCursor());
			dialog.dismiss();
		} else {
			Toast.makeText(PresetsFragmentActivity.this, msg, Toast.LENGTH_LONG).show();
		}
	}

	private class PresetCursorAdapter extends CursorAdapter {
		private LayoutInflater mLayoutInflater;
		public PresetCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View v, Context context, Cursor c) {
			TimeRule t = Util.timeRuleFromCursor(c, getContentResolver());
							
			String name = c.getString(c.getColumnIndex(PresetTable.NAME));
			String maintime = Util.formattedTime(t.getMainTime());
			
			TextView tv_name = (TextView)v.findViewById(R.id.textview_preset_name);
			TextView tv_info = (TextView)v.findViewById(R.id.textview_preset_info);
			
			tv_name.setText(name);
			tv_info.setText(maintime+"\n"+t.extraInfo().replace("\n", " "));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return mLayoutInflater.inflate(R.layout.preset_item, parent, false);
		}
	}
}