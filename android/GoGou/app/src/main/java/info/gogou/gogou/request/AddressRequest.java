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
import info.gogou.gogou.model.Address;
import info.gogou.gogou.model.AddressList;
import info.gogou.gogou.utils.GenericResponse;

/**
 * Created by lxu on 2015-12-05.
 */
public class AddressRequest {

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

    public static class CreateAddressRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreateTripRequest";

        private String mUserName;
        private String mEncodedPassword;
        private Address mAddress;

        public CreateAddressRequest(String userName, String encodedPassword, Address address)
        {
            super(GenericResponse.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mAddress = address;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/address/create";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mAddress);
            Log.d(TAG, "CreateAddressRequest json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader(mUserName, mEncodedPassword));

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, GenericResponse.class);

            Log.d("response message", ((GenericResponse)response.getBody()).getMessage());

            return (GenericResponse) response.getBody();
        }
    }

    public static class GetAddressListRequest extends SpringAndroidSpiceRequest<AddressList> {

        private static final String TAG = "GetAddressListRequest";

        private String mUserName;
        private String mEncodedPassword;

        public GetAddressListRequest(String userName, String encodedPassword)
        {
            super(AddressList.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
        }

        @Override
        public AddressList loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/address/list?userName=" + mUserName;

            AddressList addressList = getRestTemplate().getForObject(service_url, AddressList.class);

            return addressList;
        }
    }
}

