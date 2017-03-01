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
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.model.TripFilter;
import info.gogou.gogou.model.TripList;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.photo.PhotoUtils;

/**
 * Created by lxu on 2015-12-05.
 */
public class TripRequest {

    static HttpHeaders getMultipartFormHeader(String userName, String encodedPassword){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));

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

    public static class CreateTripRequest extends SpringAndroidSpiceRequest<GenericResponse> {

        private static final String TAG = "CreateTripRequest";

        private String mUserName;
        private String mEncodedPassword;
        private Trip mTrip;

        public CreateTripRequest(String userName, String encodedPassword, Trip trip)
        {
            super(GenericResponse.class);
            mUserName = userName;
            mEncodedPassword = encodedPassword;
            mTrip = trip;
        }

        @Override
        public GenericResponse loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/trip/create";

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

            RestTemplate restTemplate = getRestTemplate();
            FormHttpMessageConverter fc = new FormHttpMessageConverter();
            fc.addPartConverter(new MappingJacksonHttpMessageConverter());
            restTemplate.getMessageConverters().add(fc);

            HttpHeaders header = getHeader();
            header.setContentDispositionFormData("trip", null);

            HttpEntity<Trip> tripEntity = new HttpEntity<Trip>(mTrip, header);
            map.add("trip", tripEntity);

            List<String> imageFilePaths = mTrip.getPhotoPaths();

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

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mTrip);
            Log.d(TAG, "TripCreate json string: " + json);

            return (GenericResponse) response.getBody();
        }

        private MultiValueMap<String, ?> getMultiValueMap(Trip trip) {
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

            if (trip.getArrival() != null)
                map.add("arrival", trip.getArrival());
            if (trip.getDeparture() != null)
                map.add("departure", trip.getDeparture());
            if (trip.getDestination() != null)
                map.add("destination", trip.getDestination());
            if (trip.getOrigin() != null)
                map.add("origin", trip.getOrigin());
            if (trip.getDescription() != null)
                map.add("description", trip.getDescription());
            if (trip.getUserName() != null)
                map.add("userName", trip.getUserName());
            if (trip.getMaxheight() != null)
                map.add("maxheight", trip.getMaxheight());
            if (trip.getMaxlength() != null)
                map.add("maxlength", trip.getMaxlength());
            if (trip.getMaxwidth() != null)
                map.add("maxwidth", trip.getMaxwidth());
            if (trip.getMaxweight() != null)
                map.add("maxweight", trip.getMaxweight());
            return map;
        }
    }

    public static class GetTripListRequest extends SpringAndroidSpiceRequest<TripList> {

        private static final String TAG = "GetTripListRequest";

        private TripFilter mTripFilter;

        public GetTripListRequest(TripFilter tripFilter)
        {
            super(TripList.class);
            mTripFilter = tripFilter;
        }

        @Override
        public TripList loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/trip/list";

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(mTripFilter);
            Log.d(TAG, "TripFilter json string: " + json);

            HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

            ResponseEntity response = getRestTemplate().postForEntity(service_url, entity, TripList.class);

            return (TripList) response.getBody();
        }
    }


    public static class GetTripRequest extends SpringAndroidSpiceRequest<Trip> {

        private static final String TAG = "GetTripRequest";

        private Long mTripId;

        public GetTripRequest(long  tripId)
        {
            super(Trip.class);
            mTripId = tripId;
        }

        @Override
        public Trip loadDataFromNetwork() throws Exception {
            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/trip/get?id=" + mTripId;

            Trip trip = getRestTemplate().getForObject(service_url, Trip.class);
            return trip;

        }
    }
}

