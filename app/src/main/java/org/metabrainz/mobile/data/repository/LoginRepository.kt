package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.api.LoginService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class LoginRepository {

    private final LoginService service;
    private final MutableLiveData<AccessToken> accessTokenLiveData;
    private final MutableLiveData<UserInfo> userInfoLiveData;

    @Inject
    public LoginRepository(LoginService service) {
        this.service = service;
        accessTokenLiveData = new MutableLiveData<>();
        userInfoLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<AccessToken> getAccessTokenLiveData() {
        return accessTokenLiveData;
    }

    public MutableLiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public void fetchAccessToken(String code) {
        service.getAccessToken(MusicBrainzServiceGenerator.AUTH_BASE_URL + "token",
                code,
                "authorization_code",
                MusicBrainzServiceGenerator.CLIENT_ID,
                MusicBrainzServiceGenerator.CLIENT_SECRET,
                MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                        AccessToken token = response.body();
                        accessTokenLiveData.setValue(token);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public void fetchUserInfo() {
        service.getUserInfo().enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                UserInfo info = response.body();
                userInfoLiveData.postValue(info);
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {

            }
        });
    }

}
