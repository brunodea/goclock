package br.brunodea.goclock;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClockFragment extends Fragment {
	
	private TextView mTextViewTimeLeft;
	private CountDownTimer mCountDownTimer;

	private long mMillisUntilFinished;
	
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		mMillisUntilFinished = TimeSettings.MAIN_TIME_SEC*1000;
	}
	
	public void setUpsideDown() {
		mTextViewTimeLeft.setRotation(180.f);
	}
	
	public void invertColors() {
		mTextViewTimeLeft.setTextColor(Color.WHITE);
		getView().setBackgroundColor(Color.BLACK);
	}
	
	public void setTimeLeftText() {
		mTextViewTimeLeft.setText(DateUtils.formatElapsedTime(mMillisUntilFinished/1000L));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.clock, container, false);
		mTextViewTimeLeft = (TextView) v.findViewById(R.id.textview_time_left);	
		return v;
	}
	
	public void pauseTimer() {
		mCountDownTimer.cancel();
	}
	public void resumeTimer() {
		mCountDownTimer = new CountDownTimer(mMillisUntilFinished, 1) {
			@Override
			public void onTick(long millisUntilFinished) {
				mTextViewTimeLeft.setText(DateUtils.formatElapsedTime(millisUntilFinished/1000L));
				mMillisUntilFinished = millisUntilFinished;
			}
			
			@Override
			public void onFinish() {
				mTextViewTimeLeft.setText(getResources().getString(R.string.time_over));
			}
		};
		mCountDownTimer.start();
	}
}
