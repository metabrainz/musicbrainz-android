package org.metabrainz.mobile.presentation.features.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LoginRepository;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;

public class LoginViewModel extends ViewModel {

    private LoginRepository repository = LoginRepository.getRepository();
    private MutableLiveData<AccessToken> accessTokenLiveData;

    public LoginViewModel() {
    }

    public MutableLiveData<AccessToken> getAccessTokenLiveData() {
        if (accessTokenLiveData == null) accessTokenLiveData = repository.getAccessTokenLiveData();
        return accessTokenLiveData;
    }

    public void fetchAccessToken(String code) {
        repository.fetchAccessToken(code);
    }
}
