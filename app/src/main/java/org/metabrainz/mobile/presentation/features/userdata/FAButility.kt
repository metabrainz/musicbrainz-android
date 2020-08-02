package org.metabrainz.mobile.presentation.features.userdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentFabUtilityBinding

class FAButility : Fragment(R.layout.fragment_fab_utility) {

    private lateinit var binding:FragmentFabUtilityBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFabUtilityBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}