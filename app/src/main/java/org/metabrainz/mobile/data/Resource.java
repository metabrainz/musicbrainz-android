package org.metabrainz.mobile.data;

public class Resource<T> {
    private final Status status;
    private final T data;

    public Resource(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public enum Status {
        LOADING, FAILED, SUCCESS
    }
}
