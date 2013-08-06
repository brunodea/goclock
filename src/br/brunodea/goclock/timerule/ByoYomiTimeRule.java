package br.brunodea.goclock.timerule;

import br.brunodea.goclock.App;
import br.brunodea.goclock.R;
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
	 * retorna o tempo que o relógio vai começar depois que for despausado.
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
	public boolean isSuddenDeath(long millisUntilFinished) {
		if(mByoYomiPeriodsLeft == 1) {
			return true;
		}
		
		return false;
	}
}
