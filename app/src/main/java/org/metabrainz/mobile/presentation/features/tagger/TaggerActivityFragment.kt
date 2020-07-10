package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.metabrainz.mobile.R
import androidx.databinding.DataBindingUtil
import org.metabrainz.mobile.databinding.FragmentTaggerActivityBinding

class TaggerActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTaggerActivityBinding>(inflater,
                R.layout.fragment_tagger_activity,container,false)



        return binding.root
    }
}