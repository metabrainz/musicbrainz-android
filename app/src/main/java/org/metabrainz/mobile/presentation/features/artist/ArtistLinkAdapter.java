package org.metabrainz.mobile.presentation.features.artist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.LinksClassifier;
import org.metabrainz.mobile.data.sources.api.entities.Url;
import org.metabrainz.mobile.databinding.ItemLinkBinding;

import java.util.List;

import retrofit2.http.HEAD;

class ArtistLinkAdapter extends RecyclerView.Adapter<ArtistLinkAdapter.LinkViewHolder> implements View.OnClickListener {
    private final List<Link> links;
    private final Context context;

    ArtistLinkAdapter(Context context, List<Link> links) {
        this.links = links;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new LinkViewHolder(ItemLinkBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistLinkAdapter.LinkViewHolder holder, int position) {
        String type = links.get(position).getType();
        LinkViewHolder linkViewHolder = (LinkViewHolder) holder;

        Drawable drawable = getLinkImage(linkViewHolder.itemView.getContext(), type);
        linkViewHolder.bind(drawable, type);
        linkViewHolder.itemView.setTag(R.id.link_image, links.get(position).getUrl());
        linkViewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    @Override
    public void onClick(View v) {
        Url url = (Url) v.getTag(R.id.link_image);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url.getResource()));
        context.startActivity(intent);
    }

    private Drawable getLinkImage(Context context, String type) {
        Resources resources = context.getResources();
        switch (type) {
            case LinksClassifier.YOUTUBE:
                return resources.getDrawable(R.drawable.youtube_logo);
            case LinksClassifier.BANDCAMP:
                return resources.getDrawable(R.drawable.bandcamp_logo);
            case LinksClassifier.DISCOGS:
                return resources.getDrawable(R.drawable.discogs_logo);
            case LinksClassifier.IMDB:
                return resources.getDrawable(R.drawable.imdb_logo);
            case LinksClassifier.VIAF:
                return resources.getDrawable(R.drawable.viaf_logo);
            case LinksClassifier.MYSPACE:
                return resources.getDrawable(R.drawable.myspace_logo);
            default:
                return resources.getDrawable(R.drawable.link_generic);
        }
    }

    class LinkViewHolder extends RecyclerView.ViewHolder {
        ItemLinkBinding binding;

        LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemLinkBinding.bind(itemView);
        }
    }
}
