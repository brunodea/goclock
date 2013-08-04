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
	public abstract void onByoYomiTimeOver();
	public abstract long onPause(long millisUntilFinished);
	public abstract String byoYomiInfo();
}
