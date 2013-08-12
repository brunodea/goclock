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
import br.brunodea.goclock.preferences.GoClockPreferences;
import br.brunodea.goclock.preferences.TimePreferenceActivity;
import br.brunodea.goclock.preferences.TimePreferenceActivityApi10;
import br.brunodea.goclock.util.Util;

public class ClockActivity extends FragmentActivity {
	public static final int SHOW_PREFERENCES_REQUEST_CODE = 0;
	public static final int PRESETS_REQUEST_CODE = 0;
	private ClockFragment mClockFragmentBlack;
	private ClockFragment mClockFragmentWhite;
	private MediaPlayer mMediaPlayerSuddenDeath;
	private MediaPlayer mMediaPlayerPushButton;
	private MediaPlayer mMediaPlayerTimeOver;
		
	private enum Turn {
		NONE, WHITE, BLACK;
	}
	
	private Turn mCurrentTurn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if(GoClockPreferences.getFullscreen()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		if(GoClockPreferences.getKeepScreenOn()) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		
		setContentView(R.layout.activity_clock);
		mClockFragmentBlack = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.black_frag);
		mClockFragmentWhite = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.white_frag);

		mClockFragmentBlack.setBaseColorBlack();
		mClockFragmentWhite.setBaseColorWhite();
		mClockFragmentWhite.setUpsideDown();

		mMediaPlayerPushButton = MediaPlayer.create(getApplicationContext(), R.raw.pushbutton_amp);
		mMediaPlayerSuddenDeath = MediaPlayer.create(getApplicationContext(), R.raw.snd002_amp);
		mMediaPlayerTimeOver = MediaPlayer.create(getApplicationContext(), R.raw.snd_over_003);
		
		mClockFragmentBlack.setMediaPlayer(mMediaPlayerSuddenDeath);
		mClockFragmentWhite.setMediaPlayer(mMediaPlayerSuddenDeath);
		
		mClockFragmentBlack.setTimeOverMediaPlayer(mMediaPlayerTimeOver);
		mClockFragmentWhite.setTimeOverMediaPlayer(mMediaPlayerTimeOver);
		
		mCurrentTurn = Turn.NONE;
		final LinearLayout l = (LinearLayout) findViewById(R.id.linearlayout_clockactivity);
		l.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == l) {
					if(mClockFragmentBlack.isTimeOver() || mClockFragmentWhite.isTimeOver()) {
						resetClocks();
					} else {
						if(GoClockPreferences.buttonSoundOnTap()) {
							mMediaPlayerPushButton.start();
						}
						if(mCurrentTurn == Turn.NONE) {
							mCurrentTurn = GoClockPreferences.isBlackFirstToPlay() ? Turn.BLACK : Turn.WHITE;
						}
						nextTurn();
					}
				}
			}
		});
	}
	
	private void swapCurrTurn() {
		mCurrentTurn = (mCurrentTurn == Turn.BLACK) ? Turn.WHITE : Turn.BLACK; 
	}
	
	public void nextTurn() {
		ClockFragment to_start = null;
		ClockFragment to_pause = null;
		if(mCurrentTurn == Turn.BLACK) {
			to_start = mClockFragmentBlack;
			to_pause = mClockFragmentWhite;
		} else if(mCurrentTurn == Turn.WHITE) {
			to_start = mClockFragmentWhite;
			to_pause = mClockFragmentBlack;
		}
		boolean first_move = mClockFragmentBlack.isClockPaused() && mClockFragmentWhite.isClockPaused();
		toggleClockFragmentClock(to_start);
		if(!first_move) {
			toggleClockFragmentClock(to_pause);
			swapCurrTurn();
		}
	}
	
	private void toggleClockFragmentClock(ClockFragment clock) {
		if(clock.isClockPaused()) {
			clock.resumeTimer();
		} else {
			clock.pauseTimer();
		}
		clock.setTimeTextInfos();
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
			
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				startActivityForResult(new Intent(this, TimePreferenceActivity.class),
						SHOW_PREFERENCES_REQUEST_CODE);
			} else {
				startActivityForResult(new Intent(this, TimePreferenceActivityApi10.class),
						SHOW_PREFERENCES_REQUEST_CODE);
			}
			break;
		case R.id.action_reset_clock:
			resetClocks();
			break;
		case R.id.action_presets:
			resetClocks();
			startActivityForResult(new Intent(this, PresetsFragmentActivity.class),
					PRESETS_REQUEST_CODE);
			break;
		case R.id.action_pause_clock:
			//ideia:
			//dialog ou textview bem no meio dizendo PAUSE.
			//embaixo dizendo: White to Play ; ou Black to Play dependendo do caso.
			mClockFragmentBlack.pauseTimer();
			mClockFragmentBlack.setTimeTextInfos();
			mClockFragmentWhite.pauseTimer();
			mClockFragmentWhite.setTimeTextInfos();
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
			Util.adjustActivityFullscreenMode(this);
			Util.adjustActivityKeepScreenOn(this);
		} else if(requestCode == PRESETS_REQUEST_CODE) {
			if(resultCode == RESULT_OK) {
				resetClocks();
			}
		}
	}
	
	private void resetClocks() {
		mCurrentTurn = Turn.NONE;
		mClockFragmentBlack.reset();
		mClockFragmentBlack.setBaseColorBlack();
		mClockFragmentWhite.reset();
		mClockFragmentWhite.setBaseColorWhite();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mMediaPlayerSuddenDeath != null)
			mMediaPlayerSuddenDeath.release();
		if(mMediaPlayerPushButton != null)
			mMediaPlayerPushButton.release();
	}
}
