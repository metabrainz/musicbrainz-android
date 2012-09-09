package org.musicbrainz.mobile.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class MusicBrainzBackupAgent extends BackupAgentHelper {
    
    private static final String PREFERENCES_NAME = "org.musicbrainz.mobile_preferences";
    private static final String PREFERENCES_BACKUP_KEY = "MusicBrainzPrefs";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(getApplicationContext(), PREFERENCES_NAME);
        addHelper(PREFERENCES_BACKUP_KEY, helper);
    }
    
}
