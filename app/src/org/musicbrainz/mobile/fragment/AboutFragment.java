package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.view.HtmlAssetTextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends SherlockFragment {
    
    private TextView versionLabel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_about, container);
        versionLabel = (TextView) layout.findViewById(R.id.version_text);
        HtmlAssetTextView body = (HtmlAssetTextView) layout.findViewById(R.id.about_text);
        body.setAsset("about.html");
        return layout;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        versionLabel.setText(getText(R.string.version_text) + " " + App.getVersion());
    }
    
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_about, menu);
    }

}
