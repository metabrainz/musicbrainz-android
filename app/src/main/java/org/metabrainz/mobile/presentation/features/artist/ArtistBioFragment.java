package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.repository.ArtistLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.ArtistWikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;

public class ArtistBioFragment extends Fragment {

    private ArtistViewModel artistViewModel;

    private TextView wikiTextView;
    private TextView artistType, artistGender, artistArea, artistLifeSpan;
    private View wikiCard;

    public static ArtistBioFragment newInstance() {
        return new ArtistBioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        artistViewModel = ViewModelProviders.of(getActivity()).get(ArtistViewModel.class);
        artistViewModel.initializeArtistData().observe(getViewLifecycleOwner(), this::setArtistInfo);
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
                    method = ArtistLookupRepository.METHOD_WIKIPEDIA_URL;
                    break;
                }
                if (link.getType().equals("wikidata")) {
                    title = link.getPageTitle();
                    method = ArtistLookupRepository.METHOD_WIKIDATA_ID;
                    break;
                }
            }
        }
        if (method != -1)
            artistViewModel.loadArtistWiki(title, method);
        else hideWikiCard();

    }

    private void setWiki(ArtistWikiSummary wiki) {
        if (wiki != null) {
            String wikiText = wiki.getExtract();
            if (wikiText != null && !wikiText.isEmpty()) {
                showWikiCard();
                wikiTextView.setText(wikiText);
            } else hideWikiCard();
        } else hideWikiCard();
    }

    private void showWikiCard() {
        wikiCard.setVisibility(View.VISIBLE);
    }

    private void hideWikiCard() {
        wikiCard.setVisibility(View.GONE);
    }

    private void setArtistInfo(Artist artist) {
        String type, gender, area, lifeSpan;

        if (artist != null) {
            type = artist.getType();
            gender = artist.getGender();

            if (artist.getArea() != null) area = artist.getArea().getName();
            else area = "";

            if (artist.getLifeSpan() != null)
                lifeSpan = artist.getLifeSpan().getTimePeriod();
            else lifeSpan = "";

            if (type != null && !type.isEmpty())
                artistType.setText(type);
            if (gender != null && !gender.isEmpty())
                artistGender.setText(gender);
            if (area != null && !area.isEmpty())
                artistArea.setText(area);
            if (lifeSpan != null && !lifeSpan.isEmpty())
                artistLifeSpan.setText(lifeSpan);
        }

        getArtistWiki(artist);
    }
}
