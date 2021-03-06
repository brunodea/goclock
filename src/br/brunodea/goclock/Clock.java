package br.brunodea.goclock;

import java.util.Timer;
import java.util.TimerTask;

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
	public static final int IS_ALERT_TIME = 4;
	public static final int NOT_ALERT_TIME = 5;
	
	private CountDownTimer mCountDownTimer;
	private long mMillisUntilFinished;
	private TimeRule mTimeRule;
	
	private Handler mTimeHandler;
	
	private boolean mIsOnAlert;
	Timer alert_timer = null;
	
	private boolean mIsPaused;

	public Clock(TimeRule time_rule, Handler time_handler) {
		mTimeRule = time_rule;
		mMillisUntilFinished = time_rule.getMainTime();
		mTimeHandler = time_handler;
		mIsOnAlert = false;
		mIsPaused = true;
	}
	
	public boolean isPaused() {
		return mIsPaused;
	}
	
	public void pauseTimer() {
		if(mTimeRule.isTimeOver())
			return;
		if(mCountDownTimer != null)
			mCountDownTimer.cancel();
		mMillisUntilFinished = mTimeRule.onPause(mMillisUntilFinished);
		mIsPaused = true;
		mIsOnAlert = false;
		if(alert_timer != null) {
			alert_timer.cancel();
		}
	}
	
	private void initCountDownTimer() {
		mIsPaused = false;

		mCountDownTimer = new CountDownTimer(mMillisUntilFinished, 100) {
			@Override
			public void onTick(long millisUntilFinished) {
				if(mTimeRule.isAlertTime(millisUntilFinished-1000) && !mIsOnAlert) {
					mIsOnAlert = true;
					alert_timer = new Timer();
					alert_timer.schedule(new TimerTask() {
						@Override
						public void run() {
							mTimeHandler.sendEmptyMessage(IS_ALERT_TIME);
						}
					}, 0, 1000);
				} else if(!mTimeRule.isAlertTime(millisUntilFinished-1000)) {
					mTimeHandler.sendEmptyMessage(NOT_ALERT_TIME);
					if(alert_timer != null) {
						alert_timer.cancel();
						alert_timer = null;
					}
					mIsOnAlert = false;
				}

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
				if(alert_timer != null) {
					alert_timer.cancel();
					alert_timer = null;
				}
				mIsOnAlert = false;
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
