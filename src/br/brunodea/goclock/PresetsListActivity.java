package br.brunodea.goclock;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import br.brunodea.goclock.db.DBStructure.PresetTable;
import br.brunodea.goclock.db.DBStructure.TimeRulesTable;
import br.brunodea.goclock.db.GoClockContentProvider;
import br.brunodea.goclock.timerule.TimeRule;
import br.brunodea.goclock.util.Util;

public class PresetsListActivity extends ListActivity {	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		Util.adjustActivityFullscreenMode(this);
		Cursor cursor = getContentResolver().query(GoClockContentProvider.CONTENT_URI_PRESETS, 
				null, null, null, PresetTable.PRESET_NAME_COLUMN);
		
		setListAdapter(new PresetCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
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
				String name = c.getString(c.getColumnIndex(PresetTable.PRESET_NAME_COLUMN));
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
