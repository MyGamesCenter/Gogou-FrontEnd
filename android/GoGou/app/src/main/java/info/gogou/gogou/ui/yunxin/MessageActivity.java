//package info.gogou.gogou.ui.chat;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.netease.nim.uikit.NimUIKit;
//import com.netease.nim.uikit.cache.DataCacheManager;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.Observer;
//import com.netease.nimlib.sdk.RequestCallback;
//import com.netease.nimlib.sdk.auth.AuthService;
//import com.netease.nimlib.sdk.auth.LoginInfo;
//import com.netease.nimlib.sdk.friend.FriendService;
//import com.netease.nimlib.sdk.friend.constant.VerifyType;
//import com.netease.nimlib.sdk.friend.model.AddFriendData;
//import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
//import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
//import com.netease.nimlib.sdk.msg.model.IMMessage;
//
//import java.util.List;
//
//import info.gogou.gogou.chat.main.MyCache;
//import info.gogou.gogou.constants.GoGouIntents;
//
//public class MessageActivity extends AppCompatActivity{
//
//    private static final String TAG = MessageActivity.class.getSimpleName();
//
//    private String mChatTo;
//    private String mChatFrom;
//
//    private String mChatToken;
//    private String mChatWindowType;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Intent intent = this.getIntent();
//        mChatTo = intent.getStringExtra(GoGouIntents.CHAT_TO);
//        mChatFrom = intent.getStringExtra(GoGouIntents.CHAT_FROM);
//
//        mChatToken = intent.getStringExtra(GoGouIntents.CHAT_TOKEN);
//        mChatWindowType = intent.getStringExtra(GoGouIntents.CHAT_WINDOW_TYPE);
//
//        yunXinLogin(mChatFrom, mChatToken);
//
////        NIMClient.getService(MsgServiceObserve.class)
////                .observeReceiveMessage(incomingMessageObserver, true);
//
//    }
//
//
//    private void yunXinLogin(final String mAccid, final String mToken) {
//        LoginInfo info = new LoginInfo(mAccid, mToken); // config...
//        RequestCallback<LoginInfo> callback =
//                new RequestCallback<LoginInfo>() {
//                    @Override
//                    public void onSuccess(LoginInfo loginInfo) {
//                        MyCache.setAccount(mAccid);
//                        // 构建缓存
//                        DataCacheManager.buildDataCacheAsync();
//
//                        if (mChatWindowType.equals("P2P")){
//                            startP2PSession();
//                        }else if (mChatWindowType.equals("RECENT_CONTRACT")){
//                            openRecentContact();
//                        }else if (mChatWindowType.equals("FRIENDS")){
//                            startContactActivity();
//                        }else if (mChatWindowType.equals("ADD_FRIEND")){
//                            addFriend();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(int i) {
//                        Toast.makeText(MessageActivity.this,
//                                "Message service failed", Toast.LENGTH_LONG);
//                        finish();
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//                        Toast.makeText(MessageActivity.this,
//                                "Message service failed", Toast.LENGTH_LONG);
//                        finish();
//                    }
//                };
//        NIMClient.getService(AuthService.class).login(info)
//                .setCallback(callback);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        NIMClient.getService(MsgServiceObserve.class)
////                .observeReceiveMessage(incomingMessageObserver, false);
//    }
//
//    Observer<List<IMMessage>> incomingMessageObserver =
//            new Observer<List<IMMessage>>() {
//                @Override
//                public void onEvent(List<IMMessage> messages) {
//                    // 处理新收到的消息，为了上传处理方便，SDK保证参数messages全部来自同一个聊天对象。
//                    for (IMMessage message : messages) {
//                        if (message.getMsgType() == MsgTypeEnum.image) {
//                            //setImage(message);
//                        } else {
//                            //text.setText(message.getContent());
//                        }
//                    }
//                }
//            };
//
//    /**
//     * 注销按钮响应事件
//     */
//    private void logout() {
//        NIMClient.getService(AuthService.class).logout();
//        MyCache.clear();
//        NimUIKit.clearCache();
//        finish();
//    }
//
//    /**
//     * 调用uikit p2p会话接口
//     */
//    private void startP2PSession() {
//        NimUIKit.startChatting(this, mChatTo, SessionTypeEnum.P2P, null);
//    }
//
//    /**
//     * 打开通讯录
//     */
//    private void startContactActivity() {
//        startActivity(new Intent(MessageActivity.this, ContactActivity.class));
//    }
//
//    /**
//     * 添加好友
//     */
//    private void addFriend() {
//        if (NIMClient.getService(FriendService.class).isMyFriend(mChatTo)) {
//            //Toast.makeText(MessageActivity.this, "已经是好友", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        VerifyType verifyType = VerifyType.DIRECT_ADD;
//        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(mChatTo, verifyType, ""))
//                .setCallback(new RequestCallback<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        //Toast.makeText(MessageActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onFailed(int i) {
//                        Log.d(TAG, "add friend failed code:" + i);
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//
//                    }
//                });
//    }
//
//    /**
//     * 打开最近联系人列表
//     */
//    private void openRecentContact() {
//        startActivity(new Intent(MessageActivity.this, RecentContactActivity.class));
//    }
//
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        this.finish();
//    }
//}
