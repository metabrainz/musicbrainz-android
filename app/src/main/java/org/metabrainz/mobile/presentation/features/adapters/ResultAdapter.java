package org.metabrainz.mobile.presentation.features.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
<<<<<<< HEAD
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {
    private final List<ResultItem> data;
    private final MBEntityType entity;
    private int lastPosition = -1;

    public ResultAdapter(List<ResultItem> data, MBEntityType entity) {
=======
import org.metabrainz.mobile.presentation.features.label.LabelActivity;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter {
    protected List<ResultItem> data;
    protected String entity;
    private int lastPosition = -1;

    public ResultAdapter(List<ResultItem> data, String entity) {
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
        this.data = data;
        this.entity = entity;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
<<<<<<< HEAD
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(data.get(position));
        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    private void setAnimation(View viewToAnimate, int position) {
=======
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResultViewHolder viewHolder = (ResultViewHolder) holder;
        viewHolder.bind(data.get(position));
        setAnimation(viewHolder.itemView, position);
        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    void setAnimation(View viewToAnimate, int position) {
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
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

<<<<<<< HEAD
    private void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), entity.typeActivityClass);
        intent.putExtra(Constants.MBID, data.get(position).getMBID());
        view.getContext().startActivity(intent);
    }

=======
    void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), LabelActivity.class);
        intent.putExtra(entity, data.get(position).getMBID());
        view.getContext().startActivity(intent);
    }
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
}
