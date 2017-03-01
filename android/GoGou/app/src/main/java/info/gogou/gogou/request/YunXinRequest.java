package info.gogou.gogou.request;

import android.util.Base64;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;

import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by user on 2016/4/16.
 */
public class YunXinRequest {

    public static class YunXinRegisterIDRequest extends SpringAndroidSpiceRequest<Subscriber> {

        private static final String TAG = "YunXinRegisterIDRequest";

        private String userName;
        private String encodedPassword;

        public YunXinRegisterIDRequest(String userName, String encodedPassword)
        {
            super(Subscriber.class);
            this.userName = userName;
            this.encodedPassword = encodedPassword;
        }

        static HttpHeaders getHeader(String userName, String encodedPassword){
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
            headers.setContentType(mediaType);
            if (userName != null && encodedPassword != null) {
                //Basic Authentication
                String authorisation = userName + ":" + encodedPassword;
                byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes(), Base64.NO_WRAP);
                headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
            }

            return headers;
        }

        @Override
        public Subscriber loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/yunxin/registeryunxinId?username="+userName;

            //ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //String json = writer.writeValueAsString("{'username':'"+userName+"'}");

            HttpEntity<String> entity = new HttpEntity<String>(null, getHeader(userName, encodedPassword));

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, Subscriber.class);

            return (Subscriber)response.getBody();
        }
    }
}
