package br.brunodea.goclock.preferences;

import br.brunodea.goclock.preferences.TimePreferenceFragment.MyOnFullscreenModePreferenceChangeListener;
import br.brunodea.goclock.util.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class TimePreferenceActivity extends Activity implements MyOnFullscreenModePreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if(GoClockPreferences.getFullscreen()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new TimePreferenceFragment())
			.commit();
		setResult(Activity.RESULT_OK);
	}
	@Override
	public void onFullscreenModePreferenceChange(boolean fullscreen) {
		Util.adjustActivityFullscreenMode(this, fullscreen);
	}
}
