package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.databinding.CardReleaseGroupInfoBinding;

public class ReleaseGroupInfoFragment extends Fragment {

    private CardReleaseGroupInfoBinding binding;
    private ReleaseGroupViewModel releaseGroupViewModel;

    public static ReleaseGroupInfoFragment newInstance() {
        return new ReleaseGroupInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CardReleaseGroupInfoBinding.inflate(inflater, container, false);

        releaseGroupViewModel = new ViewModelProvider(requireActivity()).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.getData().observe(getViewLifecycleOwner(), this::setReleaseGroupInfo);
        releaseGroupViewModel.getWikiData().observe(getViewLifecycleOwner(), this::setWiki);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setWiki(WikiSummary wiki) {
        if (wiki != null) {
            String wikiText = wiki.getExtract();
            if (wikiText != null && !wikiText.isEmpty()) {
                showWikiCard();
                binding.wikiSummary.setText(wikiText);
            } else hideWikiCard();
        } else hideWikiCard();
    }

    private void showWikiCard() {
        binding.cardView.setVisibility(View.VISIBLE);
    }

    private void hideWikiCard() {
        binding.cardView.setVisibility(View.GONE);
    }

    private void setReleaseGroupInfo(ReleaseGroup releaseGroup) {
        if (releaseGroup != null) {
            String title, artist;
            title = releaseGroup.getTitle();
            artist = EntityUtils.getDisplayArtist(releaseGroup.getArtistCredits());
            binding.releaseGroupTitle.setText(title);
            if (artist != null && !artist.isEmpty())
                binding.releaseGroupArtist.setText("( ".concat(artist).concat(" )"));
            else
                binding.releaseGroupArtist.setVisibility(View.GONE);
        }
    }
}
