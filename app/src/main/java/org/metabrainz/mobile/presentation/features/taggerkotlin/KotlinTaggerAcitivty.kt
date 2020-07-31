package org.metabrainz.mobile.presentation.features.taggerkotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.metabrainz.mobile.R
import org.metabrainz.mobile.presentation.features.login.LoginActivity
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences.STATUS_LOGGED_IN
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences.STATUS_LOGGED_OUT
import org.metabrainz.mobile.presentation.features.login.LogoutActivity
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity
import java.util.*

class KotlinTaggerAcitivty: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_tagger_activity)

        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_login -> {
                if(LoginSharedPreferences.getLoginStatus()== STATUS_LOGGED_OUT)
                    startActivity(Intent(KotlinTaggerAcitivty@this,LoginActivity::class.java))
                else
                    startActivity(Intent(KotlinTaggerAcitivty@this,LogoutActivity::class.java))
                true
            }
            R.id.menu_preferences -> {
                startActivity(Intent(KotlinTaggerAcitivty@this,SettingsActivity::class.java))
            }
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

}