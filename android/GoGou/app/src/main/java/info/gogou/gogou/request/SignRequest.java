package info.gogou.gogou.request;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.model.SignatureRequest;
import info.gogou.gogou.utils.GenericResponse;

/**
 * Created by lxu on 2015-12-05.
 */
public class SignRequest {

    static HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);

        return headers;
    }

    public static class CreateRSASignatureRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreateTripRequest";

        private SignatureRequest mSignatureRequest;

        public CreateRSASignatureRequest(SignatureRequest signatureRequest)
        {
            super(GenericResponse.class);
            mSignatureRequest = signatureRequest;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/signature/signwithrsa";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mSignatureRequest);
            Log.d(TAG, "Signature request json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

            GenericResponse response = getRestTemplate().postForObject(service_url, mSignatureRequest, GenericResponse.class);

            return response;
        }
    }
}

