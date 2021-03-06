package br.brunodea.goclock.timerule;

import br.brunodea.goclock.App;
import br.brunodea.goclock.R;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.util.Util;

public class CanadianTimeRule extends TimeRule {
	public static final String CANADIAN_RULE = App.instance().getString(R.string.canadian);
	public static final String CANADIAN_KEY = "canadian_rule";

	private int mStonesPerByoYomi;
	private int mCurrStones;
	
	public CanadianTimeRule(long milliSMainTime, 
			long milliSByoYomiTimeMilliS, int stones) {
		super(milliSMainTime, milliSByoYomiTimeMilliS);
		mStonesPerByoYomi = stones;
		mCurrStones = mStonesPerByoYomi;
	}

	@Override
	public void onByoYomiTimeOver() {
		if(mCurrStones > 0) {
			mTimeOver = true;
		}
	}

	@Override
	public long onPause(long millisUntilFinished) {
		if(isMainTimeOver()) {
			mCurrStones -= 1;
			if(mCurrStones == 0) {
				mCurrStones = mStonesPerByoYomi;
				return getByoYomiTime();
			}
		}
		return millisUntilFinished;
	}

	@Override
	public String currentExtraInfo() {
		String text = extraInfo();
		if(isMainTimeOver()) {
			text = "("+mCurrStones+")";
		}
		return text;
	}

	@Override
	public String extraInfo() {
		return CANADIAN_RULE+"\n"+Util.formattedTime(getByoYomiTime())+" ("+mStonesPerByoYomi+")";
	}
	
	@Override
	public boolean isAlertTime(long millisUntilFinished) {
		if(mCurrStones == 0)
			return true;
		float secPerStone = ((float)(millisUntilFinished/1000L)/mCurrStones);
		
		if(secPerStone <= GoClockPreferences.getCanadianSecondsPerStone()) {
			return true;
		}

		return false;
	}
	
	public int getStones() {
		return mStonesPerByoYomi;
	}

	@Override
	public String getTimeRuleKey() {
		return CANADIAN_KEY;
	}
}
