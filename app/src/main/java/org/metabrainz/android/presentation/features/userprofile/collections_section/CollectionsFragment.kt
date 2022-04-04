package org.metabrainz.android.presentation.features.userprofile.collections_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.metabrainz.android.presentation.features.userprofile.ProfileTheme


class CollectionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                ProfileTheme {
                    Surface {
                        CollectionFragmentSection()
                    }
                }
            }
        }
        return view
    }
}

@Composable
fun CollectionFragmentSection(){
    CollectionsSectionScreen()
}