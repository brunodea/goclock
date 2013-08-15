package br.brunodea.goclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.brunodea.goclock.App;
import br.brunodea.goclock.timerule.AbsoluteTimeRule;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;
import br.brunodea.goclock.timerule.CanadianTimeRule;
import br.brunodea.goclock.timerule.TimeRule;
import br.brunodea.goclock.util.Util;

public class GoClockPreferences {	
	private static String getStringPreference(Context c, String key, String def) {
		SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(c);
		return s.getString(key, def);
	}
	private static void setStringPreference(Context c, String key, String value) {
		SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(c);
		s.edit().putString(key, value)
			.commit();
	}
	private static boolean getBooleanPreference(Context c, String key, boolean def) {
		SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(c);
		return s.getBoolean(key, def);
	}
	
	/* Byo Yomi */
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
		return Integer.parseInt(getStringPreference(App.instance(), "byoyomi_periods_key", 
				"3"));
	}
	public static String getByoYomiAlertTimeString() {
		return getStringPreference(App.instance(), "byoyomi_alert_key", 
				"00:00:10");
	}
	public static long getByoYomiAlertTimeMillis() {
		return Util.timeStringToMillis(getByoYomiAlertTimeString());
	}
	/////////////////////////////////////////////////////////////
	/* Canadian */
	public static String getCanadianMainTimeString() {
		return getStringPreference(App.instance(), "canadian_maintime_key", 
				"00:10:00");
	}
	public static long getCanadianMainTimeMillis() {
		return Util.timeStringToMillis(getCanadianMainTimeString());
	}
	public static String getCanadianExtraTimeString() {
		return getStringPreference(App.instance(), "canadian_extratime_key", 
				"00:01:00");
	}
	public static long getCanadianExtraTimeMillis() {
		return Util.timeStringToMillis(getCanadianExtraTimeString());
	}
	public static int getCanadianStones() {
		return Integer.parseInt(getStringPreference(App.instance(), "canadian_stones_key", 
				"10"));
				
	}
	public static float getCanadianSecondsPerStone() {
		return Float.parseFloat(getStringPreference(App.instance(), "canadian_seconds_per_stone_key", 
				"5"));
				
	}
	
	/////////////////////////////////////////////////////////////
	/* Absolute */
	public static String getAbsoluteMainTimeString() {
		return getStringPreference(App.instance(), "absolute_maintime_key",
				"00:10:00");
	}
	public static long getAbsoluteMainTimeMillis() {
		return Util.timeStringToMillis(getAbsoluteMainTimeString());
	}
	
	/////////////////////////////////////////////////////////////
	/* Time Rule */
	public static String getTimeRuleKeyString() {
		return getStringPreference(App.instance(), "timerules_key", 
				ByoYomiTimeRule.BYOYOMI_KEY);
	}

	public static String getTimeRuleString() {
		String tr = getTimeRuleKeyString();
		String res = ByoYomiTimeRule.BYOYOMI_RULE;
		if(tr.equals(ByoYomiTimeRule.BYOYOMI_KEY)) {
			res = ByoYomiTimeRule.BYOYOMI_RULE;
		} else if(tr.equals(CanadianTimeRule.CANADIAN_KEY)) {
			res = CanadianTimeRule.CANADIAN_RULE;
		} else if(tr.equals(AbsoluteTimeRule.ABSOLUTE_KEY)) {
			res = AbsoluteTimeRule.ABSOLUTE_RULE;
		}
		
		return res;
	}
	
	public static void setTimeRule(TimeRule time_rule) {
		String tr = time_rule.getTimeRuleKey();
		setStringPreference(App.instance(), "timerules_key", tr);
		String maintime_key = "byoyomi_maintime_key";
		String extratime_key = "byoyomi_extratime_key";
		if(tr.equals(ByoYomiTimeRule.BYOYOMI_KEY)) {
			setStringPreference(App.instance(), "byoyomi_periods_key", 
					((ByoYomiTimeRule)time_rule).getPeriods()+"");
		} else if(tr.equals(CanadianTimeRule.CANADIAN_KEY)) {
			maintime_key = "canadian_maintime_key";
			extratime_key = "canadian_extratime_key";
			setStringPreference(App.instance(), "canadian_stones_key", 
					((CanadianTimeRule)time_rule).getStones()+"");
		}
		if(tr.equals(AbsoluteTimeRule.ABSOLUTE_KEY)) {
			maintime_key = "absolute_maintime_key";
		} else {
			setStringPreference(App.instance(), extratime_key,
					Util.formattedTime(time_rule.getByoYomiTime()));
		}
		setStringPreference(App.instance(), maintime_key,
				Util.formattedTime(time_rule.getMainTime()));
	}
	
	public static TimeRule getTimeRule() {
		String tr = getTimeRuleKeyString();
		TimeRule time_rule = null;
		if(tr.equals(ByoYomiTimeRule.BYOYOMI_KEY)) {
			time_rule = createByoYomiTimeRule();
		} else if(tr.equals(CanadianTimeRule.CANADIAN_KEY)) {
			time_rule = createCanadianTimeRule();
		} else if(tr.equals(AbsoluteTimeRule.ABSOLUTE_KEY)) {
			time_rule = createAbsoluteTimeRule();
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
		return new CanadianTimeRule(getCanadianMainTimeMillis(),
				getCanadianExtraTimeMillis(), getCanadianStones());
	}
	private static TimeRule createAbsoluteTimeRule() {
		return new AbsoluteTimeRule(getAbsoluteMainTimeMillis());
	}
	/////////////////////////////////////////////////////////////
	/* General */
	public static boolean isBlackFirstToPlay() {
		return getBooleanPreference(App.instance(), "first_to_play_key", true);
	}
	public static boolean getFullscreen() {
		return getBooleanPreference(App.instance(), "fullscreen_key", true);
	}
	public static boolean getKeepScreenOn() {
		return getBooleanPreference(App.instance(), "keep_screen_on_key", true);
	}
	public static boolean blinkOnSuddenDeath() {
		return getBooleanPreference(App.instance(), "blink_on_sudden_death_key", true);
	}
	public static boolean beepOnSuddenDeath() {
		return getBooleanPreference(App.instance(), "beep_on_sudden_death_key", true);
	}
	public static boolean buttonSoundOnTap() {
		return getBooleanPreference(App.instance(), "button_sound_on_tap_key", true);
	}
	public static boolean beepOnTimeTransition() {
		return getBooleanPreference(App.instance(), "beep_on_time_transition_key", true);
	}
}
