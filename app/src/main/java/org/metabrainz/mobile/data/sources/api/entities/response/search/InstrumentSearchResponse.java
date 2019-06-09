package org.metabrainz.mobile.data.sources.api.entities.response.search;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;

import java.util.ArrayList;
import java.util.List;

public class InstrumentSearchResponse extends JSONSearchResponse {
    private List<Instrument> instruments = new ArrayList<>();

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }
}
