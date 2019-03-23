package org.metabrainz.mobile.adapter.list;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.search.CoverArt;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;

public class ArtistReleaseAdapter extends RecyclerView.Adapter {

    private List<Release> releaseList;
    private ArtistViewModel artistViewModel;
    private CompositeDisposable compositeDisposable;

    public ArtistReleaseAdapter(Context context, List<Release> releaseList){
        this.releaseList = releaseList;
        // Load the ViewModel to fetch cover art for each release item
        artistViewModel = ViewModelProviders.of((FragmentActivity) context).get(ArtistViewModel.class);
        compositeDisposable = new CompositeDisposable();
    }

    private class ReleaseItemViewHolder extends RecyclerView.ViewHolder{
        TextView releaseName, releaseDisambiguation;
        ImageView coverArtView;
        Disposable disposable;

        public ReleaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseDisambiguation = itemView.findViewById(R.id.release_disambiguation);
            coverArtView = itemView.findViewById(R.id.cover_art);
        }

        public void bind(Release release, int position){
            releaseName.setText(release.getTitle());
            setViewVisibility(release.getDisambiguation(), releaseDisambiguation);

            if(release.getCoverArt() != null) setCoverArtView(release);
            else fetchCoverArtForRelease(position);
        }

        private void setCoverArtView(Release release){
            if (release != null && release.getCoverArt() != null && releaseList.contains(release)){
                // TODO: Search for the first “FRONT” image to use it as cover
                String url = release.getCoverArt()
                        .getImages()
                        .get(0)
                        .getImage();

                if (url != null && !url.isEmpty()) {
                    Picasso.get()
                            .load(Uri.parse(url))
                            .placeholder(R.drawable.link_discog)
                            .into(coverArtView);
                }
            }
        }

        private void addCoverArt(CoverArt coverArt){
            if (coverArt != null && coverArt.getImages() != null
                    && !coverArt.getImages().isEmpty()) {
                String coverArtRelease = coverArt.getRelease();
                for (Release release:releaseList){
                    if (coverArtRelease.endsWith(release.getMbid())) {
                        release.setCoverArt(coverArt);
                        setCoverArtView(release);
                        break;
                    }
                }
            }
        }

        private void fetchCoverArtForRelease(int position) {
            // Ask the viewModel to retrieve the cover art
            // and append it to this release
            artistViewModel.fetchCoverArtForRelease(releaseList.get(position));
            disposable = artistViewModel
                            .fetchCoverArtForRelease(releaseList.get(position))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::addCoverArt, Throwable::printStackTrace);
            compositeDisposable.add(disposable);
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
        if (viewHolder.disposable != null && !viewHolder.disposable.isDisposed())
            compositeDisposable.remove(viewHolder.disposable);
        viewHolder.bind(releaseList.get(position) , position);
    }

    @Override
    public int getItemCount() {
        return releaseList.size();
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(GONE);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        compositeDisposable.clear();
    }
}
