package info.gogou.gogou.request;

import android.util.Base64;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by lxu on 2015-12-05.
 */
public class CategoryRequest {

    static HttpHeaders getHeader(String userName, String encodedPassword){
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
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

    public static class GetCategoryListRequest extends SpringAndroidSpiceRequest<CategoryList> {

        private static final String TAG = "GetCategoryListRequest";

        private String mLanguageCode;

        public GetCategoryListRequest(String languageCode)
        {
            super(CategoryList.class);
            mLanguageCode = languageCode;
        }

        @Override
        public CategoryList loadDataFromNetwork() throws Exception {

            String service_url = GoGouConstants.REST_SERVICES_URL + "/services/category/list?languageCode=" + mLanguageCode;

            CategoryList categoryList = getRestTemplate().getForObject(service_url, CategoryList.class);

            return categoryList;
        }
    }
}


