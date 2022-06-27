package org.metabrainz.android.presentation.features.brainzplayer.services.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.metabrainz.android.R
import org.metabrainz.android.util.BrainzPlayerUtils.NOTIFICATION_CHANNEL_ID
import org.metabrainz.android.util.BrainzPlayerUtils.NOTIFICATION_ID

class BrainzPlayerNotificationManager(
    private val context : Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: ()-> Unit
) {
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setChannelNameResourceId(R.string.brainzplayer_notification_channel_name)
            .setChannelDescriptionResourceId(R.string.brainzplayer_notification_channel_description)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .setNotificationListener(notificationListener)
            .build()
            .apply {
                setSmallIcon(R.drawable.ic_musicbrainz_logo_no_text)
                setMediaSessionToken(sessionToken)
            }
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    inner class DescriptionAdapter(private val mediaController: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            newSongCallback()
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap()
                .load(R.drawable.ic_musicbrainz_logo_no_text)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback.onBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                })
            return null
        }
    }
}
