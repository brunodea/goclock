package br.brunodea.goclock.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

public class NonEmptyEditTextPreference extends EditTextPreference {
	public NonEmptyEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public NonEmptyEditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public NonEmptyEditTextPreference(Context context) {
		super(context);
	}
	
	@Override
	public String getText() {
		String value = super.getText();
		return TextUtils.isEmpty(value) ? "0" : value;
	}
	
	@Override
	public void setText(String text) {
		if(TextUtils.isEmpty(text)) {
			text = "0";
		}
		super.setText(text);
	}
}
