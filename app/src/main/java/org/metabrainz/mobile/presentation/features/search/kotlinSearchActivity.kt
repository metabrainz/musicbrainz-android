package org.metabrainz.mobile.presentation.features.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.KotlinSearchActivityBinding
import org.metabrainz.mobile.presentation.features.login.LoginActivity
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences
import org.metabrainz.mobile.presentation.features.login.LogoutActivity
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity
import java.util.*

class kotlinSearchActivity : AppCompatActivity() {

    private lateinit var binding: KotlinSearchActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KotlinSearchActivityBinding.inflate(layoutInflater)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_login -> {
                if(LoginSharedPreferences.getLoginStatus()== LoginSharedPreferences.STATUS_LOGGED_OUT)
                    startActivity(Intent(kotlinSearchActivity@this,LoginActivity::class.java))
                else
                    startActivity(Intent(kotlinSearchActivity@this, LogoutActivity::class.java))
                true
            }
            R.id.menu_preferences -> {
                startActivity(Intent(kotlinSearchActivity@this, SettingsActivity::class.java))
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