package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.databinding.FragmentTaggerBinding

@AndroidEntryPoint
class TaggerFragment : Fragment() {

    private val tagsList = ArrayList<TagField>()
    private val tagFieldsAdapter = TagFieldsAdapter(tagsList)
    private lateinit var binding: FragmentTaggerBinding
    private val viewModel: TaggerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTaggerBinding.inflate(inflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = tagFieldsAdapter

        viewModel.serverFetchedMetadata.observe(viewLifecycleOwner) {
            tagsList.clear()
            tagsList.addAll(it)
            tagFieldsAdapter.notifyDataSetChanged()
        }
        return binding.root
    }

}