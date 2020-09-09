package org.metabrainz.mobile.presentation.features.links

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import org.metabrainz.mobile.data.sources.api.entities.Link
import org.metabrainz.mobile.databinding.FragmentLinksBinding
import org.metabrainz.mobile.presentation.features.base.MusicBrainzFragment
import java.util.*

class LinksFragment : Fragment() {
    private var binding: FragmentLinksBinding? = null
    private lateinit var linkAdapter: LinksAdapter
    private lateinit var linkList: MutableList<Link>
    private val linksViewModel: LinksViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linkList = ArrayList()
        linkAdapter = LinksAdapter(activity, linkList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLinksBinding.inflate(inflater, container, false)
        binding!!.linksList.adapter = linkAdapter
        binding!!.linksList.layoutManager = GridLayoutManager(binding!!.root.context, 2)
        linksViewModel.data.observe(viewLifecycleOwner) { setLinks(it) }
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setLinks(links: List<Link>?) {
        if (links != null) {
            linkList.clear()
            linkList.addAll(links)
            linkAdapter.notifyDataSetChanged()
        }
    }

    companion object : MusicBrainzFragment {
        override fun newInstance(): Fragment {
            return LinksFragment()
        }
    }
}