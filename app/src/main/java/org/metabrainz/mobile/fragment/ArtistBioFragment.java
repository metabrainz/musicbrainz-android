package org.metabrainz.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Link;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.repository.LookupRepository;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

public class ArtistBioFragment extends Fragment {

    private ArtistViewModel artistViewModel;

    private TextView wikiTextView;
    private TextView artistType, artistGender, artistArea, artistLifeSpan;
    private View wikiCard;
    private Artist artist;

    public static ArtistBioFragment newInstance(Artist artist) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentFactory.Extra.ARTIST, artist);
        ArtistBioFragment fragment = new ArtistBioFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        findViews(layout);

        artistViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);
        wikiTextView = layout.findViewById(R.id.wiki_summary);

        artist = (Artist) getArguments().getSerializable(IntentFactory.Extra.ARTIST);
        setArtistInfo();
        getArtistWiki();
        return layout;
    }

    private void findViews(View layout) {
        artistType = layout.findViewById(R.id.artist_type);
        artistGender = layout.findViewById(R.id.artist_gender);
        artistArea = layout.findViewById(R.id.artist_area);
        artistLifeSpan = layout.findViewById(R.id.life_span);
        wikiCard = layout.findViewById(R.id.card_artist_wiki);
    }

    private void getArtistWiki(){
        String title = "";
        int method = -1;
        for(Link link: artist.getRelations()){
            if(link.getType().equals("wikipedia")) {
                title = link.getPageTitle();
                method = LookupRepository.METHOD_WIKIPEDIA_URL;
                break;
            }
            if (link.getType().equals("wikidata")){
                title = link.getPageTitle();
                method = LookupRepository.METHOD_WIKIDATA_ID;
                break;
            }
        }
        if (method != -1)
            artistViewModel.getArtistWiki(title, method)
                    .observe(this, this::setWiki );
        else hideWikiCard();

    }

    private void setWiki(ArtistWikiSummary wiki){
        if (wiki != null){
            String wikiText = wiki.getExtract();
            if(wikiText != null && !wikiText.isEmpty())
                wikiTextView.setText(wikiText);
            else hideWikiCard();
        }else hideWikiCard();
    }

    private void hideWikiCard(){
        wikiCard.setVisibility(View.GONE);
    }

    private void setArtistInfo(){
        String type,gender,area,lifeSpan;

        if(artist != null) {
            type = artist.getType();
            gender = artist.getGender();
            area = artist.getArea().getName();
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
    }
}
