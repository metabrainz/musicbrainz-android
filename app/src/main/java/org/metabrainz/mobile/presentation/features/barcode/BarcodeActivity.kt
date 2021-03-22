package org.metabrainz.mobile.presentation.features.barcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.metabrainz.mobile.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarcodeActivity extends Activity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView scannerView;
    private boolean cameraPermission;
    private final int CAMERA_PERMISSION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        scannerView = new ZBarScannerView(this);
        contentFrame.addView(scannerView);
        scannerView.setResultHandler(this);

        cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermission) ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraPermission = requestCode == CAMERA_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (cameraPermission)
            scannerView.startCamera();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Camera Permission Denied");
            builder.setMessage("You need to grant MusicBrainz for Android access to your device's " +
                    "camera to scan a barcode.");
            builder.setNegativeButton("Close", (dialog, which) -> finish());
            builder.setOnCancelListener(dialog -> finish());
            builder.setOnDismissListener(dialog -> finish());
            builder.create().show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraPermission) scannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cameraPermission) scannerView.startCamera();

    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BarcodeResultActivity.class);
        intent.putExtra("barcode", rawResult.getContents());
        startActivity(intent);
        finish();
    }

}
