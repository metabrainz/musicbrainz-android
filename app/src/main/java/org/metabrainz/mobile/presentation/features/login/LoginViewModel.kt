package org.metabrainz.mobile.presentation.features.login;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LoginRepository;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserInfo;

public class LoginViewModel extends ViewModel {

    private LoginRepository repository;
    private MutableLiveData<AccessToken> accessTokenLiveData;
    private MutableLiveData<UserInfo> userInfoLiveData;

    @ViewModelInject
    public LoginViewModel(LoginRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<AccessToken> getAccessTokenLiveData() {
        if (accessTokenLiveData == null) accessTokenLiveData = repository.getAccessTokenLiveData();
        return accessTokenLiveData;
    }

    public MutableLiveData<UserInfo> getUserInfoLiveData() {
        if (userInfoLiveData == null) userInfoLiveData = repository.getUserInfoLiveData();
        return userInfoLiveData;
    }

    public void fetchAccessToken(String code) {
        repository.fetchAccessToken(code);
    }

    public void fetchUserInfo() {
        repository.fetchUserInfo();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository = null;
    }
}
