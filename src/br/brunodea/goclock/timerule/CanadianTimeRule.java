package br.brunodea.goclock.timerule;

import br.brunodea.goclock.App;
import br.brunodea.goclock.R;

public class CanadianTimeRule extends TimeRule {

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
	public String byoYomiInfo() {
		String text = "";
		if(isMainTimeOver()) {
			text = "("+mCurrStones+")";
		}
		return text;
	}

}
