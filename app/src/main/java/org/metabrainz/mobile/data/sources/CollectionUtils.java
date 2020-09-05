package org.metabrainz.mobile.data.sources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.response.CollectionListResponse;
import org.metabrainz.mobile.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CollectionUtils {

    /*
     * The response from ws/2/collections has a field for count in all collections. Depending on the
     * entity, the name of the field can be artist-count, release-count etc. This method finds the
     * correct field name and assigns the value to count field in each collection. */
    public static List<Collection> setGenericCountParameter(String jsonResponse) {
        Map<String, String> countList = new HashMap<>();
        CollectionListResponse response = new Gson().fromJson(jsonResponse, CollectionListResponse.class);
        List<Collection> collections = new ArrayList<>(response.getCollections());
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonArray result = jsonElement.getAsJsonObject().getAsJsonArray("collections");

        for (JsonElement element : result) {
            Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
            String count = "", id = "";
            for (Map.Entry<String, JsonElement> entry : entries) {
                if (entry.getKey().contains("count")) count = entry.getValue().getAsString();
                if (entry.getKey().equalsIgnoreCase("id")) id = entry.getValue().getAsString();
            }
            Log.d(id + " " + count);
            countList.put(id, count);
        }
        for (Collection collection : collections) {
            String id = collection.getMbid();
            collection.setCount(Integer.parseInt(Objects.requireNonNull(countList.get(id))));
        }
        return collections;
    }

    public static void removeCollections(List<Collection> collections) {
        Iterator<Collection> itr = collections.iterator();
        while (itr.hasNext()) {
            String entity = itr.next().getEntityType();
            if (entity.equalsIgnoreCase("artist") || entity.equalsIgnoreCase("release")
                    || entity.equalsIgnoreCase("label") || entity.equalsIgnoreCase("event")
                    || entity.equalsIgnoreCase("instrument") || entity.equalsIgnoreCase("recording")
                    || entity.equalsIgnoreCase("release-group"))
                continue;
            itr.remove();
        }
    }

    public static MBEntityType getCollectionEntityType(Collection collection) {
        return MBEntityType.valueOf(collection
                .getEntityType()
                .replace('-', '_')
                .toUpperCase());
    }
}
