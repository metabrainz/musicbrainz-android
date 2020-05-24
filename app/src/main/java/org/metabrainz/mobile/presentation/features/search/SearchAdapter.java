package org.metabrainz.mobile.presentation.features.search;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.presentation.features.label.LabelActivity;

import java.util.List;

abstract class SearchAdapter extends RecyclerView.Adapter {
    protected List<? extends MBEntity> data;
    protected MBEntities entity;
    private int lastPosition = -1;

    SearchAdapter(List<? extends MBEntity> data, MBEntities entity) {
        this.data = data;
        this.entity = entity;
    }

    void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }

    void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils
                    .loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void resetAnimation() {
        lastPosition = -1;
    }

    void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), LabelActivity.class);
        intent.putExtra(entity.name, data.get(position).getMbid());
        view.getContext().startActivity(intent);
    }
}
