package br.brunodea.goclock.preferences;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import br.brunodea.goclock.R;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;

public class TimePreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {
	
	private TimeDialogPreference mByoYomiMainTime;
	private TimeDialogPreference mByoYomiExtraTime;
	private EditTextPreference mByoYomiPeriods;
	
	private TimeDialogPreference mCanadianMainTime;
	private TimeDialogPreference mCanadianExtraTime;
	private EditTextPreference mCanadianStones;
	
	private ListPreference mTimeRuleList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		bindGUI();
		setListeners();
		setPreValues();
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
		
		mTimeRuleList = (ListPreference)
				getPreferenceScreen().findPreference("timerules_key");
		
		String []entries = {ByoYomiTimeRule.BYOYOMI_RULE, CanadianTimeRule.CANADIAN_RULE};
		String []values = {ByoYomiTimeRule.BYOYOMI_KEY, CanadianTimeRule.CANADIAN_KEY};
		mTimeRuleList.setEntries(entries);
		mTimeRuleList.setEntryValues(values);
		mTimeRuleList.setDefaultValue(ByoYomiTimeRule.BYOYOMI_KEY);
	}
	
	private void setListeners() {
		mByoYomiMainTime.setOnPreferenceChangeListener(this);
		mByoYomiExtraTime.setOnPreferenceChangeListener(this);
		mByoYomiPeriods.setOnPreferenceChangeListener(this);

		mCanadianMainTime.setOnPreferenceChangeListener(this);
		mCanadianExtraTime.setOnPreferenceChangeListener(this);
		mCanadianStones.setOnPreferenceChangeListener(this);
		
		mTimeRuleList.setOnPreferenceChangeListener(this);
	}
	
	private void setPreValues() {
		mByoYomiMainTime.setSummary(GoClockPreferences.getByoYomiMainTimeString());
		mByoYomiExtraTime.setSummary(GoClockPreferences.getByoYomiExtraTimeString());
		mByoYomiPeriods.setSummary(GoClockPreferences.getByoYomiPeriods()+"");
		
		mCanadianMainTime.setSummary(GoClockPreferences.getCanadianMainTimeString());
		mCanadianExtraTime.setSummary(GoClockPreferences.getCanadianExtraTimeString());
		mCanadianStones.setSummary(GoClockPreferences.getCanadianStones()+"");
		
		mTimeRuleList.setSummary(GoClockPreferences.getTimeRuleString());
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean ok = true;
		if(preference == mByoYomiMainTime) {
			preference.setSummary(newValue.toString());
		} else if(preference == mByoYomiExtraTime) {
			preference.setSummary(newValue.toString());
		} else if(preference == mByoYomiPeriods) {
			preference.setSummary(newValue.toString());
		} else if(preference == mTimeRuleList) {
			preference.setSummary(GoClockPreferences.getTimeRuleString());
		} else {
			ok = false;
		}
		return ok;
	}
}
