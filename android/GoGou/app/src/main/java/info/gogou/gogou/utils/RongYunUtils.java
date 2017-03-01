package info.gogou.gogou.utils;

import android.content.Context;
import android.util.Log;

import info.gogou.gogou.listeners.RESTRequestListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lxu on 2016-06-04.
 */
public class RongYunUtils {

    private static final String TAG = "RongYunUtils";

    public static void rongYunIMconnect(Context context, String token, final RESTRequestListener<String> listener) {

        if (context.getApplicationInfo().packageName.equals(SystemUtil.getProcessName(context))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onTokenIncorrect() {
                    Log.e(TAG, "Token is incorrect.!");
                    listener.onGogouRESTRequestFailure();
                }

                /**
                 * 连接融云成功
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d(TAG, "Manage to connect rongyun with user id: " + userid);
                    listener.onGogouRESTRequestSuccess(userid);
                }

                /**
                 * 连接融云失败
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e(TAG, "Error in connecting rongyun with error code: " + errorCode);
                    listener.onGogouRESTRequestFailure();
                }
            });
        }
    }

    public static void updateRongYunUserInfo(UserInfo userInfo)
    {
        RongIM.getInstance().setCurrentUserInfo(userInfo);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
    }
}
