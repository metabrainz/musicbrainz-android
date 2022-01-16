package org.metabrainz.android.presentation.features.userdata

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.metabrainz.android.R
import org.metabrainz.android.databinding.FragmentFabUtilityBinding

class FAButility : Fragment(R.layout.fragment_fab_utility) {

    private lateinit var binding:FragmentFabUtilityBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFabUtilityBinding.bind(view)

    }
}