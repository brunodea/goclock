package br.brunodea.goclock;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import br.brunodea.goclock.timerule.TimeRule;

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
	
	public void resumeTimer() {
		if(mTimeRule.isTimeOver())
			return;
		
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
		mCountDownTimer.start();
	}
	
	public String formattedTimeLeft() {
		NumberFormat nf = new DecimalFormat("00");
		int h = (int) TimeUnit.MILLISECONDS.toHours(mMillisUntilFinished);
		int m = (int) TimeUnit.MILLISECONDS.toMinutes(mMillisUntilFinished) -
				(int) TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mMillisUntilFinished));
		int s = (int) TimeUnit.MILLISECONDS.toSeconds(mMillisUntilFinished) - 
				(int) TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mMillisUntilFinished));
		
		return nf.format(h)+":"+nf.format(m)+":"+nf.format(s);
	}
	
	public TimeRule getTimeRule() {
		return mTimeRule;
	}
	public void setTimeRule(TimeRule time_rule) {
		mTimeRule = time_rule;
		reset();
	}
	
	public void reset() {
		mMillisUntilFinished = mTimeRule.getMainTime();
		if(mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
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
