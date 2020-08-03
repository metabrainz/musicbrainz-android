package org.metabrainz.mobile.presentation.features.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.presentation.features.adapters.SliderAdapter;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;

public class GettingStarted extends AppCompatActivity {

    public ViewPager viewPager;
    public LinearLayout linearLayout;
    public SliderAdapter adapter;
    public TextView[] nDots;
    Button prevB, nextB;

    int currentpage;
    final ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsView(position);
            currentpage = position;

            if (position == 0) {
                prevB.setText("");
                nextB.setText(R.string.next);
            } else if (position == nDots.length - 1) {
                prevB.setVisibility(View.VISIBLE);
                nextB.setText(R.string.finish);
            } else {
                prevB.setVisibility(View.VISIBLE);
                nextB.setVisibility(View.VISIBLE);
                prevB.setText(R.string.previous);
                nextB.setText(R.string.next);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboard_main);
        prevB = findViewById(R.id.prevB);
        nextB = findViewById(R.id.nextB);
        viewPager = findViewById(R.id.slide_id);
        linearLayout = findViewById(R.id.dots);
        adapter = new SliderAdapter(GettingStarted.this);
        viewPager.setAdapter(adapter);
        prevB.setVisibility(View.INVISIBLE);
        addDotsView(0);
        viewPager.addOnPageChangeListener(viewListener);
        nextB.setOnClickListener(v -> {
            if (currentpage == nDots.length - 1) {
                UserPreferences.setOnBoardingCompleted();
                Intent intent = new Intent(GettingStarted.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else viewPager.setCurrentItem(currentpage + 1);
        });
        prevB.setOnClickListener(v -> viewPager.setCurrentItem(currentpage - 1));

    }

    public void addDotsView(int position) {
        nDots = new TextView[4];
        linearLayout.removeAllViews();

        for (int i = 0; i < 4; i++) {
            nDots[i] = new TextView(this);
            nDots[i].setText(Html.fromHtml("&#8226"));
            nDots[i].setTextColor(getResources().getColor(R.color.app_bg_light));
            nDots[i].setTextSize(35);
            linearLayout.addView(nDots[i]);
        }
        if (nDots.length > 0) {
            nDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
}
