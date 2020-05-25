package org.metabrainz.mobile.presentation.features.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.IntentFactory;

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
                        recording.getReleases().get(0).getTitle(), recording.getDisplayArtist());
            else
                item = new ResultItem(recording.getMbid(), recording.getTitle(), recording.getDisambiguation(),
                        "", recording.getDisplayArtist());
        } else if (entity instanceof Release) {
            Release release = (Release) entity;
            item = new ResultItem(release.getMbid(), release.getTitle(), release.getDisambiguation(),
                    release.getDisplayArtist(), release.labelCatalog());
        } else if (entity instanceof ReleaseGroup) {
            ReleaseGroup releaseGroup = (ReleaseGroup) entity;
            item = new ResultItem(releaseGroup.getMbid(), releaseGroup.getTitle(), releaseGroup.getDisambiguation(),
                    releaseGroup.getDisplayArtist(), releaseGroup.getFullType());
        } else item = null;
        return item;
    }

    public static Type getTypeToken(String entity) {
        if (entity.equalsIgnoreCase(IntentFactory.Extra.ARTIST))
            return TypeToken.getParameterized(List.class, Artist.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.RELEASE))
            return TypeToken.getParameterized(List.class, Release.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.LABEL))
            return TypeToken.getParameterized(List.class, Label.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.RECORDING))
            return TypeToken.getParameterized(List.class, Recording.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.EVENT))
            return TypeToken.getParameterized(List.class, Event.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.INSTRUMENT))
            return TypeToken.getParameterized(List.class, Instrument.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.RELEASE_GROUP))
            return TypeToken.getParameterized(List.class, ReleaseGroup.class).getType();
        else return null;
    }

    public static List<ResultItem> getJSONResponseAsResultItemList(String response, String entity) {
        List<? extends MBEntity> list = new Gson().fromJson(
                JsonParser.parseString(response)
                        .getAsJsonObject()
                        .get(entity + "s"), ResultItemUtils.getTypeToken(entity));
        List<ResultItem> items = new ArrayList<>();
        for (MBEntity e : list) items.add(ResultItemUtils.getEntityAsResultItem(e));
        return items;
    }
}
