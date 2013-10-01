package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class WebFragment extends SherlockFragment {

    private WebView webView;
    private MenuItem refresh;
    private WebFragmentCallbacks callbacks;
    private String initialUrl;

    public interface WebFragmentCallbacks {
        public void onPageStart();
        public void onPageFinish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initialUrl = activity.getIntent().getStringExtra(Extra.TARGET_URL);
        try {
            callbacks = (WebFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + WebFragmentCallbacks.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) layout.findViewById(R.id.webview);
        loadWebView();
        return layout;
    }

    private void loadWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new MBWebViewClient());
        webView.loadUrl(initialUrl);
    }

    private class MBWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startRefresh();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            stopRefresh();
        }
    }

    private void startRefresh() {
        if (refresh == null) return;
        refresh.setVisible(false);
        callbacks.onPageStart();
    }

    private void stopRefresh() {
        refresh.setVisible(true);
        callbacks.onPageFinish();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRefresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_web, menu);
        refresh = menu.findItem(R.id.menu_refresh);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            webView.reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
