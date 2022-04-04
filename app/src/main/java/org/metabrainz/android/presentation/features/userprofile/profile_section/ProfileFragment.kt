package org.metabrainz.android.presentation.features.userprofile.profile_section

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import org.metabrainz.android.presentation.features.userprofile.ProfileTheme

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                ProfileTheme {
                    Surface {
                        ProfileFragmentScreen()
                    }
                }
            }
        }
        return view
    }
}

@Composable
private fun ProfileFragmentScreen(){
    ProfileSectionScreen()
}