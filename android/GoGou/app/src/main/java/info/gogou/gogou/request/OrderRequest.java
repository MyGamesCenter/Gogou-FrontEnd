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

import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderFilter;
import info.gogou.gogou.model.OrderList;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by grace on 16-3-17.
 */
public class OrderRequest {

    static HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);
        return headers;
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


    public static class CreateOrderRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreateOrderRequest";

        private String mUserName;
        private String mEncodedPassword;
        private Order mOrder;

        public CreateOrderRequest(String userName, String encodedPassword, Order order)
        {
            super(GenericResponse.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mOrder = order;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/order/create";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mOrder);
            Log.d(TAG, "CreateOrderRequest json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader(mUserName, mEncodedPassword));

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, GenericResponse.class);

            Log.d("response message", ((GenericResponse)response.getBody()).getMessage());

            return (GenericResponse) response.getBody();
        }
    }

    public static class UpdateOrderRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "UpdateOrderRequest";

        private String mUserName;
        private String mEncodedPassword;
        private Order mOrder;

        public UpdateOrderRequest(String userName, String encodedPassword, Order order)
        {
            super(GenericResponse.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mOrder = order;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/order/update";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mOrder);
            Log.d(TAG, "CreateOrderRequest json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader(mUserName, mEncodedPassword));

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, GenericResponse.class);

            Log.d("response message", ((GenericResponse)response.getBody()).getMessage());

            return (GenericResponse) response.getBody();
        }
    }

    
    public static class GetOrderListRequest extends SpringAndroidSpiceRequest<OrderList> {

        private static final String TAG = "GetOrderListRequest";

        private OrderFilter mOrderFilter;
        private String mUserName;
        private String mEncodedPassword;

        public GetOrderListRequest(String userName, String encodedPassword, OrderFilter orderFilter)
        {
            super(OrderList.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mOrderFilter = orderFilter;
        }

        @Override
        public OrderList loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/order/list";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mOrderFilter);

            Log.d(TAG, "OrderFilter json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader(mUserName, mEncodedPassword));

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, OrderList.class);

            return (OrderList) response.getBody();
        }
    }


    public static class GetOrderRequest extends SpringAndroidSpiceRequest<Order> {

        private static final String TAG = "GetOrderRequest";

        private Long mOrderId;
        private String mUserName;
        private String mEncodedPassword;


        public GetOrderRequest(String userName, String encodedPassword, long  orderId)
        {
            super(Order.class);
            mOrderId = orderId;
            mUserName = userName;
            mEncodedPassword = encodedPassword;
        }

        @Override
        public Order loadDataFromNetwork() throws Exception {
            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/order/get?id=" + mOrderId;

            HttpEntity<String> entity = new HttpEntity<String>(null, getHeader(mUserName, mEncodedPassword));

            Order order = getRestTemplate().postForObject(service_url, entity, Order.class);
            return order;

        }
    }
}
