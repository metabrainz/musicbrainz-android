package org.metabrainz.mobile;

import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ListenSessionListener implements MediaSessionManager.OnActiveSessionsChangedListener {

    private static final String SPOTIFY_PACKAGE_NAME = "com.spotify.music";
    private final List<MediaController> controllers;
    private final ListenHandler handler;
    private final Map<MediaSession.Token, ListenCallback> activeSessions;

    public ListenSessionListener(ListenHandler handler) {
        this.controllers = new ArrayList<>();
        this.handler = handler;
        this.activeSessions = new HashMap<>();
    }

    @Override
    public void onActiveSessionsChanged(@Nullable List<MediaController> controllers) {
        if (controllers == null)
            return;

        clearSessions();
        this.controllers.addAll(controllers);

        for (MediaController controller : controllers) {
            if (!UserPreferences.getPreferenceListeningSpotifyEnabled() &&
                    controller.getPackageName().equals(SPOTIFY_PACKAGE_NAME))
                continue;
            ListenCallback callback = new ListenCallback();
            controller.registerCallback(callback);
        }
    }

    public void clearSessions() {
        for (Map.Entry<MediaSession.Token, ListenCallback> entry : activeSessions.entrySet())
            for (MediaController controller : this.controllers)
                if (controller.getSessionToken().equals(entry.getKey()))
                    controller.unregisterCallback(entry.getValue());
        activeSessions.clear();
        this.controllers.clear();
    }

    private class ListenCallback extends MediaController.Callback {

        String artist, title;
        long timestamp;
        PlaybackState state;
        boolean submitted = true;

        @Override
        public void onMetadataChanged(@Nullable MediaMetadata metadata) {
            if (metadata == null)
                return;
            if (state != null)
                Log.d("Listen Metadata " + state.getState());
            else Log.d("Listen Metadata");

            artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST);
            if (artist == null || artist.isEmpty())
                artist = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST);
            title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE);
            if (artist == null || title == null || artist.isEmpty() || title.isEmpty())
                return;

            if (System.currentTimeMillis() / 1000 - timestamp >= 1000)
                submitted = false;
            timestamp = System.currentTimeMillis() / 1000;

            if (state != null && state.getState() == PlaybackState.STATE_PLAYING && !submitted) {
                handler.submitListen(artist, title, timestamp);
                submitted = true;
            }
        }

        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state) {
            if (state == null)
                return;

            this.state = state;
            Log.d("Listen PlaybackState " + state.getState());

            if (state.getState() == PlaybackState.STATE_PLAYING && !submitted) {
                handler.submitListen(artist, title, timestamp);
                submitted = true;
            }

            if (state.getState() == PlaybackState.STATE_PAUSED ||
                    state.getState() == PlaybackState.STATE_STOPPED) {
                handler.cancelListen(timestamp);
                artist = "";
                title = "";
                timestamp = 0;
            }
        }
    }
}
