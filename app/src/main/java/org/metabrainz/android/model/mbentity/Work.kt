package org.metabrainz.android.model.mbentity

internal class Work : org.metabrainz.android.model.mbentity.MBEntity() {
    //TODO: Implement Relations
    var title: String? = null
    override fun toString(): String {
        return "Work{" +
                "title='" + title + '\'' +
                ", mbid='" + mbid + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}'
    }
}