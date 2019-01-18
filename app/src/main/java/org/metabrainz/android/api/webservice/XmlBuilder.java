package org.metabrainz.android.api.webservice;

import java.util.Collection;

public class XmlBuilder {

    private static final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://metabrainz.org/ns/mmd-2.0#\">";
    private static final String FOOT = "</metadata>";

    static String buildTagSubmissionXML(Entity entityType, String entityMbid, Collection<String> tags) {
        StringBuilder content = new StringBuilder(HEAD);
        switch (entityType) {
        case ARTIST:
            content.append("<artist-list><artist id=\"" + entityMbid + "\">" + getTagsInXML(tags)
                    + "</artist></artist-list>");
            break;
        case RELEASE_GROUP:
            content.append("<release-group-list><release-group id=\"" + entityMbid + "\">" + getTagsInXML(tags)
                    + "</release-group></release-group-list>");
        }
        content.append(FOOT);
        return content.toString();
    }

    private static String getTagsInXML(Collection<String> tags) {
        StringBuilder tagString = new StringBuilder("<user-tag-list>");
        for (String tag : tags) {
            tagString.append("<user-tag><name>" + tag + "</name></user-tag>");
        }
        tagString.append("</user-tag-list>");
        return tagString.toString();
    }

    static String buildRatingSubmissionXML(Entity entityType, String entityMbid, int rating) {
        StringBuilder content = new StringBuilder(HEAD);
        switch (entityType) {
        case ARTIST:
            content.append("<artist-list><artist id=\"" + entityMbid + "\"><user-rating>" + rating * 20
                    + "</user-rating></artist></artist-list>");
            break;
        case RELEASE_GROUP:
            content.append("<release-group-list><release-group id=\"" + entityMbid + "\"><user-rating>" + rating * 20
                    + "</user-rating></release-group></release-group-list>");
        }
        content.append(FOOT);
        return content.toString();
    }

    static String buildBarcodeSubmissionXML(String releaseMbid, String barcode) {
        StringBuilder content = new StringBuilder(HEAD);
        content.append("<release-list><release id=\"" + releaseMbid + "\"><barcode>" + barcode
                + "</barcode></release></release-list>");
        content.append(FOOT);
        return content.toString();
    }

}
