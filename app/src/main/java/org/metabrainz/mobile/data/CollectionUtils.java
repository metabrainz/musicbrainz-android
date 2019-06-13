package org.metabrainz.mobile.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.metabrainz.mobile.data.sources.api.entities.userdata.Collection;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.CollectionListResponse;
import org.metabrainz.mobile.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {

    /*
     * The response from ws/2/collections has a field for count in all collections. Depending on the
     * entity, the name of the field can be artist-count, release-count etc. This method finds the
     * correct field name and assigns the value to count field in each collection. */
    public static void setGenericCountParameter(List<Collection> collections, String jsonResponse) {
        Map<String, String> countList = new HashMap<>();
        CollectionListResponse response = new Gson().fromJson(jsonResponse, CollectionListResponse.class);
        collections.addAll(response.getCollections());
        JsonElement jsonElement = new JsonParser().parse(jsonResponse);
        JsonArray result = jsonElement.getAsJsonObject().getAsJsonArray("collections");
        Iterator<JsonElement> iterator = result.iterator();

        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
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
            collection.setCount(Integer.valueOf(countList.get(id)));
        }
    }

    public static void removeCollections(List<Collection> collections) {
        for (Collection collection : collections) {
            String entity = collection.getEntityType();
            if (entity.equalsIgnoreCase("artist") || entity.equalsIgnoreCase("release")
                    || entity.equalsIgnoreCase("label") || entity.equalsIgnoreCase("event")
                    || entity.equalsIgnoreCase("instrument") || entity.equalsIgnoreCase("recording")
                    || entity.equalsIgnoreCase("release-group"))
                continue;
            collections.remove(collection);
        }
    }
}
