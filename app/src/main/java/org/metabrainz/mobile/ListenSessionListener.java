package org.metabrainz.mobile;

import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ListenSessionListener implements MediaSessionManager.OnActiveSessionsChangedListener {

    List<MediaController> controllers;
    ListenHandler handler;
    private String token;

    public ListenSessionListener(ListenHandler handler, String token) {
        this.controllers = new ArrayList<>();
        this.handler = handler;
        this.token = token;
    }

    @Override
    public void onActiveSessionsChanged(@Nullable List<MediaController> controllers) {
        if (controllers == null)
            return;
        this.controllers.clear();
        this.controllers.addAll(controllers);
        for (MediaController controller : controllers)
            controller.registerCallback(new ListenCallback());
    }

    private class ListenCallback extends MediaController.Callback {

        String artist, title;
        long timestamp;

        @Override
        public void onMetadataChanged(@Nullable MediaMetadata metadata) {
            if (metadata == null)
                return;

            artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST);
            if (artist == null || artist.isEmpty())
                artist = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST);
            title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE);
            if (artist == null || title == null || artist.isEmpty() || title.isEmpty())
                return;

            timestamp = System.currentTimeMillis() / 1000;
            handler.submitListen(token, artist, title, timestamp);
        }

        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state) {
            if (state == null)
                return;

            if ((state.getState() == PlaybackState.STATE_PAUSED ||
                 state.getState() == PlaybackState.STATE_STOPPED) &&
                 System.currentTimeMillis() - timestamp < 30000) {
                handler.cancelListen(timestamp);
                artist = "";
                title = "";
                timestamp = 0;
            }
        }
    }
}
