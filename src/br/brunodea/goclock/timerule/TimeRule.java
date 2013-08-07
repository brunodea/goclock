package br.brunodea.goclock.timerule;


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
	
	public boolean isSuddenDeath(long millisUntilFinished) {
		return false;
	}
	
	public abstract String getTimeRuleKey();
	public abstract void onByoYomiTimeOver();
	public abstract long onPause(long millisUntilFinished);
	public abstract String currentExtraInfo(); //informações atuais sobre a regra (e.g. número de pedras).
	public abstract String extraInfo(); //informações extra sobre a regra.
}
