package org.metabrainz.android.util

class Resource<T>(val status: Status, val data: T?) {

    enum class Status {
        LOADING, FAILED, SUCCESS
    }

    companion object {
        fun <S> failure(): Resource<S> {
            return Resource(Status.FAILED, null)
        }
        fun <S> loading(): Resource<S> {
            return Resource(Status.LOADING, null)
        }
    }
}