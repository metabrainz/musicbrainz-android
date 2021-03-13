package org.metabrainz.mobile.presentation.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import org.metabrainz.mobile.App;

public class RobotoLightTextView extends AppCompatTextView {

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
