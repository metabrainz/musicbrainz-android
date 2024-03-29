package org.metabrainz.android.repository

import androidx.lifecycle.MutableLiveData
import org.metabrainz.android.service.LoginService
import org.metabrainz.android.service.MusicBrainzServiceGenerator
import org.metabrainz.android.model.entities.AccessToken
import org.metabrainz.android.model.userdata.UserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(private val service: LoginService) : LoginRepository {

    override val accessTokenLiveData: MutableLiveData<org.metabrainz.android.model.entities.AccessToken?> = MutableLiveData()
    override val userInfoLiveData: MutableLiveData<UserInfo?> = MutableLiveData()

    override fun fetchAccessToken(code: String?) {
        service.getAccessToken(
            MusicBrainzServiceGenerator.AUTH_BASE_URL + "token",
                code,
                "authorization_code",
                MusicBrainzServiceGenerator.CLIENT_ID,
                MusicBrainzServiceGenerator.CLIENT_SECRET,
                MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)!!.enqueue(object : Callback<org.metabrainz.android.model.entities.AccessToken?> {
                    override fun onResponse(call: Call<org.metabrainz.android.model.entities.AccessToken?>, response: Response<org.metabrainz.android.model.entities.AccessToken?>) {
                        val token = response.body()
                        accessTokenLiveData.value = token
                    }

                    override fun onFailure(call: Call<org.metabrainz.android.model.entities.AccessToken?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
    }

    override fun fetchUserInfo() {
        service.userInfo!!.enqueue(object : Callback<UserInfo?> {
            override fun onResponse(call: Call<UserInfo?>, response: Response<UserInfo?>) {
                val info = response.body()
                userInfoLiveData.postValue(info)
            }

            override fun onFailure(call: Call<UserInfo?>, t: Throwable) {}
        })
    }

}