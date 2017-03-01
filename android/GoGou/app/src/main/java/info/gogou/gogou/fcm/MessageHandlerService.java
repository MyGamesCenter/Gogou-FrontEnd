package info.gogou.gogou.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.model.OrderStatus;
import info.gogou.gogou.ui.MainActivity;
import info.gogou.gogou.ui.MyOrderActivity;
import info.gogou.gogou.ui.MyReceivedOrderActivity;

import static info.gogou.gogou.constants.GoGouConstants.MSG_MESSAGE;
import static info.gogou.gogou.constants.GoGouConstants.MSG_REGISTRATION;
import static info.gogou.gogou.constants.GoGouConstants.MSG_TOPICS;
import static info.gogou.gogou.constants.GoGouConstants.TOPICS;

/**
 * Created by lxu on 2016-01-10.
 */
public class MessageHandlerService extends Service {

    private static final String TAG = "MessageHandlerService";

    private static MessageHandlerService mInstance = null;

    private MessageHandler mHandler;

    public static MessageHandlerService getInstance(){
        return mInstance;
    }

    private void initialize() {
        if (mInstance != null) {
            Log.d(TAG, "initialize, already initialized, do nothing.");
            return;
        }

        mInstance = this;
        HandlerThread thread = new HandlerThread("MessageHandler Thread", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mHandler = new MessageHandler(thread.getLooper());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Message handler service is starting...");
        processIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void processIntent(Intent intent)
    {
        if (intent == null)
            return;

        boolean isNeeded = intent.getBooleanExtra(GoGouIntents.NEED_GET_TOKEN_FLAG, false);
        if (isNeeded) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    try {
                        // Get updated InstanceID token.
                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        Log.d(TAG, "Refreshed token when service starts: " + refreshedToken);
                        return refreshedToken;
                    } catch (Exception e) {
                        Log.d(TAG, "Failed to get token", e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    Log.d(TAG, "GCM Registration Token on service start : " + result);
                    // save subscriber's token to preferences
                    GogouApplication.getInstance().setGcmToken(result);
                    subscribeTopics();
                }
            }.execute();
        } else {
            subscribeTopics();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * message handler looper to handle all the msg sent to the service
     */
    final class MessageHandler extends Handler {

        public MessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            processMessage(msg);
        }
    }

    /**
     * the main message process loop.
     */
    private void processMessage(android.os.Message msg) {

        switch (msg.what) {
            case MSG_REGISTRATION:
                onRegistration();
                break;
            case MSG_TOPICS:
                onTopics(msg);
                break;
            case MSG_MESSAGE:
                onDataMessage(msg);
                break;
            default:
                break;
        }
    }

    /**************** registration ****************/
    private void onRegistration() {
        try {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token onTokenRefresh: " + refreshedToken);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);

            // Subscribe to topic channels
            subscribeTopics();

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            GogouApplication.getInstance().setTokenSent(true);
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            GogouApplication.getInstance().setTokenSent(false);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(GoGouConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics() {
        FirebaseMessaging instance = FirebaseMessaging.getInstance();
        for (String topic : TOPICS) {
            instance.subscribeToTopic(topic);
        }
    }
    /**************** end of registration ****************/

    /**************** received topics ****************/
    private void onTopics(Message msg)
    {
        Bundle data = msg.getData();
        String message = data.getString(MessagePayload.KEY_MESSAGE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        sendNotification(intent, message);
    }

    /**************** received data message ****************/
    private void onDataMessage(Message msg)
    {
        Bundle data = msg.getData();
        String category = data.getString(MessagePayload.KEY_CATEGORY);
        String message = data.getString(MessagePayload.KEY_MESSAGE);
        if (category.equals(MessagePayload.Category.ORDER.getValue())) {
            handleCategoryOrder(message);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            sendNotification(intent, message);
        }
    }

    void handleCategoryOrder(String message) {
        Class<?> activityClass = null;
        String notification = message;
        if (message.equals(OrderStatus.PREORDERED.getValue())) {
            notification = getResources().getString(R.string.notif_order_created);
            activityClass = MyReceivedOrderActivity.class;
        }
        else if (message.equals(OrderStatus.CONFIRMED.getValue())) {
            notification = getResources().getString(R.string.notif_order_confirmed);
            activityClass = MyOrderActivity.class;
        }
        else if (message.equals(OrderStatus.ORDERED.getValue())) {
            notification = getResources().getString(R.string.notif_order_paid);
            activityClass = MyReceivedOrderActivity.class;
        }
        else if (message.equals(OrderStatus.DELIVERED.getValue())) {
            notification = getResources().getString(R.string.notif_order_delivered);
            activityClass = MyOrderActivity.class;
        }
        else if (message.equals(OrderStatus.COLLECTED.getValue())) {
            notification = getResources().getString(R.string.notif_order_collected);
            activityClass = MyReceivedOrderActivity.class;
        }
        else if (message.equals(OrderStatus.REFUNDED.getValue())) {
            notification = getResources().getString(R.string.notif_order_refunded);
            activityClass = MyOrderActivity.class;
        }
        else {
            activityClass = MainActivity.class;
        }

        Intent intent = new Intent(this, activityClass);
        sendNotification(intent, notification);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(Intent intent, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_28x28)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    /**************** end of received topics ****************/

    /**************** received chat message ****************/
    private void onChatMessages(Message msg)
    {
        /*Log.d(TAG, "Chat message received: " + msg);
        Bundle data = msg.getData();
        Intent chatIntent = new Intent(GoGouConstants.MSG_ACTION_MESSAGE);
        chatIntent.putExtra(GoGouIntents.CHAT_MESSAGE, data);
        sendBroadcast(chatIntent);
        if (!isAppOnForeground(this))
        {
            sendNotification(chatIntent, data.getString(GoGouConstants.MSG_KEY_CONTENT));
        }*/
    }

    /**************** end of chat message ****************/

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
