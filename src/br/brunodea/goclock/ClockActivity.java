package br.brunodea.goclock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class ClockActivity extends FragmentActivity {
	
	private ClockFragment mClockFragmentBlack;
	private ClockFragment mClockFragmentWhite;
	
	private enum Turn {
		NONE, WHITE, BLACK;
	}
	
	private Turn mCurrentTurn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_clock);

		mClockFragmentBlack = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.black_frag);
		mClockFragmentWhite = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.white_frag);
		
		mClockFragmentBlack.invertColors();
		mClockFragmentWhite.setUpsideDown();
		
		mCurrentTurn = Turn.NONE;
		
		final LinearLayout l = (LinearLayout) findViewById(R.id.linearlayout_clockactivity);
		l.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == l) {
					nextTurn();
				}
			}
		});
	}
	
	public void nextTurn() {
		if(mCurrentTurn == Turn.NONE) {
			mCurrentTurn = Turn.BLACK;
			mClockFragmentBlack.resumeTimer();
			mClockFragmentWhite.setTimeLeftText();
		} else if(mCurrentTurn == Turn.BLACK) {
			mCurrentTurn = Turn.WHITE;
			mClockFragmentBlack.pauseTimer();
			mClockFragmentWhite.resumeTimer();
		} else if(mCurrentTurn == Turn.WHITE) {
			mCurrentTurn = Turn.BLACK;
			mClockFragmentBlack.resumeTimer();
			mClockFragmentWhite.pauseTimer();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clock, menu);
		return true;
	}
}
