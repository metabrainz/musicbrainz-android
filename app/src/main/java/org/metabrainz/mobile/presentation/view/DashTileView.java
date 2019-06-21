package org.metabrainz.mobile.presentation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.metabrainz.mobile.R;

public class DashTileView extends RelativeLayout {

    private ImageView icon;
    private TextView text;

    public DashTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_dash_tile, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setFocusable(true);
        setBackgroundResource(R.drawable.dash_grid);
        icon = findViewById(R.id.dash_tile_icon);
        text = findViewById(R.id.dash_tile_text);
    }

    public void setIcon(int drawableId) {
        icon.setImageResource(drawableId);
    }

    public void setText(int stringId) {
        text.setText(stringId);
    }

}
