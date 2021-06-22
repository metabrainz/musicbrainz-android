package org.metabrainz.mobile.presentation.features.webview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import org.metabrainz.mobile.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView: WebView = findViewById(R.id.webView)

        webView.setDownloadListener { p0, _, _, _, _ ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(p0)))
        }

        intent.extras?.getString("url","https://metabrainz.org/")?.let {
            webViewFunc(webView, it)
        }
    }

    fun webViewFunc(webView: WebView, url: String) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                webView.reload()
            }
        }
        webView.canGoBack()
        webView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && !webView.canGoBack()) {
                false
            } else if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP) {
                webView.goBack()
                true
            } else true
        }
    }
}