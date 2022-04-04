package org.metabrainz.android.presentation.features.userprofile.ratings_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.fragment.app.Fragment
import org.metabrainz.android.presentation.features.userprofile.ProfileTheme



class RatingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                ProfileTheme {
                    Surface {
                        RatingsFragmentScreen()
                    }
                }
            }
        }
        return view
    }
}

@Composable
fun RatingsFragmentScreen(){
    val constraints = ConstraintSet {
        val chipSet = createRefFor("chipSet")
        val ratings = createRefFor("ratings")

        constrain(chipSet){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(ratings){
            top.linkTo(chipSet.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
    ConstraintLayout(constraints,modifier = Modifier.fillMaxSize()) {
        val ratingEntities = listOf("Artist","Event","Label","Release Group","Recording","Work")
        val ratedEntities = listOf(Pair("Fall Out Boy",3f),Pair("Sean Paul",4.5f),Pair("Ed Sheeren",5f))
        RatingChipsSection(ratingEntities,"chipSet")
        Ratings(items = ratedEntities, layoutID = "ratings")

    }
}

