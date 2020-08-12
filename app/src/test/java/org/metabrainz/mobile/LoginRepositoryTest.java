package org.metabrainz.mobile;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.metabrainz.mobile.data.sources.api.LoginService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.metabrainz.mobile.RetrofitUtils.createTestService;

public class LoginRepositoryTest {

    MockWebServer webServer;
    String code = "Nlaa7v15QHm9g8rUOmT3dQ";
    String accessToken = "{\n" +
            "  \"access_token\":\"8OC8as1VpATqkM79KfcdTw\",\n" +
            "  \"expires_in\":3600,\n" +
            "  \"token_type\":\"Bearer\",\n" +
            "  \"refresh_token\":\"Td5-okcuJvqgXLuq_YMXHA\"\n" +
            "}";

    private String getErrorBody(String reason) {
        return String.format("{\"error\":\"%s\"}", reason);
    }

    @Before
    public void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                MockResponse response = null;
                switch (recordedRequest.getPath()) {
                    case "/oauth2/authorize":
                        response = new MockResponse()
                                .setResponseCode(200)
                                .setStatus("OK")
                                .setBody(MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI + "?code=" + code);
                        break;
                    case "/oauth2/token":
                        Map<String, String> parameters = new HashMap<>();
                        String[] args = recordedRequest.getBody().readUtf8().split("&");
                        Arrays.stream(args).forEach(s -> {
                            String[] parts = s.split("=");
                            parameters.put(parts[0], parts[1]);
                        });
                        String invalid = "";
                        if (!parameters.containsKey("grant_type"))
                            invalid = "Missing grant type";
                        else if (!parameters.containsKey("client_id"))
                            invalid = "Missing Client id";
                        else if (!parameters.containsKey("client_secret"))
                            invalid = "Missing Client Secret";
                        else if (!parameters.containsKey("code"))
                            invalid = "Missing code";
                        else if (!parameters.containsKey("redirect_uri"))
                            invalid = "Missing redirect URI";
                        else if (!parameters.get("grant_type").equalsIgnoreCase("authorization_code"))
                            invalid = "Invalid grant type";
                        else if (!parameters.get("client_id").equalsIgnoreCase(MusicBrainzServiceGenerator.CLIENT_ID))
                            invalid = "Invalid Client ID";
                        else if (!parameters.get("client_secret").equalsIgnoreCase(MusicBrainzServiceGenerator.CLIENT_SECRET))
                            invalid = "Invalid Client Secret";
                        else if (!parameters.get("code").equalsIgnoreCase(code))
                            invalid = "Invalid authorization code";
                        else {
                            response = new MockResponse()
                                    .setResponseCode(200)
                                    .setBody(accessToken);
                        }
                        if (!invalid.isEmpty())
                            response = new MockResponse()
                                    .setResponseCode(401)
                                    .setBody(getErrorBody(invalid));
                        break;
                    default:
                        response = new MockResponse().setResponseCode(404);
                }
                return response;
            }
        });
        webServer.start();
    }

    @Test
    public void fetchAccessToken() throws IOException {
        LoginService service = createTestService(LoginService.class, webServer.url("/"));

        AccessToken accessToken = service
                .getAccessToken(webServer.url("/oauth2/") + "token",
                        code,
                        "authorization_code",
                        MusicBrainzServiceGenerator.CLIENT_ID,
                        MusicBrainzServiceGenerator.CLIENT_SECRET,
                        MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)
                .execute()
                .body();

        assert accessToken != null;
        assertEquals(accessToken.getAccessToken(), "8OC8as1VpATqkM79KfcdTw");
    }

    @Test
    public void fetchUserInfo() {
    }
}