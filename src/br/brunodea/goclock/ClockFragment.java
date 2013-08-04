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
import br.brunodea.goclock.timerule.ByoYomiTimeRule;

public class ClockFragment extends Fragment {
	
	private TextView mTextViewTimeLeft;
	private TextView mTextViewByoYomiInfo;
	
	private LinearLayout mLinearLayoutClock;
	
	private boolean mCurrBaseColorBlack;
	
	private Clock mClock;
	
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		mCurrBaseColorBlack = false;
		mClock = new Clock(new ByoYomiTimeRule(20*1000, 10*1000, 3), mTimeHandler);
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
		setByoYomiInfoText();
	}
	
	private void setTimeLeftText() {
		mTextViewTimeLeft.setText(mClock.formattedTimeLeft());
	}
	private void setByoYomiInfoText() {
		mTextViewByoYomiInfo.setText(mClock.getTimeRule().byoYomiInfo());
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
		
		return v;
	}
	
	public void pauseTimer() {
		mClock.pauseTimer();
	}
	public void resumeTimer() {
		mClock.resumeTimer();
	}
	private void playNotificationSound() {
		//TODO
	}
	
	private Handler mTimeHandler = new Handler() {
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
