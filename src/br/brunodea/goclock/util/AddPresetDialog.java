package br.brunodea.goclock.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import br.brunodea.goclock.R;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AddPresetDialog extends SherlockDialogFragment {
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	private NoticeDialogListener mNoticeDialogListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	mNoticeDialogListener = (NoticeDialogListener) activity;
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
		builder.setView(inflater.inflate(R.layout.add_preset_dialog, null));
		builder.setPositiveButton(getActivity().getString(R.string.add), 
				new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mNoticeDialogListener.onDialogPositiveClick(AddPresetDialog.this);
			}
		});
		builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), 
				new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AddPresetDialog.this.getDialog().cancel();
				mNoticeDialogListener.onDialogNegativeClick(AddPresetDialog.this);
			}
		});
		
		
		return builder.create();
	}
}
