package org.metabrainz.mobile.presentation.features.onboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.metabrainz.mobile.databinding.OnboardAllowMeBinding;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;

public class AllowMe extends AppCompatActivity {
    private OnboardAllowMeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OnboardAllowMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.skipBtn.setOnClickListener(v -> {
            UserPreferences.setOnBoardingCompleted();
            Intent intent = new Intent(AllowMe.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        binding.letsGo.setOnClickListener(v -> {
            Intent intent = new Intent(AllowMe.this, GettingStarted.class);
            startActivity(intent);
            finish();
        });
    }
}
