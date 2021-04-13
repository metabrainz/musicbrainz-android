package org.metabrainz.mobile.data.repository

interface LoginRepository {

    fun fetchAccessToken(code: String?)

    fun fetchUserInfo()
}