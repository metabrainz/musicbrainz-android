package org.metabrainz.mobile.data.sources.api.entities.response.search;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;

import java.util.List;

public class EventSearchResponse extends JSONSearchResponse {
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
