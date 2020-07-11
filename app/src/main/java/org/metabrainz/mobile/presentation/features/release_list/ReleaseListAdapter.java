package org.metabrainz.mobile.presentation.features.release_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.CardReleaseItemBinding;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;

public class ReleaseListAdapter extends RecyclerView.Adapter<ReleaseListAdapter.ReleaseItemViewHolder> {

    private final List<Release> releaseList;
    private final ReleaseListViewModel viewModel;
    private final CompositeDisposable compositeDisposable;

    public ReleaseListAdapter(Context context, List<Release> releaseList) {
        this.releaseList = releaseList;
        // Load the ViewModel to fetch cover art for each release item
        viewModel = new ViewModelProvider((FragmentActivity) context).get(ReleaseListViewModel.class);
        compositeDisposable = new CompositeDisposable();
    }

    @NonNull
    @Override
    public ReleaseListAdapter.ReleaseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ReleaseItemViewHolder(CardReleaseItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReleaseListAdapter.ReleaseItemViewHolder holder, int position) {
        if (holder.disposable != null && !holder.disposable.isDisposed())
            compositeDisposable.remove(holder.disposable);

        holder.bind(releaseList.get(position));
    }

    @Override
    public int getItemCount() {
        return releaseList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

    class ReleaseItemViewHolder extends RecyclerView.ViewHolder {
        CardReleaseItemBinding binding;
        Disposable disposable;

        ReleaseItemViewHolder(@NonNull CardReleaseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Release release) {
            binding.releaseName.setText(release.getTitle());
            setViewVisibility(release.getDisambiguation(), binding.releaseDisambiguation);

            binding.releaseCoverArt.setImageDrawable(binding.getRoot().getContext()
                    .getResources()
                    .getDrawable(R.drawable.link_discog));

            if (release.getCoverArt() != null)
                setCoverArtView(release);
            else
                fetchCoverArtForRelease(release);

            this.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ReleaseActivity.class);
                intent.putExtra(Constants.MBID, release.getMbid());
                v.getContext().startActivity(intent);
            });
        }

        private void setCoverArtView(Release release) {
            if (release != null && release.getCoverArt() != null && releaseList.contains(release)) {
                // TODO: Search for the first “FRONT” image to use it as cover
                String url = release.getCoverArt()
                        .getImages()
                        .get(0)
                        .getThumbnails()
                        .getSmall();

                if (url != null && !url.isEmpty()) {
                    Picasso.get()
                            .load(Uri.parse(url))
                            .placeholder(R.drawable.link_discog)
                            .into(binding.releaseCoverArt);
                }
            }
        }

        private void addCoverArt(CoverArt coverArt) {
            if (coverArt != null && coverArt.getImages() != null
                    && !coverArt.getImages().isEmpty()) {
                String coverArtRelease = coverArt.getRelease();
                for (Release release : releaseList) {
                    if (coverArtRelease.endsWith(release.getMbid())) {
                        release.setCoverArt(coverArt);
                        setCoverArtView(release);
                        break;
                    }
                }
            }
        }

        private void fetchCoverArtForRelease(Release release) {
            // Ask the viewModel to retrieve the cover art
            // and append it to this release
            disposable = viewModel
                    .fetchCoverArtForRelease(release)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addCoverArt, Throwable::printStackTrace);
            compositeDisposable.add(disposable);
        }
    }
}
