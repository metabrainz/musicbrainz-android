package org.metabrainz.mobile.presentation.features.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import org.jetbrains.annotations.NotNull;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;

public class ResultPagingAdapter extends PagingDataAdapter<ResultItem, ResultViewHolder> {

    private final MBEntityType entity;
    private int lastPosition = -1;

    public ResultPagingAdapter(@NotNull DiffUtil.ItemCallback<ResultItem> diffCallback,
                               MBEntityType entity) {
        super(diffCallback);
        this.entity = entity;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(getItem(position));
        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils
                    .loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void resetAnimation() {
        lastPosition = -1;
    }

    private void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), entity.typeActivityClass);
        intent.putExtra(Constants.MBID, getItem(position).getMBID());
        view.getContext().startActivity(intent);
    }
}
