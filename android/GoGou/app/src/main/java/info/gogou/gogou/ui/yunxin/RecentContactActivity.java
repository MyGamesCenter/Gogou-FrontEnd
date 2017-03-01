//package info.gogou.gogou.ui.chat;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.netease.nim.uikit.common.activity.TActionBarActivity;
//import com.netease.nim.uikit.recent.RecentContactsFragment;
//
//import info.gogou.gogou.R;
//
///**
// * Created by hzxuwen on 2015/12/8.
// */
//public class RecentContactActivity extends TActionBarActivity {
//    private RecentContactsFragment contactsFragment;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recent_contact_activity);
//
//        Button backBtn = (Button)findViewById(R.id.recentContactBackButton);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        addRecentContactsFragment();
//    }
//
//    // 将最近联系人列表fragment动态集成进来。开发者也可以使用在xml中配置的方式静态集成。
//    private void addRecentContactsFragment() {
//        contactsFragment = new RecentContactsFragment();
//
//        // 设置要集成联系人列表fragment的布局文件
//        contactsFragment.setContainerId(R.id.messages_fragment);
//
//        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
//        contactsFragment = (RecentContactsFragment) addFragment(contactsFragment);
//    }
//}
