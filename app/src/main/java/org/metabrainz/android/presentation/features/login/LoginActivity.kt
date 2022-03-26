package org.metabrainz.android.presentation.features.login

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.api.MusicBrainzServiceGenerator
import org.metabrainz.android.data.sources.api.entities.AccessToken
import org.metabrainz.android.data.sources.api.entities.userdata.UserInfo
import org.metabrainz.android.databinding.ActivityLoginBinding
import org.metabrainz.android.presentation.features.base.MusicBrainzActivity
import org.metabrainz.android.presentation.features.dashboard.DashboardActivity
import org.metabrainz.android.presentation.features.userprofile.ProfileActivity
import org.metabrainz.android.util.Log.d

@AndroidEntryPoint
class LoginActivity : MusicBrainzActivity() {

    private var binding: ActivityLoginBinding? = null
    private var loginViewModel: LoginViewModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel!!.accessTokenLiveData!!.observe(this) { accessToken: AccessToken? ->
            saveOAuthToken(accessToken)
        }
        loginViewModel!!.userInfoLiveData!!.observe(this) { userInfo: UserInfo? ->
            saveUserInfo(userInfo)
        }
        when (LoginSharedPreferences.loginStatus) {
            LoginSharedPreferences.STATUS_LOGGED_IN -> {
                startActivity(Intent(this,ProfileActivity::class.java))
                finish()
//                binding!!.loginPromptId.setText(R.string.logout_prompt)
//                binding!!.loginBtn.setText(R.string.logout)
//                binding!!.loginBtn.setOnClickListener { logoutUser() }
            }
            else -> binding!!.loginBtn.setOnClickListener { startLogin() }
        }
    }

    override fun onResume() {
        val callbackUri = intent.data
        if (callbackUri != null && callbackUri.toString().startsWith(MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)) {
            val code = callbackUri.getQueryParameter("code")
            if (code != null) loginViewModel!!.fetchAccessToken(code)
        }
        super.onResume()
    }

    private fun startLogin() {
        val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(MusicBrainzServiceGenerator.AUTH_BASE_URL
                        + "authorize"
                        + "?response_type=code"
                        + "&client_id=" + MusicBrainzServiceGenerator.CLIENT_ID
                        + "&redirect_uri=" + MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI
                        + "&scope=profile%20collection%20tag%20rating"))
        startActivity(intent)
    }

    private fun saveOAuthToken(accessToken: AccessToken?) {
        when {
            accessToken != null -> {
                d(accessToken.accessToken)
                LoginSharedPreferences.saveOAuthToken(accessToken)
                loginViewModel!!.fetchUserInfo()
            }
            else -> {
                Toast.makeText(applicationContext,
                    "Failed to obtain access token ",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserInfo(userInfo: UserInfo?) {
        if (userInfo != null &&
                LoginSharedPreferences.loginStatus == LoginSharedPreferences.STATUS_LOGGED_OUT) {
            LoginSharedPreferences.saveUserInfo(userInfo)
            Toast.makeText(applicationContext,
                    "Login successful. " + userInfo.username + " is now logged in.",
                    Toast.LENGTH_LONG).show()
            startActivity(Intent(this, DashboardActivity::class.java))
            d(userInfo.username)
            finish()
        }
    }

    private fun logoutUser() {
        LoginSharedPreferences.logoutUser()
        Toast.makeText(applicationContext,
                "User has successfully logged out.",
                Toast.LENGTH_LONG).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.findItem(R.id.menu_open_website).isVisible = false
        return true
    }
}