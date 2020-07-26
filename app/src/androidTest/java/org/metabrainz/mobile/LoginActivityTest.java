package org.metabrainz.mobile;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.presentation.features.login.LoginActivity;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> loginTestRule = new IntentsTestRule<>(LoginActivity.class);
    String code = "Nlaa7v15QHm9g8rUOmT3dQ";

    @Before
    public void stubInternetIntent() {
        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_VIEW))
                .respondWith(new Instrumentation.ActivityResult(0,
                        new Intent().setData(Uri.parse(
                                MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI + "?code=" + code))));
    }

    @Test
    public void testLoginAuthorization() {
        Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click());
        Intents.intended(AllOf.allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Uri.parse(MusicBrainzServiceGenerator.AUTH_BASE_URL
                        + "authorize"
                        + "?response_type=code"
                        + "&client_id=" + MusicBrainzServiceGenerator.CLIENT_ID
                        + "&redirect_uri=" + MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI
                        + "&scope=profile%20collection%20tag%20rating"))));
    }
}