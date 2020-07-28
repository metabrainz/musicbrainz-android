package org.metabrainz.mobile;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class EntityTestUtils {

    public static String loadResourceAsString(String resource) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        return builder.toString();
    }

    public static WikiSummary getTestArtistWiki() {
        WikiSummary summary = new WikiSummary();
        summary.setExtract("Edward Christopher Sheeran is an English singer, songwriter, record " +
                "producer, and actor. In early 2011, Sheeran independently released the extended " +
                "play, No. 5 Collaborations Project. After signing with Asylum Records, his debut " +
                "album, +, was released in September 2011 and topped the UK Albums Chart. It " +
                "contained his first hit single \"The A Team\". In 2012, Sheeran won the Brit Awards" +
                " for Best British Male Solo Artist and British Breakthrough Act.");
        return summary;
    }

    public static String getTestArtistMBID() {
        return "b8a7c51f-362c-4dcb-a259-bc6e0095f0a6";
    }

    public static Artist getTestArtist() {
        String MBID = getTestArtistMBID();
        Artist testArtist = new Artist();
        testArtist.setMbid(MBID);
        testArtist.setType("Person");
        testArtist.setCountry("GB");
        testArtist.setDisambiguation("UK singer-songwriter");
        testArtist.setName("Ed Sheeran");
        testArtist.setSortName("Sheeran, Ed");
        testArtist.setGender("Male");

        List<Release> testReleases = new ArrayList<>();
        Release testRelease = new Release();
        testRelease.setMbid("8b6d9a7e-2f94-4cca-b2e3-d35116b1a49c");
        testRelease.setDate("2005");
        testRelease.setDisambiguation("");
        testRelease.setTitle("The Orange Room EP");
        testRelease.setStatus("Official");
        testRelease.setCountry("XW");
        testRelease.setBarcode("8791423234");
        testReleases.add(testRelease);

        testRelease = new Release();
        testRelease.setMbid("71c07e92-4b71-43d3-a041-5fd67d8343af");
        testRelease.setDate("2006-03-22");
        testRelease.setDisambiguation("");
        testRelease.setTitle("Ed Sheeran");
        testRelease.setStatus("Official");
        testRelease.setCountry("GB");
        testRelease.setBarcode("12223213211");
        testReleases.add(testRelease);

        testRelease = new Release();
        testRelease.setMbid("fe2c71cf-52e3-4abd-a0ed-45ab3770d965");
        testRelease.setDate("2006-03-22");
        testRelease.setDisambiguation("");
        testRelease.setTitle("Ed Sheeran (EP)");
        testRelease.setStatus("Official");
        testRelease.setCountry("GB");
        testRelease.setBarcode("1221421");
        testReleases.add(testRelease);

        testRelease = new Release();
        testRelease.setMbid("3c8473d3-202c-44c8-a37b-8e259fe12c39");
        testRelease.setDate("2007-05-01");
        testRelease.setDisambiguation("");
        testRelease.setTitle("Want Some?");
        testRelease.setStatus("Official");
        testRelease.setCountry("GB");
        testRelease.setBarcode("8791423234");
        testReleases.add(testRelease);

        testArtist.setReleases(testReleases);

        return testArtist;
    }

    public static String getTestReleaseMBID() {
        return "94ad3a58-a1cc-46a3-acf4-9cb6c1d6f032";
    }

    public static Release getTestRelease() {
        String MBID = getTestReleaseMBID();
        Release testRelease = new Release();
        testRelease.setMbid(MBID);
        testRelease.setTitle("+");
        testRelease.setBarcode("5052498646524");
        testRelease.setStatus("Official");
        testRelease.setCountry("XE");
        testRelease.setDisambiguation("");
        testRelease.setDate("2011-09-09");

        List<ArtistCredit> testArtistCredits = new ArrayList<>();
        ArtistCredit testArtistCredit = new ArtistCredit();
        testArtistCredit.setJoinphrase("");
        testArtistCredit.setName("Ed Sheeran");
        Artist testArtist = new Artist();
        testArtist.setMbid("b8a7c51f-362c-4dcb-a259-bc6e0095f0a6");
        testArtistCredit.setArtist(testArtist);
        testRelease.setArtistCredits(testArtistCredits);

        return testRelease;
    }

    public static String getTestReleaseGroupMBID() {
        return "05ce100c-eddf-4967-8d7e-33fc0883fe39";
    }

    public static ReleaseGroup getTestReleaseGroup() {
        String MBID = getTestReleaseGroupMBID();
        ReleaseGroup testReleaseGroup = new ReleaseGroup();
        testReleaseGroup.setMbid(MBID);
        testReleaseGroup.setCount(1);
        testReleaseGroup.setTitle("+");
        testReleaseGroup.setPrimaryType("Album");
        testReleaseGroup.setDisambiguation("plus");

        List<ArtistCredit> testArtistCredits = new ArrayList<>();
        ArtistCredit testArtistCredit = new ArtistCredit();
        testArtistCredit.setJoinphrase("");
        testArtistCredit.setName("Ed Sheeran");
        Artist testArtist = new Artist();
        testArtist.setMbid("b8a7c51f-362c-4dcb-a259-bc6e0095f0a6");
        testArtistCredit.setArtist(testArtist);
        testReleaseGroup.setArtistCredits(testArtistCredits);

        return testReleaseGroup;
    }

    public static String getTestLabelMBID() {
        return "015a28ab-1eb8-45a0-b2c6-601e410548af";
    }

    public static Label getTestLabel() {
        String MBID = getTestLabelMBID();
        Label testLabel = new Label();
        testLabel.setMbid(MBID);
        testLabel.setName("Speed Records");
        testLabel.setCode("SPREC");
        testLabel.setCountry("IN");
        testLabel.setDisambiguation("India");
        testLabel.setType("Original Production");
        return testLabel;
    }

    public static String getTestRecordingMBID() {
        return "11f3c37d-839f-4f65-87c1-d55cb416d0c5";
    }

    public static Recording getTestRecording() {
        String MBID = getTestRecordingMBID();
        Recording testRecording = new Recording();
        testRecording.setMbid(MBID);
        testRecording.setLength(125400);
        testRecording.setTitle("Plus Plus");
        testRecording.setDisambiguation("no disambiguation");

        List<ArtistCredit> testArtistCredits = new ArrayList<>();
        ArtistCredit testArtistCredit = new ArtistCredit();
        testArtistCredit.setJoinphrase("");
        testArtistCredit.setName("Like a Tim");
        Artist testArtist = new Artist();
        testArtist.setMbid("90568a1e-ef8d-4d22-93fa-faadc819d576");
        testArtistCredit.setArtist(testArtist);
        testRecording.setArtistCredits(testArtistCredits);

        return testRecording;
    }
}
