package org.metabrainz.mobile.presentation.features.release_group;

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
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.databinding.CardReleaseGroupInfoBinding;

public class ReleaseGroupInfoFragment extends Fragment {

    private CardReleaseGroupInfoBinding binding;
    private ReleaseGroupViewModel releaseGroupViewModel;

    public static ReleaseGroupInfoFragment newInstance() {
        return new ReleaseGroupInfoFragment();
    }

    @Override
<<<<<<< HEAD
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CardReleaseGroupInfoBinding.inflate(inflater, container, false);

        releaseGroupViewModel = new ViewModelProvider(requireActivity()).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.getData().observe(getViewLifecycleOwner(), this::setReleaseGroupInfo);
        releaseGroupViewModel.getWikiData().observe(getViewLifecycleOwner(), this::setWiki);

        return binding.getRoot();
=======
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.card_release_group_info, container, false);
        releaseGroupViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.initializeData().observe(getViewLifecycleOwner(), this::setReleaseGroupInfo);
        releaseGroupViewModel.initializeWikiData().observe(getViewLifecycleOwner(), this::setWiki);
        findViews(layout);
        return layout;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
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
<<<<<<< HEAD
            String title, artist;
            title = releaseGroup.getTitle();
            artist = releaseGroup.getDisplayArtist();
            binding.releaseGroupTitle.setText(title);
            if (artist != null && !artist.isEmpty())
                binding.releaseGroupArtist.setText("( ".concat(artist).concat(" )"));
            else
                binding.releaseGroupArtist.setVisibility(View.GONE);
=======
            for (Link link : releaseGroup.getRelations()) {
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
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
        }
    }
}
