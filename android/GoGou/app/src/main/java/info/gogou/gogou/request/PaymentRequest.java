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

import java.nio.charset.Charset;

import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.model.PaymentMethod;
import info.gogou.gogou.model.PaymentMethodList;
import info.gogou.gogou.utils.GenericResponse;

/**
 * Created by lxu on 2015-12-05.
 */
public class PaymentRequest {

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

    public static class CreatePaymentMethodRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreatePaymentMethod";

        private String mUserName;
        private String mEncodedPassword;
        private PaymentMethod mPaymentMethod;

        public CreatePaymentMethodRequest(String userName, String encodedPassword, PaymentMethod paymentMethod)
        {
            super(GenericResponse.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mPaymentMethod = paymentMethod;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/paymentmethod/create";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mPaymentMethod);
            Log.d(TAG, "CreatePaymentMethodRequest json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader(mUserName, mEncodedPassword));

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, GenericResponse.class);

            Log.d("response message", ((GenericResponse)response.getBody()).getMessage());

            return (GenericResponse) response.getBody();
        }
    }

    public static class GetPaymentMethodListRequest extends SpringAndroidSpiceRequest<PaymentMethodList> {

        private static final String TAG = "GetPaymentMethodList";

        private String mUserName;
        private String mEncodedPassword;

        public GetPaymentMethodListRequest(String userName, String encodedPassword)
        {
            super(PaymentMethodList.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
        }

        @Override
        public PaymentMethodList loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/paymentmethod/list?userName=" + mUserName;

            PaymentMethodList paymentMethodList = getRestTemplate().getForObject(service_url, PaymentMethodList.class);

            return paymentMethodList;
        }
    }
}

