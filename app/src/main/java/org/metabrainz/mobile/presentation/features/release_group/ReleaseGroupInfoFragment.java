package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.repository.ReleaseGroupLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.ArtistWikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.Objects;

public class ReleaseGroupInfoFragment extends Fragment {

    private ReleaseGroupViewModel releaseGroupViewModel;
    private TextView releaseGroupTitle, releaseGroupArtist, wikiTextView;
    private View wikiCard;

    public static ReleaseGroupInfoFragment newInstance() {
        return new ReleaseGroupInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.card_release_group_info, container, false);
        releaseGroupViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.initializeReleaseGroupData().observe(getViewLifecycleOwner(), this::setReleaseGroupInfo);
        releaseGroupViewModel.initializeWikiData().observe(getViewLifecycleOwner(), this::setWiki);
        findViews(layout);
        return layout;
    }

    private void findViews(View layout) {
        releaseGroupTitle = layout.findViewById(R.id.release_group_title);
        releaseGroupArtist = layout.findViewById(R.id.release_group_artist);
        wikiCard = layout.findViewById(R.id.cardView);
        wikiTextView = layout.findViewById(R.id.wiki_summary);
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

    private void setReleaseGroupInfo(ReleaseGroup releaseGroup) {
        String title, artist;
        title = releaseGroup.getTitle();
        artist = releaseGroup.getDisplayArtist();
        releaseGroupTitle.setText(title);
        if (artist != null && !artist.isEmpty())
            releaseGroupArtist.setText("( ".concat(artist).concat(" )"));
        else
            releaseGroupArtist.setVisibility(View.GONE);
        getWikiSummary(releaseGroup);
    }

    private void getWikiSummary(ReleaseGroup releaseGroup) {
        String title = "";
        int method = -1;
        if (releaseGroup != null) {
            for (Link link : releaseGroup.getRelations()) {
                if (link.getType().equals("wikipedia")) {
                    title = link.getPageTitle();
                    method = ReleaseGroupLookupRepository.METHOD_WIKIPEDIA_URL;
                    break;
                }
                if (link.getType().equals("wikidata")) {
                    title = link.getPageTitle();
                    method = ReleaseGroupLookupRepository.METHOD_WIKIDATA_ID;
                    break;
                }
            }
        }
        if (method != -1)
            releaseGroupViewModel.getWikiSummary(title, method);
        else hideWikiCard();

    }
}
