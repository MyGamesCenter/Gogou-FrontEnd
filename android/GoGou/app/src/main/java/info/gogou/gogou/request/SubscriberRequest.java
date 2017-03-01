package info.gogou.gogou.request;

import android.util.Base64;
import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

import info.gogou.gogou.model.LoginRequest;
import info.gogou.gogou.model.RecoverPwdRequest;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by lxu on 2015-12-05.
 */
public class SubscriberRequest {

    static HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);

        return headers;
    }

    static HttpHeaders getMultipartFormHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return headers;
    }

    static HttpHeaders getMultipartFormHeader(String userName, String encodedPassword){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        if (userName != null && encodedPassword != null) {
            //Basic Authentication
            String authorisation = userName + ":" + encodedPassword;
            byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes(), Base64.NO_WRAP);
            headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
        }

        return headers;
    }

    public static class GetRegistrationCodeRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "GetRegistrationCodeRequest";

        private String mUsername;
        private String mEmailAddress;
        private String mLanguageCode;

        public GetRegistrationCodeRequest(String username, String emailAddress, String languageCode)
        {
            super(GenericResponse.class);
            mUsername = username;
            mEmailAddress = emailAddress;
            mLanguageCode = languageCode;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/subscriber/getRegCode?username="
                    + mUsername + "&emailAddress=" + mEmailAddress + "&languageCode=" + mLanguageCode;

            ResponseEntity response = getRestTemplate().getForEntity(service_url, GenericResponse.class);

            return (GenericResponse)response.getBody();
        }
    }

    public static class GetRecoverCodeRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "GetRecoverCodeRequest";

        private String mUsername;
        private String mEmailAddress;
        private String mLanguageCode;

        public GetRecoverCodeRequest(String username, String emailAddress, String languageCode)
        {
            super(GenericResponse.class);
            mUsername = username;
            mEmailAddress = emailAddress;
            mLanguageCode = languageCode;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/subscriber/getRecoverCode?username="
                    + mUsername + "&emailAddress=" + mEmailAddress + "&languageCode=" + mLanguageCode;;

            ResponseEntity response = getRestTemplate().getForEntity(service_url, GenericResponse.class);

            return (GenericResponse)response.getBody();
        }
    }

    public static class RecoverPasswordRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "GetRecoverCodeRequest";

        private RecoverPwdRequest mRecoverPassRequest;

        public RecoverPasswordRequest(RecoverPwdRequest recoverPwdRequest)
        {
            super(GenericResponse.class);
            mRecoverPassRequest = recoverPwdRequest;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/subscriber/recoverPassword";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mRecoverPassRequest);
            Log.d(TAG, "Recover password request json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, GenericResponse.class);

            return (GenericResponse)response.getBody();
        }
    }


    public static class CreateSubscriberRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreateSubscriberRequest";

        private Subscriber mSubscriber;

        public CreateSubscriberRequest(Subscriber subscriber)
        {
            super(GenericResponse.class);
            mSubscriber = subscriber;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/subscriber/registration";

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mSubscriber);
            Log.d(TAG, "Subscriber json string: " + json);

            RestTemplate restTemplate = getRestTemplate();
            FormHttpMessageConverter fc = new FormHttpMessageConverter();
            fc.addPartConverter(new MappingJacksonHttpMessageConverter());
            restTemplate.getMessageConverters().add(fc);

            HttpHeaders header = getHeader();
            header.setContentDispositionFormData("subscriber", null);

            HttpEntity<Subscriber> subEntity = new HttpEntity<Subscriber>(mSubscriber, header);
            map.add("subscriber", subEntity);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, getMultipartFormHeader());

            GenericResponse response = restTemplate.postForObject(service_url, entity, GenericResponse.class);

            return response;
        }
    }

    public static class UpdateSubscriberRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "UpdateSubscriberRequest";

        private Subscriber mSubscriber;

        public UpdateSubscriberRequest(Subscriber subscriber)
        {
            super(GenericResponse.class);
            mSubscriber = subscriber;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/subscriber/update";

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mSubscriber);
            Log.d(TAG, "Subscriber json string: " + json);

            RestTemplate restTemplate = getRestTemplate();
            FormHttpMessageConverter fc = new FormHttpMessageConverter();
            fc.addPartConverter(new MappingJacksonHttpMessageConverter());
            restTemplate.getMessageConverters().add(fc);

            HttpHeaders header = getHeader();
            header.setContentDispositionFormData("subscriber", null);

            HttpEntity<Subscriber> subEntity = new HttpEntity<Subscriber>(mSubscriber, header);
            map.add("subscriber", subEntity);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, getMultipartFormHeader(mSubscriber.getUserName(), mSubscriber.getEncodedPassword()));

            GenericResponse response = restTemplate.postForObject(service_url, entity, GenericResponse.class);

            return response;
        }
    }

    public static class SubscriberLoginRequest extends SpringAndroidSpiceRequest<Subscriber> {

        private static final String TAG = "LoginRequest";

        private Subscriber mSubscriber;
        private LoginRequest mLoginRequest;

        public SubscriberLoginRequest(LoginRequest loginRequest)
        {
            super(Subscriber.class);
            mLoginRequest = loginRequest;
        }

        @Override
        public Subscriber loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/subscriber/login";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mLoginRequest);
            Log.d(TAG, "LoginRequest json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, Subscriber.class);

            return (Subscriber) response.getBody();
        }

        /**
         * This method generates a unique cache key for this request. In this case
         * our cache key depends on subscriber's password and username.
         * @return
         */
        public String createCacheKey() {

            return mLoginRequest.getPassword() + mLoginRequest.getUsername();
        }

    }
}
