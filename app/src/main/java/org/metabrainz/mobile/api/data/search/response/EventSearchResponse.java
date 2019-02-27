package org.metabrainz.mobile.api.data.search.response;

import org.metabrainz.mobile.api.data.search.entity.Event;

import java.util.List;

public class EventSearchResponse extends JSONResponse {
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
