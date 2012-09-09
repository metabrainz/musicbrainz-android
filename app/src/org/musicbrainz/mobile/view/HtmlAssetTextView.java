package org.musicbrainz.mobile.view;

import org.musicbrainz.mobile.util.Utils;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

public class HtmlAssetTextView extends TextView {
    
    private Context context;
    
    public HtmlAssetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    
    public void setAsset(String asset) {
        setText(Html.fromHtml(Utils.stringFromAsset(context, asset)));
    }

}
