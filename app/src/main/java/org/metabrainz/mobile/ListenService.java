package org.metabrainz.mobile;

import android.content.ComponentName;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;

import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ListenService extends NotificationListenerService {
    MediaSessionManager sessionManager;
    ListenHandler handler;
    ListenSessionListener sessionListener;
    ComponentName listenServiceComponent;
    private String token;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Listen Service Started");
        return START_STICKY;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        if (Looper.myLooper() == null)
            new Handler(Looper.getMainLooper()).post(this::initialize);
        else
            initialize();
    }

    public void initialize() {
        Log.d("Initializing Listener Service");

        token = UserPreferences.getPreferenceListenBrainzToken();
        if (token == null || token.isEmpty()) {
            Log.d("Abort initialization. User token not set.");
        }

        handler = new ListenHandler();
        sessionManager = (MediaSessionManager) getApplicationContext()
                .getSystemService(MEDIA_SESSION_SERVICE);
        sessionListener = new ListenSessionListener(handler, token);
        listenServiceComponent = new ComponentName(this, this.getClass());
        sessionManager.addOnActiveSessionsChangedListener(sessionListener, listenServiceComponent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}