package br.brunodea.goclock;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.timerule.TimeRule;

public class ClockFragment extends Fragment {
	
	private TextView mTextViewTimeLeft;
	private TextView mTextViewByoYomiInfo;
	
	private View mClockView;
	
	private boolean mCurrBaseColorBlack;
	private boolean mIsBGRed;
	
	private Clock mClock;
	private Handler mTimeHandler;
	
	private MediaPlayer mMediaPlayer;
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		mCurrBaseColorBlack = false;
		initTimeHandler();
		mClock = new Clock(GoClockPreferences.getTimeRule(), mTimeHandler);
		mIsBGRed = false;
	}
	
	public void setMediaPlayer(MediaPlayer mp) {
		mMediaPlayer = mp;
	}
	
	public void setTimeRule(TimeRule time_rule) {
		mClock = new Clock(time_rule, mTimeHandler);
	}
	
	public void setUpsideDown() {
		mClockView.setScaleX(-1.f);
		mClockView.setScaleY(-1.f);
	}
	
	public void setBaseColorBlack() {
		mTextViewTimeLeft.setTextColor(Color.WHITE);
		mTextViewByoYomiInfo.setTextColor(Color.WHITE);
		getView().setBackgroundColor(Color.BLACK);
		mCurrBaseColorBlack = true;
	}
	public void setBaseColorWhite() {
		mTextViewTimeLeft.setTextColor(Color.BLACK);
		mTextViewByoYomiInfo.setTextColor(Color.BLACK);
		getView().setBackgroundColor(Color.WHITE);
		mCurrBaseColorBlack = false;
	}
	
	private void adjustBaseColor() {
		if(mCurrBaseColorBlack) {
			setBaseColorBlack();
		} else {
			setBaseColorWhite();
		}
	}
	
	public void invertColors() {
		if(mCurrBaseColorBlack) {
			setBaseColorWhite();
		} else {
			setBaseColorBlack();
		}
	}
	
	public void setTimeTextInfos() {
		setTimeLeftText();
		setByoYomiInfoText();
	}
	
	private void setTimeLeftText() {
		mTextViewTimeLeft.setText(mClock.formattedTimeLeft());
	}
	private void setByoYomiInfoText() {
		if(mClock.getTimeRule().isMainTimeOver()) {
			mTextViewByoYomiInfo.setTextSize(getActivity().getResources()
					.getDimension(R.dimen.byoyomi_curr_info_font_size));
		} else {
			mTextViewByoYomiInfo.setTextSize(getActivity().getResources()
					.getDimension(R.dimen.byoyomi_info_font_size));
		}
		mTextViewByoYomiInfo.setText(mClock.getTimeRule().currentExtraInfo());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.clock, container, false);
		mClockView = v;
		mTextViewTimeLeft = (TextView) v.findViewById(R.id.textview_time_left);
		mTextViewByoYomiInfo = (TextView) v.findViewById(R.id.textview_byoyomi_info);
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital_clock.ttf");
		mTextViewTimeLeft.setTypeface(tf);
		mTextViewByoYomiInfo.setTypeface(tf);
		
		initialTextValues();
		
		return v;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		pauseTimer();
		mMediaPlayer.stop();
	}
	
	private void initialTextValues() {
		mTextViewTimeLeft.setText(mClock.formattedTimeLeft());
		mTextViewByoYomiInfo.setText(mClock.getTimeRule().extraInfo());
		mTextViewByoYomiInfo.setTextSize(getActivity().getResources().getDimension(R.dimen.byoyomi_info_font_size));
		if(mCurrBaseColorBlack) {
			mTextViewTimeLeft.setTextColor(Color.WHITE);
			mTextViewByoYomiInfo.setTextColor(Color.WHITE);
		} else {
			mTextViewTimeLeft.setTextColor(Color.BLACK);
			mTextViewByoYomiInfo.setTextColor(Color.BLACK);
		}
	}
	
	public void pauseTimer() {
		adjustBaseColor();
		mIsBGRed = false;
		mClock.pauseTimer();
	}
	public void resumeTimer() {
		mClock.resumeTimer();
	}
	public boolean isTimeOver() {
		return mClock.getTimeRule().isTimeOver();
	}
	public void reset() {
		pauseTimer();
		initTimeHandler();
		mClock = new Clock(GoClockPreferences.getTimeRule(), mTimeHandler);
		initialTextValues();
	}
	
	private void playNotificationSound() {
		//TODO
	}

	private void initTimeHandler() {
		mTimeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == Clock.TIME_OVER) {
					adjustBaseColor();
					mTextViewTimeLeft.setText(getResources().getString(R.string.time_over));
					mTextViewTimeLeft.setTextColor(Color.RED);
					mTextViewByoYomiInfo.setText("");
				} else if(msg.what == Clock.MAIN_TIME_OVER) {
					playNotificationSound();
				} else if(msg.what == Clock.BYO_YOMI_TIME_OVER) {
					playNotificationSound();
				} else if(msg.what == Clock.ON_TICK) {
					setTimeTextInfos();
				} else if(msg.what == Clock.IS_SUDDEN_DEATH && mClock.getTimeRule().isMainTimeOver()) {
					adjustBaseColor();
					if(!mIsBGRed) {
						getView().setBackgroundColor(Color.RED);
						mIsBGRed = true;
					} else {
						mIsBGRed = false;
						mTextViewTimeLeft.setTextColor(Color.RED);
						mTextViewByoYomiInfo.setTextColor(Color.RED);
					}
					if(mMediaPlayer != null) {
						mMediaPlayer.start();
					}
				} else if(msg.what == Clock.NOT_SUDDEN_DEATH) {
				}
			}
		};
	}
}
