package info.gogou.gogou.ui.rongyun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.dao.FriendDao;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Friend;
import info.gogou.gogou.utils.RongYunUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Created by Bob on 15/8/18.
 * 会话列表
 */
public class ConversationListActivity extends RoboActionBarActivity implements RongIM.UserInfoProvider {

    @InjectView(R.id.recentContactBackButton)
    private Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_contact_activity);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        keepConnected();
    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {

        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }

    private void keepConnected() {

        Intent intent = getIntent();
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
                        enterFragment();
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
                            enterFragment();
                        }
                    });
                } else {
                    enterFragment();
                }
            }
        }
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        FriendDao friendDao = new FriendDao(this);
        friendDao.open();

        List<Friend> friends = friendDao.listAllFriends();

        if (friends != null && friends.size() > 0) {
            for (Friend friend : friends) {
                if (String.valueOf(friend.getId()).equals(userId)) {
                    //RongIM.setUserInfoProvider(this, false);
                    return new UserInfo(userId, friend.getUserName(), null);
                }
            }
        }
        return null;
    }
}
