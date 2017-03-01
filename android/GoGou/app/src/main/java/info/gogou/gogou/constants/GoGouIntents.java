package info.gogou.gogou.constants;

import android.app.Activity;
import android.content.Intent;

import info.gogou.gogou.ui.MainActivity;

/**
 * Created by lxu on 2015-11-30.
 */
public class GoGouIntents {

    public static final String LOGIN_SUCCEED_FLAG = "login_succeed_flag";
    public static final String LOGIN_USER_NAME = "login_user_name";
    public static final String UNIQUE_ID = "unique_id";
    public static final String DEMAND = "demand";
    public static final String DEMAND_ID = "demand_id";
    public static final String FLAG = "flag";
    public static final String TRIP_FLAG = "trip_flag";
    public static final String DEMAND_FLAG = "demand_flag";
    public static final String TRIP = "trip";
    public static final String TRIP_ID = "trip_id";
    public static final String ORDER = "order";
    public static final String CONFIRM_ORDER = "confirm_order";
    public static final String LIST_ROW_ID = "row_id";
    public static final String NEED_GET_TOKEN_FLAG = "need_get_token_flag";
    public static final String CHAT_FROM = "chat_from";
    public static final String CHAT_TO = "chat_to";
    public static final String CHAT_MESSAGE = "chat_message";
    public static final String CHAT_WINDOW_TYPE = "P2P";
    public static final String CHAT_TOKEN = "chat_token";
    public static final String WECHAT_CODE = "wechat_code";

    // images
    public static final String IMAGE_POS = "image_position";
    public static final String IMAGE_SELECTED_LIST = "image_selected_list";
    public static final String IMAGE_SELECTED_NUM = "image_selected_num";
    public static final String IMAGE_MAXNUM_AVAILABLE = "image_maxnum_available";
    public static final String IMAGE_UNSELECTED_LIST = "image_unselected_list";
    public static final int SELECT_IMAGE_REQUEST = 0x01;
    public static final int TAKE_PHOTO_REQUEST = 0x02;
    public static final int PREVIEW_IMAGES_REQUEST = 0x03;


    public static void goBack2MainScreen(String uId, Activity currentActivity, boolean finish)
    {
        Intent intent = new Intent(currentActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, uId);
        currentActivity.startActivity(intent);
        if (finish)
            currentActivity.finish();
    }

}
