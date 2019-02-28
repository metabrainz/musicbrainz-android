package org.metabrainz.mobile.api.data.search.response;

import org.metabrainz.mobile.api.data.search.entity.Instrument;

import java.util.ArrayList;
import java.util.List;

public class InstrumentSearchResponse extends JSONResponse {
    private List<Instrument> instruments = new ArrayList<>();

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }
}
