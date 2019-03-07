package org.metabrainz.mobile.adapter.list;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.search.CoverArt;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.repository.LookupRepository;

import java.util.List;

import static android.view.View.GONE;

public class ArtistReleaseAdapter extends RecyclerView.Adapter {

    private List<Release> data;
    private LookupRepository repository = LookupRepository.getRepository();
    public ArtistReleaseAdapter(List<Release> data){this.data = data;}

    private class ReleaseItemViewHolder extends RecyclerView.ViewHolder{
        private MutableLiveData<CoverArt> coverArtMutableLiveData;
        TextView releaseName, releaseDisambiguation, releaseArtist;
        ImageView coverArtView;
        public ReleaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseArtist = itemView.findViewById(R.id.release_artist);
            releaseDisambiguation = itemView.findViewById(R.id.release_disambiguation);
            coverArtView = itemView.findViewById(R.id.cover_art);
        }

        public void bind(Release release){
            coverArtMutableLiveData = repository.fetchCoverArt(release);
            coverArtMutableLiveData.observeForever(coverArt -> {
                boolean flag = false;
                if(coverArt != null && coverArt.getImages() != null){
                    String url = coverArt.getImages().get(0).getImage();
                    if (url != null && !url.isEmpty()) {
                        coverArtView.setVisibility(View.VISIBLE);
                        Picasso.get().load(Uri.parse(url)).into(coverArtView);
                        flag = true;
                    }
                }
                if(!flag) coverArtView.setVisibility(View.GONE);
            });
            releaseName.setText(release.getTitle());
            setViewVisibility(release.getDisplayArtist(), releaseArtist);
            setViewVisibility(release.getDisambiguation(), releaseDisambiguation);
            coverArtView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_release,parent,false);
        return new ReleaseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReleaseItemViewHolder viewHolder = (ReleaseItemViewHolder) holder;
        viewHolder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(GONE);
    }
}
