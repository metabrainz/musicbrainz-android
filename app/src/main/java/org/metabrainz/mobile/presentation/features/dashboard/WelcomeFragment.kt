package org.metabrainz.mobile.presentation.features.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.databinding.FragmentDashWelcomeBinding;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

public class WelcomeFragment extends Fragment {

    private FragmentDashWelcomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateText();
    }

    private void updateText() {
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN) {
            String message = getString(R.string.welcome_loggedin) + " " + LoginSharedPreferences.getUsername();
            binding.welcomeText.setText(message);
        } else {
            binding.welcomeText.setText(R.string.welcome);
        }
    }

}
