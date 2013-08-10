package br.brunodea.goclock;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.timerule.TimeRule;

public class ClockFragment extends Fragment {

	private TextView mTextViewTimeLeft;
	private TextView mTextViewByoYomiInfo;
	
	private LinearLayout mDisplayBackgroundLayout;
	
	protected View mClockView;
	
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
	
	public boolean isClockPaused() {
		return mClock.isPaused();
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setUpsideDown() {
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			mClockView.setScaleX(-1.f);
			mClockView.setScaleY(-1.f);
		} else {
			mClockView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim));
		}
	}
	
	public void setBaseColorBlack() {
		mDisplayBackgroundLayout.setBackgroundColor(Color.BLACK);
		mTextViewTimeLeft.setTextColor(Color.GREEN);
		mTextViewByoYomiInfo.setTextColor(Color.WHITE);
		if(getView() != null) {
			getView().setBackgroundColor(Color.BLACK);
			setBackgroundDrawable(R.drawable.bg_black);
		}
		mCurrBaseColorBlack = true;
	}
	public void setBaseColorWhite() {
		mDisplayBackgroundLayout.setBackgroundColor(Color.BLACK);
		mTextViewTimeLeft.setTextColor(Color.GREEN);
		mTextViewByoYomiInfo.setTextColor(Color.BLACK);
		if(getView() != null) {
			getView().setBackgroundColor(Color.WHITE);
			setBackgroundDrawable(R.drawable.bg_white);
		}
		mCurrBaseColorBlack = false;
	}
	
	public void setBackgroundDrawable(int drawableId) {
		getView().setBackgroundDrawable(getResources().getDrawable(drawableId));
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
		mDisplayBackgroundLayout = (LinearLayout) v.findViewById(R.id.layout_display);
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital_clock.ttf");
		mTextViewTimeLeft.setTypeface(tf);
		mTextViewByoYomiInfo.setTypeface(tf);
		
		initialTextValues();
		
		return v;
	}
	
	@Override
	public void onDestroy() {
		pauseTimer();
		super.onDestroy();
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
				} else if(msg.what == Clock.BYO_YOMI_TIME_OVER) {
				} else if(msg.what == Clock.ON_TICK) {
					setTimeTextInfos();
				} else if(msg.what == Clock.IS_SUDDEN_DEATH && mClock.getTimeRule().isMainTimeOver()) {
					if(GoClockPreferences.blinkOnSuddenDeath()) {
						adjustBaseColor();
						if(!mIsBGRed) {
							mIsBGRed = true;
							mTextViewTimeLeft.setTextColor(Color.WHITE);
							mDisplayBackgroundLayout.setBackgroundColor(Color.RED);
						} else {
							mIsBGRed = false;
							mTextViewTimeLeft.setTextColor(Color.RED);
							mDisplayBackgroundLayout.setBackgroundColor(Color.BLACK);
						}
					}
					if(GoClockPreferences.beepOnSuddenDeath()) {
						if(mMediaPlayer != null) {
							mMediaPlayer.start();
						}
					}
				} else if(msg.what == Clock.NOT_SUDDEN_DEATH) {
				}
				
				mClockView.postInvalidate();
			}
		};
	}
}
