package org.metabrainz.android.repository

import androidx.lifecycle.MutableLiveData
import org.metabrainz.android.model.entities.AccessToken
import org.metabrainz.android.model.userdata.UserInfo

interface LoginRepository {

    val accessTokenLiveData: MutableLiveData<org.metabrainz.android.model.entities.AccessToken?>
    val userInfoLiveData: MutableLiveData<UserInfo?>

    fun fetchAccessToken(code: String?)

    fun fetchUserInfo()
}