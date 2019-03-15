package org.metabrainz.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Link;
import org.metabrainz.mobile.repository.LookupRepository;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ArtistBioFragment extends Fragment {

    private ArtistViewModel artistViewModel;

    private TextView wikiTextView;
    private TextView artistType, artistGender, artistArea, artistLifeSpan;
    private View wikiCard;
    private Artist artist;

    public static ArtistBioFragment newInstance() {
        return new ArtistBioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        findViews(layout);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artistViewModel = ViewModelProviders.of(getActivity()).get(ArtistViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: Observe artistData LiveData, instead of requesting the artist sync
        artist = artistViewModel.getArtist();
        setArtistInfo();
        getArtistWiki();
    }

    private void findViews(View layout) {
        artistType = layout.findViewById(R.id.artist_type);
        artistGender = layout.findViewById(R.id.artist_gender);
        artistArea = layout.findViewById(R.id.artist_area);
        artistLifeSpan = layout.findViewById(R.id.life_span);
        wikiCard = layout.findViewById(R.id.card_artist_wiki);
        wikiTextView = layout.findViewById(R.id.wiki_summary);
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
            if(wikiText != null && !wikiText.isEmpty()) {
                showWikiCard();
                wikiTextView.setText(wikiText);
            }
            else hideWikiCard();
        }else hideWikiCard();
    }

    private void showWikiCard(){
        wikiCard.setVisibility(View.VISIBLE);
    }
    private void hideWikiCard(){
        wikiCard.setVisibility(View.GONE);
    }

    private void setArtistInfo(){
        String type,gender,area,lifeSpan;

        if(artist != null) {
            type = artist.getType();
            gender = artist.getGender();

            if(artist.getArea() != null) area = artist.getArea().getName(); else area = "";

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
