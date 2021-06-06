package org.metabrainz.mobile.util

class Resource<T>(val status: Status, val data: T?) {

    enum class Status {
        LOADING, FAILED, SUCCESS
    }

    companion object {
        fun <S> getFailure(tClass: Class<S>?): Resource<S> {
            return Resource(Status.FAILED, null)
        }
    }
}