package org.metabrainz.mobile;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.presentation.features.artist.ArtistViewModel;

import static org.junit.Assert.fail;
import static org.metabrainz.mobile.AssertionUtils.checkArtistAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkWikiAssertions;
import static org.metabrainz.mobile.EntityUtils.getTestArtist;
import static org.metabrainz.mobile.EntityUtils.getTestArtistWiki;
import static org.metabrainz.mobile.LiveDataTestUtil.getOrAwaitValue;

public class LookupViewModelTest {

    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

    @Test
    public void testArtistViewModel() {
        Artist testArtist = getTestArtist();
        WikiSummary testWiki = getTestArtistWiki();
        ArtistViewModel viewModel = new ArtistViewModel(new MockLookupRepository());
        viewModel.setMBID(EntityUtils.getTestArtistMBID());
        try {
            checkArtistAssertions(testArtist, getOrAwaitValue(viewModel.getData()));
            checkWikiAssertions(testWiki, getOrAwaitValue(viewModel.getWikiData()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
