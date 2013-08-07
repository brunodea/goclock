package br.brunodea.goclock;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import br.brunodea.goclock.db.DBStructure.AbsolutePresetTable;
import br.brunodea.goclock.db.DBStructure.ByoYomiPresetTable;
import br.brunodea.goclock.db.DBStructure.CanadianPresetTable;
import br.brunodea.goclock.db.GoClockContentProvider;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;

public class PresetsListActivity extends ListActivity {	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		
		
		Cursor cursor = getContentResolver().query(GoClockContentProvider.CONTENT_URI_ALL, projection, selection, selectionArgs, sortOrder)
		
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.preset_item, c, from, 
				to, flags)
		
		/*
		 * simplecursoradapter -> dar uma olhada.
		 * CursorAdapter adapter = new CursorAdapter(this, ) {
			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				// TODO Auto-generated method stub
				
			}
		};
		
		*/
	}/*
	
	private ArrayList<ByoYomiTimeRule> getByoYomiTimeRules() {
		ArrayList<ByoYomiTimeRule> rules = new ArrayList<ByoYomiTimeRule>();
		
		Cursor byoyomi_cursor = getContentResolver().query(GoClockContentProvider.CONTENT_URI_BYOYOMI_PRESETS, 
				null, null, null, ByoYomiPresetTable.PRESET_NAME_COLUMN);
		
		if(byoyomi_cursor.moveToFirst()) {
			do
			{
				
			} while(byoyomi_cursor.moveToNext());
		}
		
		return rules;
	}
	*/
	 
}
