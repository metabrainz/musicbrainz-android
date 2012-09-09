package org.musicbrainz.mobile.view;

import org.musicbrainz.mobile.App;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoLightTextView extends TextView {

    public RobotoLightTextView(Context context) {
        super(context);
    }

    public RobotoLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotoLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf) {
        if (isInEditMode()) {
            super.setTypeface(tf);
        } else {
            super.setTypeface(App.getRobotoLight());
        }
    }

}
