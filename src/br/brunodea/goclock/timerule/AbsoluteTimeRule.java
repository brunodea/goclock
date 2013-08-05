package br.brunodea.goclock.timerule;

import br.brunodea.goclock.App;
import br.brunodea.goclock.R;

public class AbsoluteTimeRule extends TimeRule {
	public static final String ABSOLUTE_RULE = App.instance().getString(R.string.absolute);
	public static final String ABSOLUTE_KEY = "absolute_key";
	
	public AbsoluteTimeRule(long milliSMainTime) {
		super(milliSMainTime, 0);
	}
	@Override
	public void onByoYomiTimeOver() {
		mTimeOver = true;
	}

	@Override
	public long onPause(long millisUntilFinished) {
		return millisUntilFinished;
	}

	@Override
	public String currentExtraInfo() {
		return "";
	}
	@Override
	public String extraInfo() {
		return ABSOLUTE_RULE;
	}
}
