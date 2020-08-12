package org.metabrainz.mobile;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.artist.ArtistViewModel;
import org.metabrainz.mobile.presentation.features.label.LabelViewModel;
import org.metabrainz.mobile.presentation.features.recording.RecordingViewModel;
import org.metabrainz.mobile.presentation.features.release.ReleaseViewModel;
import org.metabrainz.mobile.presentation.features.release_group.ReleaseGroupViewModel;
import org.metabrainz.mobile.util.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.metabrainz.mobile.AssertionUtils.checkArtistAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkLabelAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkRecordingAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseGroupAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkWikiAssertions;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtist;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtistMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtistWiki;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabel;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabelMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecording;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecordingMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestRelease;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroup;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroupMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseMBID;
import static org.metabrainz.mobile.LiveDataTestUtil.getOrAwaitValue;

public class LookupViewModelTest {

    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

    @Test
    public void testArtistViewModel() {
        Artist testArtist = getTestArtist();
        WikiSummary testWiki = getTestArtistWiki();
        ArtistViewModel viewModel = new ArtistViewModel(new MockLookupRepository());
        viewModel.setMBID(getTestArtistMBID());
        try {
            Resource<Artist> resource = getOrAwaitValue(viewModel.getData());
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            checkArtistAssertions(testArtist, resource.getData());

            Resource<WikiSummary> wikiResource = getOrAwaitValue(viewModel.getWikiData());
            assertEquals(Resource.Status.SUCCESS, wikiResource.getStatus());
            checkWikiAssertions(testWiki, wikiResource.getData());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReleaseViewModel() {
        Release testRelease = getTestRelease();
        ReleaseViewModel viewModel = new ReleaseViewModel(new MockLookupRepository());
        viewModel.setMBID(getTestReleaseMBID());
        try {
            Resource<Release> resource = getOrAwaitValue(viewModel.getData());
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            checkReleaseAssertions(testRelease, resource.getData());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReleaseGroupViewModel() {
        ReleaseGroup testReleaseGroup = getTestReleaseGroup();
        ReleaseGroupViewModel viewModel = new ReleaseGroupViewModel(new MockLookupRepository());
        viewModel.setMBID(getTestReleaseGroupMBID());
        try {
            Resource<ReleaseGroup> resource = getOrAwaitValue(viewModel.getData());
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            checkReleaseGroupAssertions(testReleaseGroup, resource.getData());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLabelViewModel() {
        Label testLabel = getTestLabel();
        LabelViewModel viewModel = new LabelViewModel(new MockLookupRepository());
        viewModel.setMBID(getTestLabelMBID());
        try {
            Resource<Label> resource = getOrAwaitValue(viewModel.getData());
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            checkLabelAssertions(testLabel, resource.getData());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRecordingViewModel() {
        Recording testRecording = getTestRecording();
        RecordingViewModel viewModel = new RecordingViewModel(new MockLookupRepository());
        viewModel.setMBID(getTestRecordingMBID());
        try {
            Resource<Recording> resource = getOrAwaitValue(viewModel.getData());
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            checkRecordingAssertions(testRecording, resource.getData());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
