package br.brunodea.goclock.preferences;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.WindowManager;
import br.brunodea.goclock.R;
import br.brunodea.goclock.preferences.TimePreferenceFragment.MyOnFullscreenModePreferenceChangeListener;
import br.brunodea.goclock.timerule.AbsoluteTimeRule;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;
import br.brunodea.goclock.util.Util;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class TimePreferenceActivityApi10 extends SherlockPreferenceActivity implements OnPreferenceChangeListener {
	private TimeDialogPreference mByoYomiMainTime;
	private TimeDialogPreference mByoYomiExtraTime;
	private EditTextPreference mByoYomiPeriods;
	
	private TimeDialogPreference mCanadianMainTime;
	private TimeDialogPreference mCanadianExtraTime;
	private EditTextPreference mCanadianStones;
	
	private TimeDialogPreference mAbsoluteMainTime;
	
	private ListPreference mTimeRuleList;
	
	private CheckBoxPreference mFullscrenMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(GoClockPreferences.getFullscreen()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}		

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		bindGUI();
		setValues();
		setListeners();
		setSummaries();
	}
	
	private void bindGUI() {
		mByoYomiMainTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("byoyomi_maintime_key");
		mByoYomiExtraTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("byoyomi_extratime_key");
		mByoYomiPeriods = (EditTextPreference)
				getPreferenceScreen().findPreference("byoyomi_periods_key");
		
		mCanadianMainTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("canadian_maintime_key");
		mCanadianExtraTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("canadian_extratime_key");
		mCanadianStones = (EditTextPreference)
				getPreferenceManager().findPreference("canadian_stones_key");
		
		mAbsoluteMainTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("absolute_maintime_key");
		
		mTimeRuleList = (ListPreference)
				getPreferenceScreen().findPreference("timerules_key");

		mFullscrenMode = (CheckBoxPreference)
				getPreferenceScreen().findPreference("fullscreen_key");
		
		String []entries = {ByoYomiTimeRule.BYOYOMI_RULE, CanadianTimeRule.CANADIAN_RULE,
				AbsoluteTimeRule.ABSOLUTE_RULE};
		String []values = {ByoYomiTimeRule.BYOYOMI_KEY, CanadianTimeRule.CANADIAN_KEY,
				AbsoluteTimeRule.ABSOLUTE_KEY};
		mTimeRuleList.setEntries(entries);
		mTimeRuleList.setEntryValues(values);
	}
	
	private void setListeners() {
		mByoYomiMainTime.setOnPreferenceChangeListener(this);
		mByoYomiExtraTime.setOnPreferenceChangeListener(this);
		mByoYomiPeriods.setOnPreferenceChangeListener(this);

		mCanadianMainTime.setOnPreferenceChangeListener(this);
		mCanadianExtraTime.setOnPreferenceChangeListener(this);
		mCanadianStones.setOnPreferenceChangeListener(this);
		
		mAbsoluteMainTime.setOnPreferenceChangeListener(this);
		
		mTimeRuleList.setOnPreferenceChangeListener(this);
		
		mFullscrenMode.setOnPreferenceChangeListener(this);
	}
	
	private void setValues() {
		mByoYomiMainTime.setValues(GoClockPreferences.getByoYomiMainTimeString());
		mByoYomiExtraTime.setValues(GoClockPreferences.getByoYomiExtraTimeString());
		mByoYomiPeriods.setText(GoClockPreferences.getByoYomiPeriods()+"");
		
		mCanadianMainTime.setValues(GoClockPreferences.getCanadianMainTimeString());
		mCanadianExtraTime.setValues(GoClockPreferences.getCanadianExtraTimeString());
		mCanadianStones.setText(GoClockPreferences.getCanadianStones()+"");

		mAbsoluteMainTime.setValues(GoClockPreferences.getAbsoluteMainTimeString());
		
		mTimeRuleList.setValue(GoClockPreferences.getTimeRuleKeyString());
	}
	
	private void setSummaries() {
		mByoYomiMainTime.setSummary(GoClockPreferences.getByoYomiMainTimeString());
		mByoYomiExtraTime.setSummary(GoClockPreferences.getByoYomiExtraTimeString());
		mByoYomiPeriods.setSummary(GoClockPreferences.getByoYomiPeriods()+"");
		
		mCanadianMainTime.setSummary(GoClockPreferences.getCanadianMainTimeString());
		mCanadianExtraTime.setSummary(GoClockPreferences.getCanadianExtraTimeString());
		mCanadianStones.setSummary(GoClockPreferences.getCanadianStones()+"");
		mAbsoluteMainTime.setSummary(GoClockPreferences.getAbsoluteMainTimeString());
		
		mTimeRuleList.setSummary(GoClockPreferences.getTimeRuleString());
	}
	
	private void setTimeRuleListSummaryFromValue(String value) {
		CharSequence summary = "";
		for(int i = 0; i < mTimeRuleList.getEntryValues().length; i++) {
			if(mTimeRuleList.getEntryValues()[i].equals(value)) {
				summary = mTimeRuleList.getEntries()[i];
			}
		}
		mTimeRuleList.setSummary(summary);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		preference.setSummary(newValue.toString());
		if(preference == mTimeRuleList) {
			setTimeRuleListSummaryFromValue(newValue.toString());
		} else if(preference == mFullscrenMode) {
			Util.adjustActivityFullscreenMode(this, (Boolean)newValue);
		}
		return true;
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
