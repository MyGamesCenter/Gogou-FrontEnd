package info.gogou.gogou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.constants.LoginType;
import info.gogou.gogou.dao.CategoryDao;
import info.gogou.gogou.dao.FriendDao;
import info.gogou.gogou.fcm.MessageHandlerService;
import info.gogou.gogou.fcm.MessagePayload;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.model.OAuth2Login;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.request.CategoryRequest;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import info.gogou.gogou.utils.photo.PhotoUtils;
import roboguice.activity.RoboActivity;

public class GogouSplash extends RoboActivity {

    private static final String TAG = "GogouSplash";

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gogou_splash);
        if (!GogouApplication.getInstance().getSpiceManager().isStarted())
            GogouApplication.getInstance().getSpiceManager().start(getApplicationContext());

        if (checkPlayServices()) {
            // Start MessageHandlerService to communicate with GCM.
            // If there is no token saved, then request one and save.
            String token = GogouApplication.getInstance().getGcmToken();
            Intent intent = new Intent(this, MessageHandlerService.class);
            if (token == null) {
                intent.putExtra(GoGouIntents.NEED_GET_TOKEN_FLAG, true);
            }
            startService(intent);
        }
    }

    @Override
    public void onStart() {
        performCategoryListRequest();
        //RESTRequestUtils.performYunXinRequest(null);

        super.onStart();
    }

    private void performCategoryListRequest() {

        CategoryRequest.GetCategoryListRequest request = new CategoryRequest.GetCategoryListRequest(getResources().getString(R.string.language_code));
        GogouApplication.getInstance().getSpiceManager().execute(request, new GetCategoryListRequestListener());
    }

    private class GetCategoryListRequestListener implements
            RequestListener<CategoryList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d(TAG, "Error during category list request: " + e.getLocalizedMessage());
            performOauth2RefreshToken();
        }

        @Override
        public void onRequestSuccess(CategoryList categoryList) {

            if (categoryList == null) {
                Log.d(TAG, "Error during category list request.");
            } else {
                /*try {
                    Thread.sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                }*/
                Log.d(TAG, "There are " + categoryList.size() + " categories.");
                CategoryDao categoryDao = new CategoryDao(GogouSplash.this);
                categoryDao.open();
                categoryDao.deleteCategories(getResources().getString(R.string.language_code));
                // save category list to db
                for (Category category : categoryList)
                {
                    categoryDao.saveCategory(category);
                }
                categoryDao.close();
                // save category list to cache mananger
                CacheManager.saveAsLocalCategory(categoryList);
            }
            performOauth2RefreshToken();
        }
    }

    private void performOauth2RefreshToken() {
        String loginType = GogouApplication.getInstance().getLoginType();
        Log.d(TAG, "Login type is: " + loginType);
        if (LoginType.WECHAT.getValue().equals(loginType)) {
            final String accessToken = GogouApplication.getInstance().getOauth2AccessToken();
            RESTRequestUtils.performOAuth2CheckTokenRequest(accessToken, new RESTRequestListener<GenericResponse>() {
                @Override
                public void onGogouRESTRequestFailure() {
                    enter();
                }

                @Override
                public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                    if (genericResponse.getErrorType() == ErrorType.NONE) {
                        Log.d(TAG, "Oauth2 access token: " + accessToken + " is valid");
                    } else {
                        Log.d(TAG, "Oauth2 access token: " + accessToken + " is not valid");
                        String refreshToken = GogouApplication.getInstance().getOauth2RefreshToken();
                        RESTRequestUtils.performOauth2RefreshTokenRequest(refreshToken, new RESTRequestListener<OAuth2Login>() {
                            @Override
                            public void onGogouRESTRequestFailure() {
                                enter();
                            }

                            @Override
                            public void onGogouRESTRequestSuccess(OAuth2Login oAuth2Login) {
                                if (oAuth2Login.getErrcode() == null) {

                                    Subscriber subscriber = new Subscriber();
                                    subscriber.setUserName(oAuth2Login.getNickname());
                                    subscriber.setEmailAddress("");
                                    subscriber.setGender(oAuth2Login.getGender());
                                    subscriber.setEncodedPassword(oAuth2Login.getAccessToken());

                                    // save image from oAuth2Login
                                    String imagePath = PhotoUtils.saveBitmap(PhotoUtils.resizeImage(oAuth2Login.getFileOutput(),
                                            GoGouConstants.HEAD_IMAGE_WIDTH,
                                            GoGouConstants.HEAD_IMAGE_HEIGHT),
                                            GoGouConstants.HEADIMAGE_NAME);
                                    if (imagePath != null) {
                                        subscriber.setHeadImage(imagePath);
                                        Log.d(TAG, "Saved head image path: " + imagePath);
                                    }

                                    // save subscriber's information to preferences
                                    GogouApplication.getInstance().updateSubscriber(subscriber);
                                    GogouApplication.getInstance().setOauth2AccessToken(oAuth2Login.getAccessToken());
                                    GogouApplication.getInstance().setOauth2RefreshToken(oAuth2Login.getRefreshToken());
                                    Log.d(TAG, "access token is updated: " + oAuth2Login.getAccessToken());
                                }
                            }
                        });
                    }
                    enter();
                }
            });
        } else {
            enter();
        }

    }

    private void enter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GoGouIntents.goBack2MainScreen(TAG, GogouSplash.this, true);
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}