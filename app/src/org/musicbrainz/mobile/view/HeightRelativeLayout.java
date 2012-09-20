package org.musicbrainz.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HeightRelativeLayout extends RelativeLayout {

    public HeightRelativeLayout(Context context) {
        super(context);
    }

    public HeightRelativeLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
