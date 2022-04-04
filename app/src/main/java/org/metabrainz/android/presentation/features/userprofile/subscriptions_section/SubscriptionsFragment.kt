package org.metabrainz.android.presentation.features.userprofile.subscriptions_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.fragment.app.Fragment
import org.metabrainz.android.presentation.features.userprofile.ProfileTheme


class SubscriptionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                ProfileTheme {
                    Surface {
                        SubscriptionSectionScreen()
                    }
                }
            }
        }
        return view
    }
}

@Composable
fun SubscriptionSectionScreen(){

    val constraints = ConstraintSet {
        val chipSet = createRefFor("chipSet")
        val subscriptions = createRefFor("subscriptions")

        constrain(chipSet){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(subscriptions){
            top.linkTo(chipSet.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    ConstraintLayout(constraints,modifier = Modifier.fillMaxSize()) {
        val subscriptionEntities = listOf("Artist", "Series", "Label", "Collection", "Editor")
        val subscriptions = listOf("P!nk","Arijit Singh","Coldplay")
        SubscriptionChipSection(subscriptionEntities, "chipSet")
        SubscriptionsCards(subscriptions,"subscriptions")
    }
}