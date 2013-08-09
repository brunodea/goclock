package br.brunodea.goclock.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import br.brunodea.goclock.R;
import br.brunodea.goclock.preferences.GoClockPreferences;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AddPresetDialog extends SherlockDialogFragment {
	public interface OnPresetAddedListener {
        public void onDialogPositiveClick(DialogFragment dialog, String preset_name);
    }
	
	private OnPresetAddedListener mNoticeDialogListener;
	
	public void setNoticeDialogListener(OnPresetAddedListener listener) {
		mNoticeDialogListener = listener;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	mNoticeDialogListener = (OnPresetAddedListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstaceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getActivity().getResources().getString(R.string.add_new_preset_dialog_title));
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.add_preset_dialog, null);
		
		TextView tv = (TextView)v.findViewById(R.id.textview_curr_time_rule_addpreset_dialog);
		tv.setText(GoClockPreferences.getTimeRule().info());
		
		builder.setView(v);
		builder.setPositiveButton(getActivity().getString(R.string.add), 
				new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText et = (EditText) v.findViewById(R.id.edittext_new_preset);
				mNoticeDialogListener.onDialogPositiveClick(AddPresetDialog.this,
						et.getText().toString());
				et.setText("");
			}
		});
		builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), 
				new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AddPresetDialog.this.getDialog().cancel();
			}
		});
		
		
		return builder.create();
	}
}
