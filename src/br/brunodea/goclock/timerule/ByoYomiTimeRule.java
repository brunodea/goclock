package br.brunodea.goclock.timerule;

import br.brunodea.goclock.App;
import br.brunodea.goclock.R;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.util.Util;

public class ByoYomiTimeRule extends TimeRule {
	public static final String BYOYOMI_RULE = App.instance().getString(R.string.byoyomi);
	public static final String BYOYOMI_KEY = "byoyomi_key";

	private int mByoYomiPeriods;
	private int mByoYomiPeriodsLeft;
	
	public ByoYomiTimeRule(long milliSMainTime, 
			long milliSByoYomiTimeMilliS, int byoYomiPeriods) {
		super(milliSMainTime, milliSByoYomiTimeMilliS);
		mByoYomiPeriods = byoYomiPeriods;
		mByoYomiPeriodsLeft = mByoYomiPeriods;
	}
	
	@Override
	public void onByoYomiTimeOver() {
		mByoYomiPeriodsLeft -= 1;
		if(mByoYomiPeriodsLeft <= 0) {
			mTimeOver = true;
		}
	}
	/**
	 * retorna o tempo que o rel�gio vai come�ar depois que for despausado.
	 */
	@Override
	public long onPause(long millisUntilFinished) {
		if(isMainTimeOver()) {
			return getByoYomiTime();
		}
		return millisUntilFinished;
	}
	@Override
	public String currentExtraInfo() {
		String text = extraInfo();
		if(isMainTimeOver()) {
			text = "("+mByoYomiPeriodsLeft+")";
			if(mByoYomiPeriodsLeft == 1) {
				text = "SD";
			}
		}
		return text;
	}

	@Override
	public String extraInfo() {
		return BYOYOMI_RULE+"\n"+Util.formattedTime(getByoYomiTime())+" ("+mByoYomiPeriods+")";
	}
	
	@Override
	public boolean isAlertTime(long millisUntilFinished) {
		if(millisUntilFinished <= GoClockPreferences.getByoYomiAlertTimeMillis()) {
			return true;
		}
		
		return false;
	}
	
	public int getPeriods() {
		return mByoYomiPeriods;
	}

	@Override
	public String getTimeRuleKey() {
		return BYOYOMI_KEY;
	}
}
