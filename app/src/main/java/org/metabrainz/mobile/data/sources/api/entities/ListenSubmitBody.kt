package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListenSubmitBody {

    @SerializedName("listen_type")
    String listenType;

    List<Payload> payload = new ArrayList<>();

    public String getListenType() {
        return listenType;
    }

    public void setListenType(String listenType) {
        this.listenType = listenType;
    }

    public List<Payload> getPayload() {
        return payload;
    }

    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

    public void addListen(Payload payload) {
        this.payload.add(payload);
    }

    public void addListen(long timestamp, ListenTrackMetadata metadata) {
        this.payload.add(new Payload(timestamp, metadata));
    }

    @NotNull
    @Override
    public String toString() {
        return "ListenSubmitBody{" +
                "listenType='" + listenType + '\'' +
                ", payload=" + payload +
                '}';
    }

    public static class Payload {
        @SerializedName("listened_at")
        long timestamp;

        @SerializedName("track_metadata")
        ListenTrackMetadata metadata;


        public Payload(long timestamp, ListenTrackMetadata metadata) {
            this.timestamp = timestamp;
            this.metadata = metadata;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public ListenTrackMetadata getMetadata() {
            return metadata;
        }

        public void setMetadata(ListenTrackMetadata metadata) {
            this.metadata = metadata;
        }

        @NotNull
        @Override
        public String toString() {
            return "Payload{" +
                    "timestamp=" + timestamp +
                    ", metadata=" + metadata +
                    '}';
        }
    }
}
