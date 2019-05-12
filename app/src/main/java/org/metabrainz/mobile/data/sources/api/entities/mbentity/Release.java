package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.LabelInfo;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.Media;
import org.metabrainz.mobile.data.sources.api.entities.ReleaseEvent;
import org.metabrainz.mobile.data.sources.api.entities.TextRepresentation;

import java.util.ArrayList;
import java.util.Iterator;


public class Release {

    @SerializedName("id")
    private String mbid;
    private String title;
    @SerializedName("artist-credit")
    private ArrayList<ArtistCredit> artistCredits = new ArrayList<>();
    private String date;
    private String barcode;
    private String packaging;
    @SerializedName("release-group")
    private ReleaseGroup releaseGroup;
    @SerializedName("release-events")
    private ArrayList<ReleaseEvent> releaseEvents = new ArrayList<>();
    @SerializedName("label-info")
    private ArrayList<LabelInfo> labels = new ArrayList<>();
    @SerializedName("track-count")
    private int trackCount;
    private String country;
    private String status;
    private String disambiguation;
    private ArrayList<Media> media = new ArrayList<>();
    private CoverArt coverArt;
    @SerializedName("text-representation")
    private TextRepresentation textRepresentation;

    private ArrayList<Link> relations = new ArrayList<>();

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public TextRepresentation getTextRepresentation() {
        return textRepresentation;
    }

    public void setTextRepresentation(TextRepresentation textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    public ArrayList<Link> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<Link> relations) {
        this.relations = relations;
    }

    public CoverArt getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(CoverArt coverArt) {
        this.coverArt = coverArt;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ArtistCredit> getArtistCredits() {
        return artistCredits;
    }

    public void setArtistCredits(ArrayList<ArtistCredit> artistCredits) {
        this.artistCredits = artistCredits;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public ReleaseGroup getReleaseGroup() {
        return releaseGroup;
    }

    public void setReleaseGroup(ReleaseGroup releaseGroup) {
        this.releaseGroup = releaseGroup;
    }

    public ArrayList<ReleaseEvent> getReleaseEvents() {
        return releaseEvents;
    }

    public void setReleaseEvents(ArrayList<ReleaseEvent> releaseEvents) {
        this.releaseEvents = releaseEvents;
    }

    public ArrayList<LabelInfo> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<LabelInfo> labels) {
        this.labels = labels;
    }

    public int getTrackCount() {
        if (trackCount == 0 && media != null && media.size() != 0) {
            int count = 0;
            for (Media medium : media) count += medium.getTrackCount();
            return count;
        }
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String countryCode) {
        this.country = countryCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    //TODO: Implement Text Representation
    public String getDisplayArtist() {
        StringBuilder builder = new StringBuilder();
        Iterator<ArtistCredit> iterator = artistCredits.iterator();
        while (iterator.hasNext()) {
            ArtistCredit credit = iterator.next();
            builder.append(credit.getArtist().getName());
            if (iterator.hasNext())
                builder.append(credit.getJoinphrase());
        }
        return builder.toString();
    }

    public String labelCatalog() {
        Iterator<LabelInfo> itr = labels.iterator();
        StringBuilder builder = new StringBuilder();
        while (itr.hasNext()) {
            LabelInfo labelInfo = itr.next();
            String catalogNumber = labelInfo.getCatalogNumber();
            Label label = labelInfo.getLabel();
            if (label != null) {
                if (catalogNumber != null && !catalogNumber.isEmpty()) {
                    builder.append(catalogNumber).append(" (");
                    builder.append(label.getName());
                    builder.append(")");
                } else builder.append(label.getName());
                if (itr.hasNext()) builder.append(" , ");
            }
        }
        return builder.toString();
    }

}
