package br.brunodea.goclock.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import br.brunodea.goclock.preferences.TimePreferenceFragment.MyOnFullscreenModePreferenceChangeListener;
import br.brunodea.goclock.util.Util;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class TimePreferenceActivity extends SherlockFragmentActivity implements MyOnFullscreenModePreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(GoClockPreferences.getFullscreen()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new TimePreferenceFragment())
			.commit();
		setResult(Activity.RESULT_OK);
	}
	
	@Override
	public void onFullscreenModePreferenceChange(boolean fullscreen) {
		Util.adjustActivityFullscreenMode(this, fullscreen);
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
}
