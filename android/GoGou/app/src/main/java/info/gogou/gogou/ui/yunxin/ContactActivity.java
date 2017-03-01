//package info.gogou.gogou.ui.chat;
//
//import android.os.Bundle;
//
//import com.netease.nim.uikit.common.activity.TActionBarActivity;
//import com.netease.nim.uikit.contact.ContactsFragment;
//
//import info.gogou.gogou.R;
//
///**
// * Created by hzxuwen on 2015/12/7.
// */
//public class ContactActivity extends TActionBarActivity {
//    private ContactsFragment fragment;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.contacts_list);
//
//        // 集成通讯录页面
//        addContactFragment();
//    }
//
//    // 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
//    private void addContactFragment() {
//        fragment = new ContactsFragment();
//        fragment.setContainerId(R.id.contact_fragment);
//
//
//        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
//        fragment = (ContactsFragment) addFragment(fragment);
//    }
//}
