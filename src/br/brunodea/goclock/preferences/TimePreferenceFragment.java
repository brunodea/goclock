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
import br.brunodea.goclock.timerule.TimeRule;

public class TimePreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {
	
	private TimeDialogPreference mByoYomiMainTime;
	private TimeDialogPreference mByoYomiExtraTime;
	private EditTextPreference mByoYomiPeriods;
	
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
		mTimeRuleList = (ListPreference)
				getPreferenceScreen().findPreference("timerules_key");
		
		String []entries = {ByoYomiTimeRule.BYOYOMI_RULE, CanadianTimeRule.CANADIAN_RULE};
		String []values = {ByoYomiTimeRule.BYOYOMI_KEY, CanadianTimeRule.CANADIAN_KEY};
		mTimeRuleList.setEntries(entries);
		mTimeRuleList.setEntryValues(values);
	}
	
	private void setListeners() {
		mByoYomiMainTime.setOnPreferenceChangeListener(this);
		mByoYomiExtraTime.setOnPreferenceChangeListener(this);
		mByoYomiPeriods.setOnPreferenceChangeListener(this);

		mTimeRuleList.setOnPreferenceChangeListener(this);
	}
	
	private void setPreValues() {
		mByoYomiMainTime.setSummary(GoClockPreferences.getByoYomiMainTimeString());
		mByoYomiExtraTime.setSummary(GoClockPreferences.getByoYomiExtraTimeString());
		mByoYomiPeriods.setSummary(GoClockPreferences.getByoYomiPeriods()+" "+
		getActivity().getString(R.string.periods));
		
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
			preference.setSummary(GoClockPreferences.getByoYomiPeriods()+" "+
					getActivity().getString(R.string.periods));
		} else if(preference == mTimeRuleList) {
			preference.setSummary(GoClockPreferences.getTimeRuleString());
		} else {
			ok = false;
		}
		return ok;
	}
}
