package br.brunodea.goclock;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.brunodea.goclock.db.DBStructure.PresetTable;
import br.brunodea.goclock.db.DBStructure.TimeRulesTable;
import br.brunodea.goclock.db.GoClockContentProvider;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;
import br.brunodea.goclock.timerule.TimeRule;
import br.brunodea.goclock.util.Util;

public class PresetsListActivity extends ListActivity {
	
	private Button mButtonAdd;
	private EditText mEditTextNewPreset;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		Util.adjustActivityFullscreenMode(this);
		
		setContentView(R.layout.preset_activity);
		initGUI();
		
		Cursor cursor = getContentResolver().query(GoClockContentProvider.CONTENT_URI_PRESETS, 
				null, null, null, PresetTable.NAME);
		
		setListAdapter(new PresetCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
	}
	
	private void initGUI(){
		mButtonAdd = (Button)findViewById(R.id.button_add_preset);
		mEditTextNewPreset = (EditText)findViewById(R.id.edittext_new_preset);
		
		mButtonAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = mEditTextNewPreset.getText().toString();
				String msg = "";
				if(name == null || name.equals("")) {
					msg = getResources().getString(R.string.name_cannot_be_empty);
				} else if(presetNameAlreadyTaken(name)) {
					msg = getResources().getString(R.string.preset_name_already_taken);
				}
				if(msg.equals("")) {
					TimeRule curr = GoClockPreferences.getTimeRule();
					String extra = "0";
					String time_rule_key = GoClockPreferences.getTimeRuleKeyString(); 
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
					
					getContentResolver().insert(GoClockContentProvider.CONTENT_URI_PRESETS, values);
					msg = getResources().getString(R.string.add_new_preset);
				}
				Toast.makeText(PresetsListActivity.this, msg, Toast.LENGTH_LONG).show();
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
	
	private class PresetCursorAdapter extends CursorAdapter {
		private LayoutInflater mLayoutInflater;
		public PresetCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View v, Context context, Cursor c) {
			
			int time_rule_id = c.getInt(c.getColumnIndex(PresetTable.TIME_RULE_COLUMN));
			
			Uri rowAddress = ContentUris.withAppendedId(GoClockContentProvider.CONTENT_URI_TIME_RULES, 
					time_rule_id);
			
			Cursor time_rule_cursor = getContentResolver().query(rowAddress,
					new String[] {TimeRulesTable.TIME_RULE_COLUMN}, null, null, null);
			if(time_rule_cursor.moveToFirst()) {
				String time_rule_key = time_rule_cursor.getString(time_rule_cursor
						.getColumnIndex(TimeRulesTable.TIME_RULE_COLUMN));
				
				time_rule_cursor.close();
				
				String name = c.getString(c.getColumnIndex(PresetTable.NAME));
				String maintime = c.getString(c.getColumnIndex(PresetTable.MAIN_TIME));
				String extratime = c.getString(c.getColumnIndex(PresetTable.EXTRA_TIME));
				String extrainfo = c.getString(c.getColumnIndex(PresetTable.EXTRA_INFO));
				
				TimeRule time_rule = Util.timeRuleFactory(time_rule_key, maintime, 
						extratime, extrainfo);
			
				TextView tv_name = (TextView)v.findViewById(R.id.textview_preset_name);
				TextView tv_info = (TextView)v.findViewById(R.id.textview_preset_info);
				
				tv_name.setText(name);
				tv_info.setText(time_rule.extraInfo().replace("\n", " "));
			} else {
				Log.e("PresetCursorAdapter.bindView", "time_rule with id "+time_rule_id+" not found.");
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return mLayoutInflater.inflate(R.layout.preset_item, parent, false);
		}
	}
}
