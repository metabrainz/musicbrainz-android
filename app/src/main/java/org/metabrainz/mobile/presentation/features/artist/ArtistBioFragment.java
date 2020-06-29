package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.databinding.FragmentBioBinding;

public class ArtistBioFragment extends Fragment {

    private FragmentBioBinding binding;
    private ArtistViewModel artistViewModel;

    public static ArtistBioFragment newInstance() {
        return new ArtistBioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBioBinding.inflate(inflater, container, false);
        artistViewModel = new ViewModelProvider(requireActivity()).get(ArtistViewModel.class);
        artistViewModel.getData().observe(getViewLifecycleOwner(), this::setArtistInfo);
        artistViewModel.getWikiData().observe(getViewLifecycleOwner(), this::setWiki);
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
                binding.cardArtistWiki.wikiSummary.setText(wikiText);
            } else hideWikiCard();
        } else hideWikiCard();
    }

    private void showWikiCard() {
        binding.cardArtistWiki.getRoot().setVisibility(View.VISIBLE);
    }

    private void hideWikiCard() {
        binding.cardArtistWiki.getRoot().setVisibility(View.GONE);
    }

    private void setArtistInfo(Artist artist) {
        if (artist != null) {
            String type, gender, area, lifeSpan;

            type = artist.getType();
            gender = artist.getGender();

            if (artist.getArea() != null) area = artist.getArea().getName();
            else area = "";

            if (artist.getLifeSpan() != null)
                lifeSpan = artist.getLifeSpan().getTimePeriod();
            else lifeSpan = "";

            if (type != null && !type.isEmpty())
                binding.cardArtistInfo.artistType.setText(type);
            if (gender != null && !gender.isEmpty())
                binding.cardArtistInfo.artistGender.setText(gender);
            if (area != null && !area.isEmpty())
                binding.cardArtistInfo.artistArea.setText(area);
            if (lifeSpan != null && !lifeSpan.isEmpty())
                binding.cardArtistInfo.lifeSpan.setText(lifeSpan);
        }
    }
}
