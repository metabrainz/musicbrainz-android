package org.metabrainz.android.data.sources.api.entities.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.data.sources.api.entities.*
import java.util.*

class Release : MBEntity() {
    var title: String? = null

    @SerializedName("artist-credit")
    var artistCredits: MutableList<ArtistCredit> = ArrayList()
    var date: String? = null
    var barcode: String? = null

    //    public String getPackaging() {
    //        return packaging;
    //    }
    //
    //    public void setPackaging(String packaging) {
    //        this.packaging = packaging;
    //    }
    // FIXME: Temporary fix due to mismatch with API response until issue is resolved
    //    private String packaging;
    @SerializedName("release-group")
    var releaseGroup: ReleaseGroup? = null

    @SerializedName("release-events")
    private val releaseEvents: MutableList<ReleaseEvent> = ArrayList()

    @SerializedName("label-info")
    private val labels: MutableList<LabelInfo> = ArrayList()

    @SerializedName("track-count")
    var trackCount = 0
    var country: String? = null
    var status: String? = null
    var media: MutableList<Media>? = ArrayList()
    var coverArt: CoverArt? = null

    @SerializedName("text-representation")
    var textRepresentation: TextRepresentation? = null
    @JvmField
    val relations: MutableList<Link> = ArrayList()
    fun getRelations(): List<Link> {
        return relations
    }

    fun setRelations(relations: List<Link>?) {
        this.relations.addAll(relations!!)
    }

    @JvmName("getArtistCredits1")
    fun getArtistCredits(): MutableList<ArtistCredit> {
        return artistCredits
    }

    @JvmName("setArtistCredits1")
    fun setArtistCredits(artistCredits: List<ArtistCredit>?) {
        this.artistCredits.addAll(artistCredits!!)
    }

    fun getReleaseEvents(): List<ReleaseEvent> {
        return releaseEvents
    }

    fun setReleaseEvents(releaseEvents: List<ReleaseEvent>?) {
        this.releaseEvents.addAll(releaseEvents!!)
    }

    fun getLabels(): List<LabelInfo> {
        return labels
    }

    fun setLabels(labels: List<LabelInfo>?) {
        this.labels.addAll(labels!!)
    }

    @JvmName("getTrackCount1")
    fun getTrackCount(): Int {
        if (trackCount == 0 && media != null && media!!.size != 0) {
            var count = 0
            for (medium in media!!) count += medium.trackCount
            return count
        }
        return trackCount
    }

    @JvmName("setTrackCount1")
    fun setTrackCount(trackCount: Int) {
        this.trackCount = trackCount
    }

    @JvmName("getMedia1")
    fun getMedia(): List<Media>? {
        return media
    }

    @JvmName("setMedia1")
    fun setMedia(media: List<Media>?) {
        this.media!!.addAll(media!!)
    }

    //TODO: Implement Text Representation
    fun labelCatalog(): String {
        val itr: Iterator<LabelInfo> = labels.iterator()
        val builder = StringBuilder()
        while (itr.hasNext()) {
            val labelInfo = itr.next()
            val catalogNumber = labelInfo.catalogNumber
            val label = labelInfo.label
            if (label != null) {
                if (catalogNumber != null && catalogNumber.isNotEmpty()) {
                    builder.append(catalogNumber).append(" (")
                    builder.append(label.name)
                    builder.append(")")
                } else builder.append(label.name)
                if (itr.hasNext()) builder.append(" , ")
            }
        }
        return builder.toString()
    }

    override fun toString(): String {
        return "Release{" +
                "title='" + title + '\'' +
                ", artistCredits=" + artistCredits +
                ", date='" + date + '\'' +
                ", barcode='" + barcode + '\'' +  //                ", packaging='" + packaging + '\'' +
                ", releaseGroup=" + releaseGroup +
                ", releaseEvents=" + releaseEvents +
                ", labels=" + labels +
                ", trackCount=" + trackCount +
                ", country='" + country + '\'' +
                ", status='" + status + '\'' +
                ", media=" + media +
                ", coverArt=" + coverArt +
                ", textRepresentation=" + textRepresentation +
                ", relations=" + relations +
                ", mbid='" + mbid + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}'
    }
}