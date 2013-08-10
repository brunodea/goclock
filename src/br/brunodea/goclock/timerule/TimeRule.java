package br.brunodea.goclock.timerule;

import br.brunodea.goclock.util.Util;


public abstract class TimeRule {	
	private long mMainTimeMilliS;	
	private long mByoYomiTimeMilliS;

	protected boolean mMainTimeOver;
	protected boolean mTimeOver;
	
	public TimeRule(long milliSMainTime, long milliSByoYomiTimeMilliS) {
		mMainTimeMilliS = milliSMainTime;
		mByoYomiTimeMilliS = milliSByoYomiTimeMilliS;
		mMainTimeOver = false;
		mTimeOver = false;
	}
	
	public void setMainTime(long milliSecs) {
		mMainTimeMilliS = milliSecs;
	}
	
	public long getMainTime() {
		return mMainTimeMilliS;
	}
	
	public void setByoYomiTime(long milliSecs) {
		mByoYomiTimeMilliS = milliSecs;
	}
	
	public long getByoYomiTime() {
		return mByoYomiTimeMilliS;
	}
	
	public void setMainTimeOver() {
		mMainTimeOver = true;
	}
	
	public boolean isMainTimeOver() {
		return mMainTimeOver;
	}
	
	public boolean isTimeOver() {
		return mTimeOver;
	}
	
	public boolean isAlertTime(long millisUntilFinished) {
		return false;
	}
	
	public String info() {
		return Util.formattedTime(mMainTimeMilliS)+"\n"+extraInfo();
	}
	
	public abstract String getTimeRuleKey();
	public abstract void onByoYomiTimeOver();
	public abstract long onPause(long millisUntilFinished);
	public abstract String currentExtraInfo(); //informa��es atuais sobre a regra (e.g. n�mero de pedras).
	public abstract String extraInfo(); //informa��es extra sobre a regra.
}
