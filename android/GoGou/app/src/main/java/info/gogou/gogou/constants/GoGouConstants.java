package info.gogou.gogou.constants;

import android.os.Environment;

/**
 * Created by lxu on 2015-12-01.
 */
public class GoGouConstants {
    public static final String INTERNAL_IP_ADDRESS = "http://192.168.0.103:8080";
    public static final String PUBLIC_IP_ADDRESS = "http://107.167.183.80";
    
    public static final String REST_SERVICES_URL = INTERNAL_IP_ADDRESS + "/gogou-web";

    public static final String SEED_VALUE = "I LOVE GOGOU";

    public static final String PREFS_NAME = "GoGouPrefs";
    public static final String KEY_SUBSCRIBER_ID = "subscriber_id";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PURCHASER_FLAG = "is_purchaser";
    public static final String KEY_USER_AGE = "user_age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_HEADIMAGE_PATH = "headimage_path";
    public static final String KEY_LOGIN_TYPE = "login_type";
    public static final String KEY_OAUTH2_TOKEN = "oauth2_token";
    public static final String KEY_OAUTH2_REFRESH_TOKEN = "oauth2_refresh_token";
    public static final String KEY_IM_TOKEN = "im_token";


    public static final String KEY_ORIGIN = "origin";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_IMAGE_NAME = "image_name";
    public static final String KEY_DEPART = "depart";
    public static final String KEY_DESTINATION = "destination";
    public static final String KEY_DEPARTDATE = "depart_date";
    public static final String KEY_ARRIVALDATE = "arrival_date";

    public static final String KEY_ORDER_TRAVELLER = "order_traveller";
    public static final String KEY_ORDER_BUYER = "order_buyer";
    public static final String KEY_ORDER_PRODUCT= "order_product";
    public static final String KEY_ORDER_PRODUCT_DETAIL = "order_detail";
    public static final String KEY_ORDER_STATUS = "order_status";
    public static final String KEY_ORDER_STATUS_EN = "order_status_en";
    public static final String KEY_ORDER_QUANTITY = "order_quantity";
    public static final String KEY_ORDER_MIN_PRICE = "order_min_price";
    public static final String KEY_ORDER_MAX_PRICE = "order_max_price";
    public static final String KEY_ORDER_PRICE = "order_price";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    /* Message definitions for GCM */
    public static final String GCM_SENDERID = "1053963223593";
    public static final String MSG_KEY_TO_SUBSCRIBER = "to_subscriber";
    public static final String MSG_KEY_FROM_SUBSCRIBER = "from_subscriber";
    public static final String MSG_KEY_CONTENT = "msg_content";
    public static final String MSG_KEY_ACTION = "action";

    public static final String MSG_ACTION_MESSAGE = "com.gogou.core.service.xmpp.MESSAGE";
    public static final String MSG_ACTION_ECHO = "com.gogou.core.service.xmpp.ECHO";
    public static final String MSG_ACTION_MESSAGE_ADD = "MSG_ADD";
    public static final int MSG_REGISTRATION = 0;
    public static final int MSG_TOPICS = 1;
    public static final int MSG_MESSAGE = 2;

    public static final int HEAD_IMAGE_WIDTH = 300;
    public static final int HEAD_IMAGE_HEIGHT = 300;
    public static final int IMAGE_WIDTH_PIXEL = 700;
    public static final int IMAGE_HEIGHT_PIXEL = 800;
    public static final int SELECTED_IMAGE_MAX_NUM = 9;

    public static final String[] TOPICS = {"global", "news"};

    public static final String WEIXIN_APP_ID = "wxe82cd9b01d2bc379";

    public static final String GOGOU_SDCARD_DIR = "android/data/info.gogou.gogou";

    public static final String GOGOU_IMAGE_DIR = Environment.getExternalStorageDirectory().getPath() +
            "/" + GOGOU_SDCARD_DIR + "/images/";
    public static final String GOGOU_FILE_DIR = Environment.getExternalStorageDirectory().getPath() +
            "/" + GOGOU_SDCARD_DIR + "/files/";
    public static final String GOGOU_AUDIO_DIR = Environment.getExternalStorageDirectory().getPath() +
            "/" + GOGOU_SDCARD_DIR + "/audios/";

    public static final String HEADIMAGE_NAME = "headimage";
}
