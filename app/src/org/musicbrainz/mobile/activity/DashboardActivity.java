package org.musicbrainz.mobile.activity;

import org.musicbrainz.android.api.webservice.HttpClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.async.LoadPayPalTask;
import org.musicbrainz.mobile.fragment.WelcomeFragment;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;
import org.musicbrainz.mobile.intent.zxing.IntentIntegrator;
import org.musicbrainz.mobile.intent.zxing.IntentResult;
import org.musicbrainz.mobile.view.DashTileView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.paypal.android.MEP.PayPal;

public class DashboardActivity extends MusicBrainzActivity implements OnClickListener {

    private static final int COLLECTION_LOGIN_REQUEST = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        getSupportActionBar().setHomeButtonEnabled(false);
        setupTiles();
        if (savedInstanceState == null) {
            loadPayPalInBackground();
        }
    }

    private void loadPayPalInBackground() {
        PayPal payPal = PayPal.getInstance();
        if (payPal == null || !payPal.isLibraryInitialized()) {
            new LoadPayPalTask().execute();
        }
    }

    private void setupTiles() {
        setupTile(R.id.dash_scan, R.drawable.dash_scan, R.string.dash_scan);
        setupTile(R.id.dash_collections, R.drawable.dash_collections, R.string.dash_collections);
        setupTile(R.id.dash_donate, R.drawable.dash_donate, R.string.dash_donate);
        setupTile(R.id.dash_about, R.drawable.dash_about, R.string.dash_about);
    }

    private void setupTile(int tileId, int iconId, int stringId) {
        DashTileView scanTile = (DashTileView) findViewById(tileId);
        scanTile.setIcon(iconId);
        scanTile.setText(stringId);
        scanTile.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.dash, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isLoggedIn = App.isUserLoggedIn();
        menu.findItem(R.id.menu_login).setVisible(!isLoggedIn);
        menu.findItem(R.id.menu_logout).setVisible(isLoggedIn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            logOut();
            updateWelcomeText();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateWelcomeText() {
        try {
            WelcomeFragment f = (WelcomeFragment) getSupportFragmentManager().findFragmentById(R.id.welcome_fragment);
            f.updateText();
        } catch (Exception e) {
            // Fragment not attached, nothing to do.
        }
    }

    private void logOut() {
        App.getUser().clearData();
        HttpClient.clearCredentials();
        Toast.makeText(this, R.string.toast_logged_out, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dash_scan:
            IntentIntegrator.initiateScan(this, getString(R.string.zx_title), getString(R.string.zx_message),
                    getString(R.string.zx_pos), getString(R.string.zx_neg), IntentIntegrator.PRODUCT_CODE_TYPES);
            break;
        case R.id.dash_collections:
            if (App.isUserLoggedIn()) {
                startActivity(IntentFactory.getCollectionList(getApplicationContext()));
            } else {
                startActivityForResult(IntentFactory.getLogin(getApplicationContext()), COLLECTION_LOGIN_REQUEST);
            }
            break;
        case R.id.dash_donate:
            startActivity(IntentFactory.getDonate(getApplicationContext()));
            break;
        case R.id.dash_about:
            startActivity(IntentFactory.getAbout(getApplicationContext()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.BARCODE_REQUEST) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult.getContents() != null) {
                Intent barcodeResult = new Intent(this, ReleaseActivity.class);
                barcodeResult.putExtra(Extra.BARCODE, scanResult.getContents());
                startActivity(barcodeResult);
            }
        } else if (requestCode == COLLECTION_LOGIN_REQUEST && App.isUserLoggedIn()) {
            startActivity(IntentFactory.getCollectionList(getApplicationContext()));
        }
    }

}