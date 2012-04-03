package org.musicbrainz.mobile.view;

import org.musicbrainz.mobile.util.Utils;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

public class AssetTextView extends TextView {
    
    private Context context;
    
    public AssetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    
    public void setAsset(String asset) {
        setText(Html.fromHtml(Utils.stringFromAsset(context, asset)));
    }

}
