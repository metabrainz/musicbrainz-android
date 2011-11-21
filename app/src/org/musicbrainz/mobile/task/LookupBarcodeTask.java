package org.musicbrainz.mobile.task;

import java.io.IOException;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.BarcodeNotFoundException;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.base.DataQueryActivity;

public class LookupBarcodeTask extends MusicBrainzTask {

    private Credentials creds;
    private Release release;
    private UserData userData;
    private boolean barcodeExists = true;
    
    public LookupBarcodeTask(DataQueryActivity activity) {
        super(activity);
        if (activity.isUserLoggedIn()) {
            creds = activity.getCredentials();
        }
    }
    
    @Override
    protected Void run(String... barcode) throws Exception {
        if (creds == null) {
            getRelease(barcode[0]);
        } else {
            getReleaseWithUserData(barcode[0]);
        }
        return null;
    }
    
    private void getRelease(String barcode) throws IOException {
        WebClient client = new WebClient(userAgent);
        doBarcodeLookup(client, barcode);
        release = client.lookupReleaseFromBarcode(barcode);
    }

    private void getReleaseWithUserData(String barcode) throws IOException {
        WebClient client = new WebClient(creds);
        doBarcodeLookup(client, barcode);
        if (barcodeExists) {
            userData = client.getUserData(MBEntity.RELEASE_GROUP, release.getReleaseGroupMbid());
        }
    }
    
    private void doBarcodeLookup(WebClient client, String barcode) throws IOException {
        try {
            release = client.lookupReleaseFromBarcode(barcode);
        } catch (BarcodeNotFoundException e) {
            barcodeExists = false;
        }
    }
    
    public boolean doesBarcodeExist() {
        return barcodeExists;
    }
    
    public Release getRelease() {
        return release;
    }
    
    public UserData getUserData() {
        return userData;
    }
    
}
