package org.metabrainz.android.presentation.features.userprofile.tags_section

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import org.metabrainz.android.presentation.features.userprofile.ProfileTheme


class TagsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                ProfileTheme {
                    Surface {
                        TagsFragmentScreen()
                    }
                }
            }
        }
        return view
    }
}

@Composable
private fun TagsFragmentScreen(){
    val constraints = ConstraintSet {
        val chipsSet = createRefFor("chipsSet")
        val predefinedTagSection = createRefFor("genreTypeSection")
        val otherTagSection = createRefFor("otherTypeSection")

        constrain(chipsSet){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(predefinedTagSection){
            top.linkTo(chipsSet.bottom)
            start.linkTo(parent.start)
        }
        constrain(otherTagSection){
            top.linkTo(predefinedTagSection.bottom)
            start.linkTo(parent.start)
        }
    }
    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {

        TagsChipsSection(listOf(UpvotedDownvotedTag.UPVOTE,UpvotedDownvotedTag.DOWNVOTE), layoutID = "chipsSet")
        TagTypeSection("Genres", listOf("Electronic","Experimental","Rock"), layoutID ="genreTypeSection" )
        TagTypeSection("Other Tags", listOf("Cool","Favourite"), layoutID = "otherTypeSection")
    }

}
