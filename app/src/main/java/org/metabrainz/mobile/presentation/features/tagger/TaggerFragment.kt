package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentTaggerBinding

class TaggerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTaggerBinding>(inflater,
                R.layout.fragment_tagger, container, false)
        return binding.root
    }
}