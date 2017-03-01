//package info.gogou.gogou.ui.chat;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//
//import com.netease.nim.uikit.cache.DataCacheManager;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.RequestCallback;
//import com.netease.nimlib.sdk.auth.AuthService;
//import com.netease.nimlib.sdk.auth.LoginInfo;
//
//import info.gogou.gogou.chat.main.MyCache;
//import info.gogou.gogou.constants.GoGouConstants;
//
//public class LoginActivity extends ActionBarActivity {
//    //private EditText accountEdit;
//    //private EditText pswEdit;
//    //private Button loginBtn;
//
//
//
//    private String mAccid = null;
//    private String mToken = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        NIMClient.init(getApplicationContext(), null, null);
//        //setContentView(R.layout.login_activity);
//        //findViews();
//
//        login();
//    }
//
//    private void findViews() {
//        //accountEdit = (EditText) findViewById(R.id.account_edit);
//        //pswEdit = (EditText) findViewById(R.id.token_edit);
//        //loginBtn = (Button) findViewById(R.id.login);
////        loginBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                login();
////            }
////        });
//    }
//
//    /**
//     * 登录事件响应函数
//     */
//    private void login() {
//
//
//        SharedPreferences settings = getSharedPreferences(GoGouConstants.PREFS_NAME, MODE_PRIVATE);
//        mAccid = settings.getString(GoGouConstants.KEY_SUBSCRIBER_ID, null);
//        mToken = settings.getString(GoGouConstants.KEY_YUNXIN_TOKEN, null);
//
//        LoginInfo info = new LoginInfo(mAccid, mToken); // config...
//        RequestCallback<LoginInfo> callback =
//                new RequestCallback<LoginInfo>() {
//                    @Override
//                    public void onSuccess(LoginInfo loginInfo) {
//                        MyCache.setAccount(mAccid);
//                        // 构建缓存
//                        DataCacheManager.buildDataCacheAsync();
//                        startActivity(new Intent(LoginActivity.this, MessageActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailed(int i) {
//
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//
//                    }
//                    // overwrite methods
//                };
//        NIMClient.getService(AuthService.class).login(info)
//                .setCallback(callback);
//    }
//}
