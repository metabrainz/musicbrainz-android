package org.metabrainz.android.presentation.features.brainzplayer.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.metabrainz.android.presentation.features.brainzplayer.ui.BrainzPlayerViewModel
import org.metabrainz.android.util.BrainzPlayerExtensions.toSong

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayPauseIcon(icon: ImageVector, viewModel: BrainzPlayerViewModel , modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = icon,
        transitionSpec = {
            when (targetState) {
                Icons.Rounded.PlayArrow -> {
                    slideInHorizontally { height -> -height } + fadeIn() with
                            slideOutHorizontally { height -> height } + fadeOut()
                }
                else -> {
                    slideInHorizontally { height -> height } + fadeIn() with
                            slideOutHorizontally{ height -> -height } + fadeOut()
                }
            }.using(SizeTransform(false))
        }

    ) {
        Icon(imageVector = it, contentDescription = "", modifier.clickable {
            viewModel.playOrToggleSong(viewModel.currentlyPlayingSong.value.toSong,viewModel.isPlaying.value)
        })
    }
}