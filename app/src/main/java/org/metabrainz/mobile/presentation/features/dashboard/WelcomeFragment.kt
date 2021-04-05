package org.metabrainz.mobile.presentation.features.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentDashWelcomeBinding
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences.loginStatus
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences.username

class WelcomeFragment : Fragment() {
    private var binding: FragmentDashWelcomeBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDashWelcomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        updateText()
    }

    private fun updateText() {
        if (loginStatus == LoginSharedPreferences.STATUS_LOGGED_IN) {
            val message = getString(R.string.welcome_loggedin) + " " + username
            binding!!.welcomeText.text = message
        } else {
            binding!!.welcomeText.setText(R.string.welcome)
        }
    }
}