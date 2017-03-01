package info.gogou.gogou.request;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.List;

import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Demand;
import info.gogou.gogou.model.DemandFilter;
import info.gogou.gogou.model.DemandList;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.photo.PhotoUtils;

/**
 * Created by lxu on 2015-12-05.
 */
public class DemandRequest {

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

    static HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);
        return headers;
    }

    public static class CreateDemandRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreateDemandRequest";

        private String mUserName;
        private String mEncodedPassword;
        private Demand mDemand;

        public CreateDemandRequest(String userName, String encodedPassword, Demand demand)
        {
            super(GenericResponse.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mDemand = demand;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/demand/create";

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

            RestTemplate restTemplate = getRestTemplate();
            FormHttpMessageConverter fc = new FormHttpMessageConverter();
            fc.addPartConverter(new MappingJacksonHttpMessageConverter());
            restTemplate.getMessageConverters().add(fc);

            HttpHeaders header = getHeader();
            header.setContentDispositionFormData("demand", null);

            HttpEntity<Demand> demandEntity = new HttpEntity<Demand>(mDemand, header);
            map.add("demand", demandEntity);

            List<String> imageFilePaths = mDemand.getPhotoPaths();

            for(String imageFilePath : imageFilePaths)
            {
                Bitmap bitmap = CacheManager.getBitmapFromMemCache(imageFilePath);
                if (bitmap == null) {
                    bitmap = PhotoUtils.resizeImage(imageFilePath,
                            PhotoUtils.DEFAULT_GOGOU_GRID_IMAGE_WIDTH,
                            PhotoUtils.DEFAULT_GOGOU_GRID_IMAGE_HEIGHT);
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Resource resource = new ByteArrayFilenameResource(byteArray, imageFilePath);
                map.add("images", resource);
            }

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, getMultipartFormHeader(mUserName, mEncodedPassword));

            ResponseEntity response = restTemplate.postForEntity(service_url, entity, GenericResponse.class);

            return (GenericResponse) response.getBody();
        }

//        private MultiValueMap<String, ?> getMultiValueMap(Demand demand) {
//            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//
//            if (demand.getProductname() != null)
//                map.add("productname", demand.getProductname());
//            if (demand.getQuantity() != null)
//                map.add("quantity", String.valueOf(demand.getQuantity()));
//            if (demand.getBrand() != null)
//                map.add("brand", demand.getBrand());
//            if (demand.getDescription() != null)
//                map.add("description", demand.getDescription());
//            if (demand.getDestination() != null)
//                map.add("destination", demand.getDestination());
//            if (demand.getOrigin() != null)
//                map.add("origin", demand.getOrigin());
//            if (demand.getServiceFee() != null)
//                map.add("serviceFee", String.valueOf(demand.getServiceFee()));
//            if (demand.getUserName() != null)
//                map.add("userName", demand.getUserName());
//            if (demand.getExpectTime() != null)
//                map.add("expectTime", demand.getExpectTime());
//            if (demand.getCategoryName() != null)
//                map.add("categoryName", demand.getCategoryName());
//            if (demand.getLanguageCode() != null)
//                map.add("languageCode", demand.getLanguageCode());
//
//            return map;
//        }

    }

    public static class GetDemandListRequest extends SpringAndroidSpiceRequest<DemandList> {

        private static final String TAG = "GetDemandListRequest";

        private DemandFilter mDemandFilter;

        public GetDemandListRequest(DemandFilter demandFilter)
        {
            super(DemandList.class);
            mDemandFilter = demandFilter;
        }

        @Override
        public DemandList loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/demand/list";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mDemandFilter);
            Log.d(TAG, "DemandFilter json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, DemandList.class);

            return (DemandList) response.getBody();
        }
    }


    public static class GetDemandRequest extends SpringAndroidSpiceRequest<Demand> {

        private static final String TAG = "GetDemandRequest";

        private Long mDemandId;

        public GetDemandRequest(long  demandId)
        {
            super(Demand.class);
            mDemandId = demandId;
        }

        @Override
        public Demand loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/demand/get?id=" + mDemandId;

            Demand demand = getRestTemplate().getForObject(service_url, Demand.class);

            return demand;

        }
    }
}
