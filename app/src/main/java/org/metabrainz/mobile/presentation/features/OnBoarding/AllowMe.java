package org.metabrainz.mobile.presentation.features.OnBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.databinding.OnboardAllowMeBinding;
import org.metabrainz.mobile.presentation.features.KotlinDashboard.KotlinDashboardActivity;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

public class AllowMe extends AppCompatActivity {
    private OnboardAllowMeBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OnboardAllowMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSharedPreferences.saveSkipState();
                Intent intent=new Intent(AllowMe.this, KotlinDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AllowMe.this,GettingStarted.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
