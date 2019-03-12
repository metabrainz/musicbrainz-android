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
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.repository.LookupRepository;

import java.util.List;

import static android.view.View.GONE;

public class ArtistReleaseAdapter extends RecyclerView.Adapter {

    private List<Release> data;
    private LookupRepository repository = LookupRepository.getRepository();
    public ArtistReleaseAdapter(List<Release> data){this.data = data;}

    private class ReleaseItemViewHolder extends RecyclerView.ViewHolder{
        private MutableLiveData<List<Release>> coverArtMutableLiveData;
        TextView releaseName, releaseDisambiguation;
        ImageView coverArtView;

        public ReleaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseDisambiguation = itemView.findViewById(R.id.release_disambiguation);
            coverArtView = itemView.findViewById(R.id.cover_art);
        }

        public void bind(Release release, int position){
            releaseName.setText(release.getTitle());
            setViewVisibility(release.getDisambiguation(), releaseDisambiguation);
            coverArtView.setVisibility(View.GONE);

            if(release.getCoverArt() != null) setCoverArtView(release);
            else fetchSetCoverArtView(position);
        }

        private void setCoverArtView(Release release){
            boolean flag = false;
            if(release != null && release.getCoverArt() != null){
                String url = release.getCoverArt().getImages().get(0).getImage();
                if (url != null && !url.isEmpty()) {
                    coverArtView.setVisibility(View.VISIBLE);
                    Picasso.get().load(Uri.parse(url)).into(coverArtView);
                    flag = true;
                }
            }
            if(!flag) coverArtView.setVisibility(View.GONE);
        }
        private void fetchSetCoverArtView(int position){
            coverArtMutableLiveData = repository.fetchCoverArt(data,position);
            coverArtMutableLiveData.observeForever(releases -> {
                if (releases.get(position) != null)
                    setCoverArtView(releases.get(position));
                else coverArtView.setVisibility(View.GONE);
            });
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
        viewHolder.bind(data.get(position) , position);
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
