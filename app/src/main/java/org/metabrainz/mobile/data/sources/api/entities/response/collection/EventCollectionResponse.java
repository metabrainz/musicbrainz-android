package org.metabrainz.mobile.data.sources.api.entities.response.collection;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;

import java.util.ArrayList;
import java.util.List;

public class EventCollectionResponse {

    @SerializedName("event-count")
    private int count;

    @SerializedName("event-offset")
    private int offset;

    private List<Event> events = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
