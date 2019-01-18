package org.metabrainz.android.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;

import org.metabrainz.android.util.Utils;

public class HtmlAssetTextView extends AppCompatTextView {
    
    private Context context;
    
    public HtmlAssetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    
    public void setAsset(String asset) {
        setText(Html.fromHtml(Utils.stringFromAsset(context, asset)));
    }

}
