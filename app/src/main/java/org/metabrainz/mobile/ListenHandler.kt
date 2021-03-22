package org.metabrainz.mobile;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.metabrainz.mobile.data.sources.api.ListenSubmitService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.ListenSubmitBody;
import org.metabrainz.mobile.data.sources.api.entities.ListenTrackMetadata;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.media.MediaMetadata.METADATA_KEY_ARTIST;
import static android.media.MediaMetadata.METADATA_KEY_TITLE;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ListenHandler extends Handler {

    private final int DELAY = 30000;
    private final String TIMESTAMP = "timestamp";

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        String token = UserPreferences.getPreferenceListenBrainzToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(App.getInstance(), "User token has not been set!", Toast.LENGTH_LONG).show();
            return;
        }

        ListenSubmitService service = MusicBrainzServiceGenerator
                .createService(ListenSubmitService.class, false);

        ListenTrackMetadata metadata = new ListenTrackMetadata();
        metadata.setArtist(msg.getData().getString(METADATA_KEY_ARTIST));
        metadata.setTrack(msg.getData().getString(METADATA_KEY_TITLE));

        ListenSubmitBody body = new ListenSubmitBody();
        body.addListen(msg.getData().getLong(TIMESTAMP), metadata);
        body.setListenType("single");
        Log.d(body.toString());

        service.submitListen("Token " + token, body)
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        Log.d(response.message());
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                    }
                });
    }

    public void submitListen(String artist, String title, long timestamp) {
        Message message = obtainMessage();
        Bundle data = new Bundle();
        data.putString(METADATA_KEY_ARTIST, artist);
        data.putString(METADATA_KEY_TITLE, title);
        data.putLong(TIMESTAMP, timestamp);
        message.what = Long.valueOf(timestamp).intValue();
        message.setData(data);
        sendMessageDelayed(message, DELAY);
    }

    public void cancelListen(long timestamp) {
        removeMessages(Long.valueOf(timestamp).intValue());
    }
}
