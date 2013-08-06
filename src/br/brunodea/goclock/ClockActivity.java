package br.brunodea.goclock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import br.brunodea.goclock.preferences.TimePreferenceActivity;

public class ClockActivity extends FragmentActivity {
	public static final int SHOW_PREFERENCES_REQUEST_CODE = 0;
	private ClockFragment mClockFragmentBlack;
	private ClockFragment mClockFragmentWhite;
	private MediaPlayer mMediaPlayer;
	
	private enum Turn {
		NONE, WHITE, BLACK;
	}
	
	private Turn mCurrentTurn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.snd002);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_clock);
		
		mClockFragmentBlack = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.black_frag);
		mClockFragmentWhite = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.white_frag);
		
		mClockFragmentBlack.setBaseColorBlack();
		mClockFragmentWhite.setUpsideDown();
		
		mClockFragmentBlack.setMediaPlayer(mMediaPlayer);
		mClockFragmentWhite.setMediaPlayer(mMediaPlayer);
		
		mCurrentTurn = Turn.NONE;
		
		final LinearLayout l = (LinearLayout) findViewById(R.id.linearlayout_clockactivity);
		l.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == l) {
					if(mClockFragmentBlack.isTimeOver() || mClockFragmentWhite.isTimeOver()) {
						resetClocks();
					} else {
						nextTurn();
					}
				}
			}
		});
	}
	
	public void nextTurn() {
		if(mCurrentTurn == Turn.NONE) {
			mCurrentTurn = Turn.BLACK;
			mClockFragmentBlack.resumeTimer();
			mClockFragmentWhite.setTimeTextInfos();
		} else if(mCurrentTurn == Turn.BLACK) {
			mCurrentTurn = Turn.WHITE;
			mClockFragmentBlack.pauseTimer();
			mClockFragmentBlack.setTimeTextInfos();
			mClockFragmentWhite.resumeTimer();
		} else if(mCurrentTurn == Turn.WHITE) {
			mCurrentTurn = Turn.BLACK;
			mClockFragmentWhite.pauseTimer();
			mClockFragmentWhite.setTimeTextInfos();
			mClockFragmentBlack.resumeTimer();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clock, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			resetClocks();
			Intent i = new Intent(this, TimePreferenceActivity.class);
			startActivityForResult(i, SHOW_PREFERENCES_REQUEST_CODE);
			break;
		case R.id.action_reset_clock:
			resetClocks();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == SHOW_PREFERENCES_REQUEST_CODE) {
			resetClocks();
		}
	}
	
	private void resetClocks() {
		mCurrentTurn = Turn.NONE;
		mClockFragmentBlack.reset();
		mClockFragmentWhite.reset();
	}
}
