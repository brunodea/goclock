package br.brunodea.goclock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClockFragment extends Fragment {
	
	private TextView mTextViewCurrentTime;
	private CountDownTimer mCountDownTimer;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.clock, container, false);
		mTextViewCurrentTime = (TextView) v.findViewById(R.id.textview_current_time);
		
		mCountDownTimer = new CountDownTimer(TimeSettings.MAIN_TIME_SEC, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
				Date d = new Date(millisUntilFinished);
				mTextViewCurrentTime.setText(format.format(d));
			}
			
			@Override
			public void onFinish() {
			}
		};
		
		mCountDownTimer.start();
		
		return v;
	}
}
