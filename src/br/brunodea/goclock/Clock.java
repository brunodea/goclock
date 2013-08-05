package br.brunodea.goclock;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import br.brunodea.goclock.timerule.TimeRule;
import br.brunodea.goclock.util.Util;

public class Clock {
	public static final int TIME_OVER = 0;
	public static final int MAIN_TIME_OVER = 1;
	public static final int BYO_YOMI_TIME_OVER = 2;
	public static final int ON_TICK = 3;
	
	private CountDownTimer mCountDownTimer;
	private long mMillisUntilFinished;
	private TimeRule mTimeRule;
	
	private Handler mTimeHandler;

	public Clock(TimeRule time_rule, Handler time_handler) {
		mTimeRule = time_rule;
		mMillisUntilFinished = time_rule.getMainTime();
		mTimeHandler = time_handler;
	}
	
	public void pauseTimer() {
		if(mTimeRule.isTimeOver())
			return;		
		mCountDownTimer.cancel();
		mMillisUntilFinished = mTimeRule.onPause(mMillisUntilFinished);
	}
	
	private void initCountDownTimer() {
		mCountDownTimer = new CountDownTimer(mMillisUntilFinished, 100) {
			@Override
			public void onTick(long millisUntilFinished) {
				mMillisUntilFinished = millisUntilFinished;
				mTimeHandler.sendEmptyMessage(ON_TICK);
			}
			
			@Override
			public void onFinish() {
				Message msg = new Message();
				if(!mTimeRule.isMainTimeOver()) {
					mTimeRule.setMainTimeOver();
					msg.what = MAIN_TIME_OVER;
				} else {
					msg.what = BYO_YOMI_TIME_OVER;
				}
				
				time_over_handler.sendEmptyMessage(msg.what);
			}
		};
	}
	public void resumeTimer() {
		if(mTimeRule.isTimeOver())
			return;
		
		initCountDownTimer();
		mCountDownTimer.start();
	}
	
	public String formattedTimeLeft() {
		return Util.formattedTime(mMillisUntilFinished);
	}

	public TimeRule getTimeRule() {
		return mTimeRule;
	}
	
	private Handler time_over_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MAIN_TIME_OVER) {
				mTimeHandler.sendEmptyMessage(MAIN_TIME_OVER);
				mMillisUntilFinished = mTimeRule.getByoYomiTime();
				resumeTimer();
			} else if(msg.what == BYO_YOMI_TIME_OVER) {
				mTimeRule.onByoYomiTimeOver();	
				if(mTimeRule.isTimeOver()) {
					mTimeHandler.sendEmptyMessage(TIME_OVER);
				} else {
					mTimeHandler.sendEmptyMessage(BYO_YOMI_TIME_OVER);
					mMillisUntilFinished = mTimeRule.getByoYomiTime();
					resumeTimer();
				}
			}
		}
	};
}
