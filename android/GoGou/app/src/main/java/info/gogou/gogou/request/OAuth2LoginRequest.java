package info.gogou.gogou.request;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.ResponseEntity;

import info.gogou.gogou.model.OAuth2Login;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.utils.GenericResponse;

/**
 * Created by user on 2016/4/5.
 */
public class OAuth2LoginRequest {

    public static class ThirdPartLoginRequest extends SpringAndroidSpiceRequest<OAuth2Login> {

        private static final String TAG = "ThirdPartLoginRequest";

        private String code;

        public ThirdPartLoginRequest(String code)
        {
            super(OAuth2Login.class);
            this.code = code;
        }

        @Override
        public OAuth2Login loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/Oauth2Login/getAccessToken?code=" + code;

            ResponseEntity response = getRestTemplate().getForEntity(service_url, OAuth2Login.class);

            return (OAuth2Login) response.getBody();
        }
    }

    public static class RefreshTokenRequest extends SpringAndroidSpiceRequest<OAuth2Login> {

        private static final String TAG = "RefreshTokenRequest";

        private String refreshToken;

        public RefreshTokenRequest(String refreshToken)
        {
            super(OAuth2Login.class);
            this.refreshToken = refreshToken;
        }

        @Override
        public OAuth2Login loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/Oauth2Login/refreshAccessToken?refresh_token=" + refreshToken;

            ResponseEntity response = getRestTemplate().getForEntity(service_url, OAuth2Login.class);

            return (OAuth2Login) response.getBody();
        }
    }

    public static class CheckAccessTokenRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CheckAccessTokenRequest";

        private String accessToken;

        public CheckAccessTokenRequest(String accessToken)
        {
            super(GenericResponse.class);
            this.accessToken = accessToken;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/Oauth2Login/checkAccessToken?access_token=" + accessToken;

            ResponseEntity response = getRestTemplate().getForEntity(service_url, GenericResponse.class);

            return (GenericResponse) response.getBody();
        }
    }
}
