package org.musicbrainz.mobile.fragment.contracts;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class ContractFragment<T> extends Fragment {

    private T contract;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Activity activity) {
        try {
            contract = (T) activity;
        } catch (ClassCastException e) {
            throw new IllegalStateException(activity.getClass().getSimpleName() + " must implement "
                    + getClass().getSimpleName() + "'s contract interface.", e);
        }
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contract = null;
    }

    public final T getContract() {
        return contract;
    }

}
