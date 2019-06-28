package org.metabrainz.mobile.presentation.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import org.metabrainz.mobile.util.Utils;

public class HtmlAssetTextView extends AppCompatTextView {

    private final Context context;

    public HtmlAssetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setAsset(String asset) {
        setText(Html.fromHtml(Utils.stringFromAsset(context, asset)));
    }

}
