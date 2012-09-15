package org.musicbrainz.mobile.dialog;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.intent.IntentFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BarcodeNotFoundDialog extends DialogFragment {

    public static final String TAG = "barcode_not_found";
    
    private BarcodeNotFoundCallback callback;
    
    public interface BarcodeNotFoundCallback {
        void addBarcode();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (BarcodeNotFoundCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + BarcodeNotFoundCallback.class.getSimpleName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.barcode_header);
        builder.setNegativeButton(android.R.string.cancel, cancelListener);

        if (App.isUserLoggedIn()) {
            builder.setMessage(R.string.barcode_info_log);
            builder.setPositiveButton(R.string.barcode_btn, addBarcodeListener);
        } else {
            builder.setMessage(R.string.barcode_info_nolog);
            builder.setPositiveButton(R.string.login, loginListener);
        }
        return builder.create();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
    }
    
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }
    
    DialogInterface.OnClickListener addBarcodeListener = new DialogInterface.OnClickListener() {
        
        @Override
        public void onClick(DialogInterface dialog, int which) {
                callback.addBarcode();
        }
    };
    
    DialogInterface.OnClickListener loginListener = new DialogInterface.OnClickListener() {
        
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(IntentFactory.getLogin(getActivity()));
            getDialog().cancel();
        }
    };
    
    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        
        @Override
        public void onClick(DialogInterface dialog, int which) {
            getDialog().cancel();
        }
    };

}
