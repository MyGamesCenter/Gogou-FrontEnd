package info.gogou.gogou.ui.rongyun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.dao.FriendDao;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.RongYunUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Created by Bob on 15/8/18.
 * 会话页面
 */
public class ConversationActivity extends RoboActionBarActivity {

    private static final String TAG = "ConversationActivity";

    @InjectView(R.id.toUsernameText)
    private TextView mToUsername;
    @InjectView(R.id.chatBackBtn)
    private Button mBack;

    private String mTargetId;
    private String mTitle;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        Intent intent = getIntent();
        getIntentInfo(intent);
        saveAsFriend();

        mToUsername.setText(mTitle);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        keepConnected(intent);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentInfo(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        mTitle = intent.getData().getQueryParameter("title");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
    }

    private void saveAsFriend() {
        FriendDao friendDao = new FriendDao(this);
        friendDao.open();

        Subscriber subscriber = new Subscriber();
        subscriber.setId(Long.valueOf(mTargetId));
        subscriber.setEmailAddress("fake@fake.com");
        subscriber.setUserName(mTitle);
        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format2));
        String date = df.format(new Date());
        if (!friendDao.isFriend(subscriber))
            friendDao.addFriend(subscriber, date);
        friendDao.close();
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param conversationType
     * @param targetId
     * @param title
     */
    private void enterFragment(Conversation.ConversationType conversationType, String targetId, String title) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId)
                .appendQueryParameter("title", title)
                .build();

        fragment.setUri(uri);
    }

    private void keepConnected(Intent intent) {
        String token = GogouApplication.getInstance().getSubscriber().getImToken();

        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                RongYunUtils.rongYunIMconnect(this, token, new RESTRequestListener<String>() {
                    @Override
                    public void onGogouRESTRequestFailure() {

                    }

                    @Override
                    public void onGogouRESTRequestSuccess(String s) {
                        enterFragment(mConversationType, mTargetId, mTitle);
                    }
                });
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    RongYunUtils.rongYunIMconnect(this, token, new RESTRequestListener<String>() {
                        @Override
                        public void onGogouRESTRequestFailure() {

                        }

                        @Override
                        public void onGogouRESTRequestSuccess(String s) {
                            enterFragment(mConversationType, mTargetId, mTitle);
                        }
                    });
                } else {
                    enterFragment(mConversationType, mTargetId, mTitle);
                }
            }
        }
    }
}
