package br.brunodea.goclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import br.brunodea.goclock.App;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;
import br.brunodea.goclock.timerule.TimeRule;
import br.brunodea.goclock.util.Util;

public class GoClockPreferences {
	private static final String SHARED_PREF_NAME = "br.brunodea.goclock.GoClock";
	
	private static String getStringPreference(Context c, String key, String def) {
		SharedPreferences s = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return s.getString(key, def);
	}
	private static int getIntegerPreference(Context c, String key, int def) {
		SharedPreferences s = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return s.getInt(key, def);
	}	
	
	public static String getByoYomiMainTimeString() {
		return getStringPreference(App.instance(), "byoyomi_maintime_key", 
				"00:10:00");
	}
	public static long getByoYomiMainTimeMillis()	{
		return Util.timeStringToMillis(getByoYomiMainTimeString());
	}
	public static String getByoYomiExtraTimeString() {
		return getStringPreference(App.instance(), "byoyomi_extratime_key",
				"00:00:30");
	}
	public static long getByoYomiExtraTimeMillis() {
		return Util.timeStringToMillis(getByoYomiExtraTimeString());
	}
	public static int getByoYomiPeriods() {
		return getIntegerPreference(App.instance(), "byoyomi_periods_key", 1);
	}
	
	public static String getTimeRuleString() {
		return getStringPreference(App.instance(), "timerules_key", 
				ByoYomiTimeRule.BYOYOMI_KEY);
	}

	public static TimeRule getTimeRule() {
		String tr = getTimeRuleString();
		TimeRule time_rule = null;
		if(tr.equals(ByoYomiTimeRule.BYOYOMI_KEY)) {
			time_rule = createByoYomiTimeRule();
		} else if(tr.equals(CanadianTimeRule.CANADIAN_KEY)) {
			
		} else {
			time_rule = createByoYomiTimeRule();
		}
		return time_rule;
	}
	
	private static TimeRule createByoYomiTimeRule() {
		return new ByoYomiTimeRule(getByoYomiMainTimeMillis(), 
				getByoYomiExtraTimeMillis(), getByoYomiPeriods());
	}
	private static TimeRule createCanadianTimeRule() {
		return null;
	}
}
