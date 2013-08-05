package br.brunodea.goclock.preferences;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import br.brunodea.goclock.App;
import br.brunodea.goclock.R;
import br.brunodea.goclock.util.Util;

public class TimeDialogPreference extends DialogPreference {
  private int lastHour=0;
  private int lastMinute=0;
  private int lastSecond=0;
  
  private NumberPicker mHourPicker;
  private NumberPicker mMinPicker;
  private NumberPicker mSecPicker;
  

  public TimeDialogPreference(Context ctxt, AttributeSet attrs) {
    super(ctxt, attrs);
    
    setPositiveButtonText(App.instance().getString(R.string.set));
    setNegativeButtonText(App.instance().getString(R.string.cancel));
  }

  @Override
  protected View onCreateDialogView() {
    View v = View.inflate(getContext(), R.layout.time_picker, null);
    mHourPicker = (NumberPicker) v.findViewById(R.id.np_hour);
    mMinPicker = (NumberPicker) v.findViewById(R.id.np_min);
    mSecPicker = (NumberPicker) v.findViewById(R.id.np_sec);

    mHourPicker.setMinValue(0);
    mHourPicker.setMaxValue(72);
    
    mMinPicker.setMinValue(0);
    mMinPicker.setMaxValue(59);
    
    mSecPicker.setMinValue(0);
    mSecPicker.setMaxValue(59);
    
    return v;
  }
  
  @Override
  protected void onBindDialogView(View v) {
    super.onBindDialogView(v);
    
    mHourPicker.setValue(lastHour);
    mMinPicker.setValue(lastMinute);
    mSecPicker.setValue(lastSecond);
  }
  
  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
      lastHour=mHourPicker.getValue();
      lastMinute=mMinPicker.getValue();
      lastSecond=mSecPicker.getValue();
      
      NumberFormat nf = new DecimalFormat("00");
      String time = nf.format(lastHour)+":"+nf.format(lastMinute)+":"+nf.format(lastSecond);
      
      if (callChangeListener(time)) {
    	  persistString(time);
      }
    }
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getString(index);
  }

  @Override
  protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    String time=null;
    
    if (restoreValue) {
      if (defaultValue==null) {
    	  
        time=getPersistedString("00:10:00");
      }
      else {
        time=getPersistedString(defaultValue.toString());
      }
    }
    else {
      time=defaultValue.toString();
    }
    
    lastHour=Util.getHour(time);
    lastMinute=Util.getMinute(time);
    lastSecond=Util.getSecond(time);
  }
}