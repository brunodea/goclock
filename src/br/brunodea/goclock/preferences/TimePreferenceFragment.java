package br.brunodea.goclock.preferences;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import br.brunodea.goclock.R;
import br.brunodea.goclock.timerule.AbsoluteTimeRule;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TimePreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {
	
	public interface MyOnFullscreenModePreferenceChangeListener {
		public void onFullscreenModePreferenceChange(boolean fullscreen);
	}
	
	private MyOnFullscreenModePreferenceChangeListener mListener;
	
	private TimeDialogPreference mByoYomiMainTime;
	private TimeDialogPreference mByoYomiExtraTime;
	private EditTextPreference mByoYomiPeriods;
	private TimeDialogPreference mByoYomiAlertTime;
	
	private TimeDialogPreference mCanadianMainTime;
	private TimeDialogPreference mCanadianExtraTime;
	private EditTextPreference mCanadianStones;
	private EditTextPreference mCanadianSecondsPerStone;
	
	private TimeDialogPreference mAbsoluteMainTime;
	
	private ListPreference mTimeRuleList;
	
	private CheckBoxPreference mFullscrenMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		bindGUI();
		setValues();
		setListeners();
		setSummaries();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (MyOnFullscreenModePreferenceChangeListener) activity;
		} catch(ClassCastException	e) {
			throw new ClassCastException(activity.toString()+ 
					" must implement MyOnPreferenceChangeListener");
		}
	}
	
	private void bindGUI() {
		mByoYomiMainTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("byoyomi_maintime_key");
		mByoYomiExtraTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("byoyomi_extratime_key");
		mByoYomiPeriods = (EditTextPreference)
				getPreferenceScreen().findPreference("byoyomi_periods_key");
		mByoYomiAlertTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("byoyomi_alert_key");
		
		mCanadianMainTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("canadian_maintime_key");
		mCanadianExtraTime = (TimeDialogPreference)
				getPreferenceScreen().findPreference("canadian_extratime_key");
		mCanadianStones = (EditTextPreference)
				getPreferenceManager().findPreference("canadian_stones_key");
		mCanadianSecondsPerStone = (EditTextPreference)
				getPreferenceManager().findPreference("canadian_seconds_per_stone_key");
		
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
		mByoYomiAlertTime.setOnPreferenceChangeListener(this);

		mCanadianMainTime.setOnPreferenceChangeListener(this);
		mCanadianExtraTime.setOnPreferenceChangeListener(this);
		mCanadianStones.setOnPreferenceChangeListener(this);
		mCanadianSecondsPerStone.setOnPreferenceChangeListener(this);
		mAbsoluteMainTime.setOnPreferenceChangeListener(this);
		
		mTimeRuleList.setOnPreferenceChangeListener(this);
		
		mFullscrenMode.setOnPreferenceChangeListener(this);
	}
	
	private void setValues() {
		mByoYomiMainTime.setValues(GoClockPreferences.getByoYomiMainTimeString());
		mByoYomiExtraTime.setValues(GoClockPreferences.getByoYomiExtraTimeString());
		mByoYomiPeriods.setText(GoClockPreferences.getByoYomiPeriods()+"");
		mByoYomiAlertTime.setValues(GoClockPreferences.getByoYomiAlertTimeString());

		mCanadianMainTime.setValues(GoClockPreferences.getCanadianMainTimeString());
		mCanadianExtraTime.setValues(GoClockPreferences.getCanadianExtraTimeString());
		mCanadianStones.setText(GoClockPreferences.getCanadianStones()+"");
		mCanadianSecondsPerStone.setText(GoClockPreferences.getCanadianSecondsPerStone()+"");
		
		mAbsoluteMainTime.setValues(GoClockPreferences.getAbsoluteMainTimeString());

		mTimeRuleList.setValue(GoClockPreferences.getTimeRuleKeyString());
	}
	
	private void setSummaries() {
		mByoYomiMainTime.setSummary(GoClockPreferences.getByoYomiMainTimeString());
		mByoYomiExtraTime.setSummary(GoClockPreferences.getByoYomiExtraTimeString());
		mByoYomiPeriods.setSummary(GoClockPreferences.getByoYomiPeriods()+"");
		setByoYomiAlertSummary(GoClockPreferences.getByoYomiAlertTimeString());
		
		mCanadianMainTime.setSummary(GoClockPreferences.getCanadianMainTimeString());
		mCanadianExtraTime.setSummary(GoClockPreferences.getCanadianExtraTimeString());
		mCanadianStones.setSummary(GoClockPreferences.getCanadianStones()+"");
		setCanadianAlertSummary(GoClockPreferences.getCanadianSecondsPerStone());

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
	
	private void setCanadianAlertSummary(int value) {
		String s = getActivity().getString(R.string.canadian_alert_summary);
		mCanadianSecondsPerStone.setSummary(
				String.format(s, value));
	}
	private void setByoYomiAlertSummary(String value) {
		mByoYomiAlertTime.setSummary(
				String.format(
						getActivity()
						.getString(R.string.byoyomi_alert_summary), value));
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(!TextUtils.isEmpty(newValue.toString())) {
			if(preference == mTimeRuleList) {
				setTimeRuleListSummaryFromValue(newValue.toString());
			} else if(preference == mFullscrenMode) {
				mListener.onFullscreenModePreferenceChange((Boolean)newValue);
			} else if(preference == mCanadianSecondsPerStone) {
				setCanadianAlertSummary(Integer.parseInt(newValue.toString()));
			} else if(preference == mByoYomiAlertTime) {
				setByoYomiAlertSummary(newValue.toString());
			} else {
				preference.setSummary(newValue.toString());
			}
		} else {
			if(preference == mByoYomiPeriods || preference == mCanadianStones) {
				preference.setSummary(0);
			} else if(preference == mCanadianSecondsPerStone) {
				setCanadianAlertSummary(0);
			}
		}
		return true;
	}
}
