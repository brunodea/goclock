package br.brunodea.goclock;

import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClockFragment extends Fragment {
	
	private TextView mTextViewTimeLeft;
	private CountDownTimer mCountDownTimer;

	private long mMillisUntilFinished;
	
	private boolean mCurrBaseColorBlack;
	
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		mMillisUntilFinished = TimeSettings.MAIN_TIME_SEC*1000;
		mCurrBaseColorBlack = false;
	}
	
	public void setUpsideDown() {
		mTextViewTimeLeft.setRotation(180.f);
	}
	
	public void setBaseColorBlack() {
		mTextViewTimeLeft.setTextColor(Color.WHITE);
		getView().setBackgroundColor(Color.BLACK);
		mCurrBaseColorBlack = true;
	}
	public void setBaseColorWhite() {
		mTextViewTimeLeft.setTextColor(Color.BLACK);
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
				Log.d("lol","VEIO");
				Log.d("mMillisUntilFinished-millisUntilFinished",mMillisUntilFinished-millisUntilFinished+"");
				if(millisUntilFinished <= 10*1000 && mMillisUntilFinished-millisUntilFinished >= 1000) {
					
				}
				
				mTextViewTimeLeft.setText(DateUtils.formatElapsedTime(millisUntilFinished/1000L));
				mMillisUntilFinished = millisUntilFinished;
			}
			
			@Override
			public void onFinish() {
				mTextViewTimeLeft.setText(getResources().getString(R.string.time_over));
				playNotificationSound();
			}
		};
		mCountDownTimer.start();
	}
	private void playNotificationSound() {
		try {
	        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
	        r.play();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}
