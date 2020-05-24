package org.metabrainz.mobile.data.sources.api.entities.mbentity;

public enum MBEntities {
    ARTIST("artist"),
    RELEASE("release"),
    LABEL("label"),
    RELEASE_GROUP("release-group"),
    RECORDING("recording"),
    EVENT("event"),
    INSTRUMENT("instrument");

    public final String name;

    MBEntities(String entity) {
        this.name = entity;
    }
}
