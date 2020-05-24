package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

<<<<<<< HEAD
=======
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.Link;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
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
<<<<<<< HEAD
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
=======
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        artistViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ArtistViewModel.class);
        artistViewModel.initializeData().observe(getViewLifecycleOwner(), this::setArtistInfo);
        artistViewModel.initializeWikiData().observe(getViewLifecycleOwner(), this::setWiki);
        findViews(layout);
        return layout;
    }

    private void findViews(View layout) {
        artistType = layout.findViewById(R.id.artist_type);
        artistGender = layout.findViewById(R.id.artist_gender);
        artistArea = layout.findViewById(R.id.artist_area);
        artistLifeSpan = layout.findViewById(R.id.life_span);
        wikiCard = layout.findViewById(R.id.card_artist_wiki);
        wikiTextView = layout.findViewById(R.id.wiki_summary);
    }

    private void getArtistWiki(Artist artist) {
        String title = "";
        int method = -1;
        if (artist != null) {
            for (Link link : artist.getRelations()) {
                if (link.getType().equals("wikipedia")) {
                    title = link.getPageTitle();
                    method = LookupRepository.METHOD_WIKIPEDIA_URL;
                    break;
                }
                if (link.getType().equals("wikidata")) {
                    title = link.getPageTitle();
                    method = LookupRepository.METHOD_WIKIDATA_ID;
                    break;
                }
            }
        }
        if (method != -1)
            artistViewModel.loadArtistWiki(title, method);
        else hideWikiCard();

>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
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
        binding.cardArtistWiki.cardView.setVisibility(View.VISIBLE);
    }

    private void hideWikiCard() {
        binding.cardArtistWiki.cardView.setVisibility(View.GONE);
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
