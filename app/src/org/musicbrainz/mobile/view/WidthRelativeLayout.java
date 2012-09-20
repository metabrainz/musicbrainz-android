package org.musicbrainz.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class WidthRelativeLayout extends RelativeLayout {

    public WidthRelativeLayout(Context context) {
        super(context);
    }

    public WidthRelativeLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
