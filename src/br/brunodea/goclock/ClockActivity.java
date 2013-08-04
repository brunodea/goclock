package br.brunodea.goclock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class ClockActivity extends FragmentActivity {
	
	private ClockFragment mClockFragmentBlack;
	private ClockFragment mClockFragmentWhite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_clock);

		mClockFragmentBlack = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.black_frag);
		mClockFragmentWhite = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.white_frag);
		
		mClockFragmentWhite.setUpsideDown();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clock, menu);
		return true;
	}

}
