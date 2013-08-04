package br.brunodea.goclock.timerule;

import br.brunodea.goclock.App;
import br.brunodea.goclock.R;

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
		return App.instance().getString(R.string.periods_left)+": "+mByoYomiPeriods;
	}
}
