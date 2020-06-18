package org.metabrainz.mobile;

import android.content.ComponentName;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ListenService extends NotificationListenerService {

    private MediaSessionManager sessionManager;
    private ListenHandler handler;
    private ListenSessionListener sessionListener;
    private ComponentName listenServiceComponent;

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

        String token = UserPreferences.getPreferenceListenBrainzToken();
        if (token == null || token.isEmpty())
            Toast.makeText(this, "User token has not been set!", Toast.LENGTH_LONG).show();

        handler = new ListenHandler();
        sessionManager = (MediaSessionManager) getApplicationContext()
                .getSystemService(MEDIA_SESSION_SERVICE);
        sessionListener = new ListenSessionListener(handler);
        listenServiceComponent = new ComponentName(this, this.getClass());
        sessionManager.addOnActiveSessionsChangedListener(sessionListener, listenServiceComponent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sessionManager.removeOnActiveSessionsChangedListener(sessionListener);
        sessionListener.clearSessions();
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