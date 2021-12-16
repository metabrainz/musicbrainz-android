package org.metabrainz.android.presentation.features.barcode

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import org.metabrainz.android.R

class BarcodeActivity : Activity(), ZBarScannerView.ResultHandler {
    private var scannerView: ZBarScannerView? = null
    private var cameraPermission = false
    private val CAMERA_PERMISSION = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
        val contentFrame = findViewById<ViewGroup>(R.id.content_frame)
        scannerView = ZBarScannerView(this)
        contentFrame.addView(scannerView)
        scannerView!!.setResultHandler(this)
        cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (!cameraPermission) ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPermission = requestCode == CAMERA_PERMISSION && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (cameraPermission) scannerView!!.startCamera() else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Camera Permission Denied")
            builder.setMessage("You need to grant MusicBrainz for Android access to your device's " +
                    "camera to scan a barcode.")
            builder.setNegativeButton("Close") { dialog: DialogInterface?, which: Int -> finish() }
            builder.setOnCancelListener { dialog: DialogInterface? -> finish() }
            builder.setOnDismissListener { dialog: DialogInterface? -> finish() }
            builder.create().show()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (cameraPermission) scannerView!!.stopCamera()
    }

    public override fun onResume() {
        super.onResume()
        if (cameraPermission) scannerView!!.startCamera()
    }

    override fun handleResult(rawResult: Result) {
        Toast.makeText(this, "Contents = " + rawResult.contents +
                ", Format = " + rawResult.barcodeFormat.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, BarcodeResultActivity::class.java)
        intent.putExtra("barcode", rawResult.contents)
        startActivity(intent)
        finish()
    }
}