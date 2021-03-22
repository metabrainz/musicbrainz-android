package org.metabrainz.mobile.data.sources.api.entities;

public class LinksClassifier {
    private static final String OFFICIAL_HOMEPAGE = "official homepage";
    public static final String DISCOGRAPHY = "discography";
    private static final String FANPAGE = "fanpage";
    private static final String BIOGRAPHY = "biography";
    public static final String BBC_MUSIC_PAGE = "BBC Music Page";
    public static final String INTERVIEW = "interview";
    private static final String IMAGE = "image";
    public static final String LYRICS = "lyrics";
    public static final String ONLINE_DATA = "online_data";
    public static final String SOCIAL_NETWORK = "social network";
    public static final String MYSPACE = "myspace";
    public static final String PUREVOLUME = "purevolume";
    public static final String SOUNDCLOUD = "soundcloud";
    public static final String VIDEO_CHANNEL = "video channel";
    public static final String YOUTUBE = "youtube";
    public static final String ONLINE_COMMUNITY = "online community";
    public static final String BLOG = "blog";
    public static final String CROWDFUNDING = "crowdfunding";
    public static final String PATRONAGE = "patronage";
    private static final String PURCHASE_FOR_MAIL_ORDER = "purchase for mail-order";
    private static final String PURCHASE_FOR_DOWNLOAD = "purchase for download";
    private static final String DOWNLOAD_FOR_FREE = "download for free";
    private static final String STREAMING_MUSIC = "streaming music";
    public static final String BANDCAMP = "bandcamp";
    public static final String OTHER_DATABASES = "other databases";
    public static final String ALLMUSIC = "allmusic";
    public static final String BOOKBRAINZ = "bookbrainz";
    public static final String DISCOGS = "discogs";
    public static final String IMDB = "IMDb";
    public static final String IMSLP = "IMSLP";
    private static final String LAST_FM = "last.fm";
    public static final String SECOND_HAND_SONGS = "secondhandsongs";
    public static final String SET_LIST_FM = "setlistfm";
    public static final String SONGKICK = "songkick";
    public static final String VGMDB = "vgmdb";
    public static final String VIAF = "VIAF";
    private static final String WIKIDATA = "wikidata";
    private static final String WIKIPEDIA = "wikipedia";
    private static final String INFO = "info";
    private static final String MISC = "misc";
    private static final String GET_THE_MUSIC = "get the music";

    public static String classifyToDisplayText(Link link) {
        String type = link.getType();
        String url = link.getUrl().getResource();
        if (type != null && !type.isEmpty() && url != null && !url.isEmpty()) {
            switch (type) {
                case STREAMING_MUSIC:
                    return "<a href=\"" + url + "\"> Stream Music </a>";
                case PURCHASE_FOR_DOWNLOAD:
                    return "&lt;a href=http://www.google.co.in&gt;Google&lt;/a&gt;";
                case PURCHASE_FOR_MAIL_ORDER:
                    return "<a href=\"" + url + "\"> Purchase for mail-order </a>";
                case DOWNLOAD_FOR_FREE:
                    return "<a href=\"" + url + "\"> Download for free </a>";
                case YOUTUBE:
                    return "<a href=\"" + url + "\"> Stream Music </a>";
                case GET_THE_MUSIC:
                    return "<a href=\"" + url + "\"> Get the Music </a>";
                case IMAGE:
                case BIOGRAPHY:
                case WIKIDATA:
                case WIKIPEDIA:
                case OFFICIAL_HOMEPAGE:
                case FANPAGE:
                case LAST_FM:
                case INFO:
                default:
                    return url;
            }
        } else return MISC;
    }

    public static String classifyCategory(Link link) {
        String type = link.getType();
        if (type != null && !type.isEmpty()) {
            switch (type) {
                case STREAMING_MUSIC:
                case PURCHASE_FOR_DOWNLOAD:
                case PURCHASE_FOR_MAIL_ORDER:
                case DOWNLOAD_FOR_FREE:
                case YOUTUBE:
                case GET_THE_MUSIC:
                    return GET_THE_MUSIC;
                case IMAGE:
                case BIOGRAPHY:
                case WIKIDATA:
                case WIKIPEDIA:
                case OFFICIAL_HOMEPAGE:
                case FANPAGE:
                case LAST_FM:
                case INFO:
                    return INFO;
                default:
                    return MISC;
            }
        } else return MISC;
    }
}
