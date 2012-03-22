package org.musicbrainz.mobile.dialog;

import org.musicbrainz.mobile.fragment.LoginFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AuthenticatingDialog extends DialogFragment {
    
    public static final String TAG = "loading";
    private static final String MESSAGE = "messageId";
    
    private Context context;

    public static AuthenticatingDialog newInstance(int messageId) {
        AuthenticatingDialog dialog = new AuthenticatingDialog();
        Bundle args = new Bundle();
        args.putInt(MESSAGE, messageId);
        dialog.setArguments(args);
        return dialog;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getMessage());
        dialog.setCancelable(true);
        return dialog;
    }
    
    private String getMessage() {
        int messageId = getArguments().getInt(MESSAGE);
        return context.getResources().getString(messageId);
    }
    
    @Override
    public void onCancel(DialogInterface dialog) {
        getLoaderManager().destroyLoader(LoginFragment.LOGIN_LOADER);
    }

}
