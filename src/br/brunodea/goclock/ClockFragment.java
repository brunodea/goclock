package br.brunodea.goclock;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.timerule.TimeRule;

public class ClockFragment extends Fragment {
	
	private TextView mTextViewTimeLeft;
	private TextView mTextViewByoYomiInfo;
	
	private LinearLayout mLinearLayoutClock;
	
	private boolean mCurrBaseColorBlack;
	
	private Clock mClock;
	private Handler mTimeHandler;
	
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		mCurrBaseColorBlack = false;
		initTimeHandler();
		mClock = new Clock(GoClockPreferences.getTimeRule(), mTimeHandler);
	}
	
	public void setTimeRule(TimeRule time_rule) {
		mClock = new Clock(time_rule, mTimeHandler);
	}
	
	public void setUpsideDown() {
		mLinearLayoutClock.setScaleX(-1.f);
		mLinearLayoutClock.setScaleY(-1.f);
	}
	
	public void setBaseColorBlack() {
		mTextViewTimeLeft.setTextColor(Color.WHITE);
		mTextViewByoYomiInfo.setTextColor(Color.WHITE);
		getView().setBackgroundColor(Color.BLACK);
		mCurrBaseColorBlack = true;
	}
	public void setBaseColorWhite() {
		mTextViewTimeLeft.setTextColor(Color.BLACK);
		mTextViewByoYomiInfo.setTextColor(Color.WHITE);
		getView().setBackgroundColor(Color.WHITE);
		mCurrBaseColorBlack = false;
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
		setByoYomiCurrentInfoText();
	}
	
	private void setTimeLeftText() {
		mTextViewTimeLeft.setText(mClock.formattedTimeLeft());
	}
	private void setByoYomiCurrentInfoText() {
		mTextViewByoYomiInfo.setTextSize(getActivity().getResources()
				.getDimension(R.dimen.time_left_font_size));
		mTextViewByoYomiInfo.setText(mClock.getTimeRule().currentExtraInfo());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.clock, container, false);
		mLinearLayoutClock = (LinearLayout) v.findViewById(R.id.linearlayout_clock);
		mTextViewTimeLeft = (TextView) v.findViewById(R.id.textview_time_left);
		mTextViewByoYomiInfo = (TextView) v.findViewById(R.id.textview_byoyomi_info);
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital_clock.ttf");
		mTextViewTimeLeft.setTypeface(tf);
		mTextViewByoYomiInfo.setTypeface(tf);
		
		initialTextValues();
		
		return v;
	}
	
	private void initialTextValues() {
		mTextViewTimeLeft.setText(mClock.formattedTimeLeft());
		mTextViewByoYomiInfo.setText(mClock.getTimeRule().extraInfo());
		mTextViewByoYomiInfo.setTextSize(getActivity().getResources().getDimension(R.dimen.byoyomi_info_font_size));
	}
	
	public void pauseTimer() {
		mClock.pauseTimer();
	}
	public void resumeTimer() {
		mClock.resumeTimer();
	}
	public void reset() {
		mClock = new Clock(GoClockPreferences.getTimeRule(), mTimeHandler);
		initTimeHandler();
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
					mTextViewTimeLeft.setText(getResources().getString(R.string.time_over));
					mTextViewByoYomiInfo.setText("");
				} else if(msg.what == Clock.MAIN_TIME_OVER) {
					playNotificationSound();
				} else if(msg.what == Clock.BYO_YOMI_TIME_OVER) {
					playNotificationSound();
				} else if(msg.what == Clock.ON_TICK) {
					setTimeTextInfos();
				}
			}
		};
	}
}
