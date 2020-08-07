package org.metabrainz.mobile.util;

public class Resource<T> {
    private final Status status;
    private final T data;

    public Resource(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <S> Resource<S> getFailure(Class<S> tClass) {
        return new Resource<>(Status.FAILED, null);
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
