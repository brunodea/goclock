package br.brunodea.goclock.api10;

import android.view.animation.AnimationUtils;
import br.brunodea.goclock.ClockFragment;
import br.brunodea.goclock.R;


public class ClockFragmentApi10 extends ClockFragment {
	@Override
	public void setUpsideDown() {
		mClockView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim));
	}
}
