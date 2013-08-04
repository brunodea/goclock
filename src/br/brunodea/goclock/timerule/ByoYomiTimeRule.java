package br.brunodea.goclock.timerule;


public class ByoYomiTimeRule extends TimeRule {
	private int mByoYomiPeriods;
	
	public ByoYomiTimeRule(long milliSMainTime, 
			long milliSByoYomiTimeMilliS, int byoYomiPeriods) {
		super(milliSMainTime, milliSByoYomiTimeMilliS);
		mByoYomiPeriods = byoYomiPeriods;
	}
	
	@Override
	public void onByoYomiTimeOver() {
		mByoYomiPeriods -= 1;
		if(mByoYomiPeriods <= 0) {
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
	public String byoYomiInfo() {
		String text = "";
		if(isMainTimeOver()) {
			text = "("+mByoYomiPeriods+")";
			if(mByoYomiPeriods == 1) {
				text = "F";
			}
		}
		return text;
	}
}
