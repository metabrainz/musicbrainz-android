package org.metabrainz.android.presentation.features.userprofile.subscriptions_section

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.metabrainz.android.databinding.FragmentSubscriptionsBinding


class SubscriptionsFragment : Fragment() {
    private lateinit var binding: FragmentSubscriptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
}