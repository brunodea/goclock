package br.brunodea.goclock.preferences;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import br.brunodea.goclock.App;
import br.brunodea.goclock.R;
import br.brunodea.goclock.util.Util;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimeDialogPreference extends DialogPreference {
  private int lastHour=0;
  private int lastMinute=0;
  private int lastSecond=0;
  
  private NumberPicker mHourPicker;
  private NumberPicker mMinPicker;
  private NumberPicker mSecPicker;
  
  private Spinner mHourSpinner;
  private Spinner mMinSpinner;
  private Spinner mSecSpinner;
  

  public TimeDialogPreference(Context ctxt, AttributeSet attrs) {
    super(ctxt, attrs);
    
    setPositiveButtonText(App.instance().getString(R.string.set));
    setNegativeButtonText(App.instance().getString(R.string.cancel));
  }

  @Override
  protected View onCreateDialogView() {
    View v = View.inflate(getContext(), R.layout.time_picker, null);
    
    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
    	mHourPicker = (NumberPicker) v.findViewById(R.id.np_hour);
        mMinPicker = (NumberPicker) v.findViewById(R.id.np_min);
        mSecPicker = (NumberPicker) v.findViewById(R.id.np_sec);
    	
	    mHourPicker.setMinValue(0);
	    mHourPicker.setMaxValue(72);
	    
	    mMinPicker.setMinValue(0);
	    mMinPicker.setMaxValue(59);
	    
	    mSecPicker.setMinValue(0);
	    mSecPicker.setMaxValue(59);
    }
    else {
    	mHourSpinner = (Spinner) v.findViewById(R.id.s_hour);
    	mMinSpinner = (Spinner) v.findViewById(R.id.s_min);
    	mSecSpinner = (Spinner) v.findViewById(R.id.s_sec);
    	
    	List<String> hours = new ArrayList<String>();
    	for(int i=0; i<=72; i++) {
    		hours.add(new String(""+i));
    	}
    	ArrayAdapter<String> hourArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, hours);
    	mHourSpinner.setAdapter(hourArrayAdapter);
    	
    	List<String> mins = new ArrayList<String>();
    	for(int i=0; i<=59; i++) {
    		mins.add(new String(""+i));
    	}
    	ArrayAdapter<String> minsArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mins);
    	mMinSpinner.setAdapter(minsArrayAdapter);
    	mSecSpinner.setAdapter(minsArrayAdapter);
    }
    
    return v;
  }
  
  @Override
  protected void onBindDialogView(View v) {
    super.onBindDialogView(v);
    
    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
	    mHourPicker.setValue(lastHour);
	    mMinPicker.setValue(lastMinute);
	    mSecPicker.setValue(lastSecond);
    } else {
    	mHourSpinner.setSelection(lastHour);
    	mMinSpinner.setSelection(lastMinute);
    	mSecSpinner.setSelection(lastSecond);
    }
  }
  
  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
    	if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
    		lastHour=mHourPicker.getValue();
            lastMinute=mMinPicker.getValue();
            lastSecond=mSecPicker.getValue();
    	} else {
    		lastHour = mHourSpinner.getSelectedItemPosition();
    		lastMinute = mMinSpinner.getSelectedItemPosition();
    		lastSecond = mSecSpinner.getSelectedItemPosition();
    	}
    	
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
    	  
        time=getPersistedString("00:00:00");
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
  
  public void setValues(String time) {
	    lastHour=Util.getHour(time);
	    lastMinute=Util.getMinute(time);
	    lastSecond=Util.getSecond(time);	  
  }
}