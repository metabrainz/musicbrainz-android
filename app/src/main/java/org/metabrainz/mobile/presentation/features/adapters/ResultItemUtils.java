package org.metabrainz.mobile.presentation.features.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultItemUtils {

    public static ResultItem getEntityAsResultItem(MBEntity entity) {
        ResultItem item;
        if (entity instanceof Artist) {
            Artist artist = (Artist) entity;
            item = new ResultItem(artist.getMbid(), artist.getName(), artist.getDisambiguation(),
                    artist.getType(), artist.getCountry());
        } else if (entity instanceof Event) {
            Event event = (Event) entity;
            if (event.getLifeSpan() != null)
                item = new ResultItem(event.getMbid(), event.getName(), event.getDisambiguation(),
                        event.getType(), event.getLifeSpan().getTimePeriod());
            else
                item = new ResultItem(event.getMbid(), event.getName(), event.getDisambiguation(),
                        event.getType(), "");
        } else if (entity instanceof Instrument) {
            Instrument instrument = (Instrument) entity;
            item = new ResultItem(instrument.getMbid(), instrument.getName(), instrument.getDisambiguation(),
                    instrument.getDescription(), instrument.getType());
        } else if (entity instanceof Label) {
            Label label = (Label) entity;
            item = new ResultItem(label.getMbid(), label.getName(), label.getDisambiguation(),
                    label.getType(), label.getCountry());
        } else if (entity instanceof Recording) {
            Recording recording = (Recording) entity;
            if (recording.getReleases() != null && recording.getReleases().size() > 0)
                item = new ResultItem(recording.getMbid(), recording.getTitle(), recording.getDisambiguation(),
                        recording.getReleases().get(0).getTitle(), EntityUtils.getDisplayArtist(recording.getArtistCredits()));
            else
                item = new ResultItem(recording.getMbid(), recording.getTitle(), recording.getDisambiguation(),
                        "", EntityUtils.getDisplayArtist(recording.getArtistCredits()));
        } else if (entity instanceof Release) {
            Release release = (Release) entity;
            item = new ResultItem(release.getMbid(), release.getTitle(), release.getDisambiguation(),
                    EntityUtils.getDisplayArtist(release.getArtistCredits()), release.labelCatalog());
        } else if (entity instanceof ReleaseGroup) {
            ReleaseGroup releaseGroup = (ReleaseGroup) entity;
            item = new ResultItem(releaseGroup.getMbid(), releaseGroup.getTitle(), releaseGroup.getDisambiguation(),
                    EntityUtils.getDisplayArtist(releaseGroup.getArtistCredits()), releaseGroup.getFullType());
        } else item = null;
        return item;
    }

    private static Type getTypeToken(MBEntityType entity) {
        if (entity == MBEntityType.ARTIST)
            return TypeToken.getParameterized(List.class, Artist.class).getType();
        else if (entity == MBEntityType.RELEASE)
            return TypeToken.getParameterized(List.class, Release.class).getType();
        else if (entity == MBEntityType.LABEL)
            return TypeToken.getParameterized(List.class, Label.class).getType();
        else if (entity == MBEntityType.RECORDING)
            return TypeToken.getParameterized(List.class, Recording.class).getType();
        else if (entity == MBEntityType.EVENT)
            return TypeToken.getParameterized(List.class, Event.class).getType();
        else if (entity == MBEntityType.INSTRUMENT)
            return TypeToken.getParameterized(List.class, Instrument.class).getType();
        else if (entity == MBEntityType.RELEASE_GROUP)
            return TypeToken.getParameterized(List.class, ReleaseGroup.class).getType();
        else return null;
    }

    public static List<ResultItem> getJSONResponseAsResultItemList(String response, MBEntityType entity) {
        List<? extends MBEntity> list = new Gson().fromJson(
                JsonParser.parseString(response)
                        .getAsJsonObject()
                        .get(entity.name + "s"), ResultItemUtils.getTypeToken(entity));
        List<ResultItem> items = new ArrayList<>();
        for (MBEntity e : list) items.add(ResultItemUtils.getEntityAsResultItem(e));
        return items;
    }
}
