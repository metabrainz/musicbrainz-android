package org.metabrainz.mobile.data.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.api.LoginService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private static final LoginService service = MusicBrainzServiceGenerator
            .createService(LoginService.class, false);
    private static LoginRepository repository;
    private final MutableLiveData<AccessToken> accessTokenLiveData;
    private final MutableLiveData<UserInfo> userInfoLiveData;

    private LoginRepository() {
        accessTokenLiveData = new MutableLiveData<>();
        userInfoLiveData = new MutableLiveData<>();
    }

    public static LoginRepository getRepository() {
        if (repository == null) repository = new LoginRepository();
        return repository;
    }

    public MutableLiveData<AccessToken> getAccessTokenLiveData() {
        return accessTokenLiveData;
    }

    public MutableLiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public void fetchAccessToken(String code) {
        service.getAccessToken(code,
                "authorization_code",
                MusicBrainzServiceGenerator.CLIENT_ID,
                MusicBrainzServiceGenerator.CLIENT_SECRET,
                MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        AccessToken token = response.body();
                        accessTokenLiveData.setValue(token);
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public void fetchUserInfo() {
        service.getUserInfo().enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo info = response.body();
                userInfoLiveData.postValue(info);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

}
