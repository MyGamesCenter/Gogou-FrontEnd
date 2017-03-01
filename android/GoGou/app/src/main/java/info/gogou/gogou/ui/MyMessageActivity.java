package info.gogou.gogou.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.dao.FriendDao;
import info.gogou.gogou.model.Friend;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


public class MyMessageActivity extends RoboActionBarActivity {

    private static final String TAG = "MyMessageActivity";

	@InjectView(R.id.my_message_listView)
	private ListView mMyMsgListView;

	@InjectView(R.id.my_message_back_Button)
	private Button mBackBtn;

    List<Friend> mMyFriends = new ArrayList<Friend>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_message_layout);

        Intent intent = getIntent();
        final String myName = intent.getStringExtra(GoGouIntents.LOGIN_USER_NAME);

        FriendDao friendDao = new FriendDao(MyMessageActivity.this);
        friendDao.open();
        mMyFriends = friendDao.listAllFriends();
        friendDao.close();

        MyMessageAdapter msgAdapter = new MyMessageAdapter(MyMessageActivity.this, mMyFriends);
        mMyMsgListView.setAdapter(msgAdapter);

        mMyMsgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MyMessageActivity.this, ChatActivity.class);
                intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
                intent.putExtra(GoGouIntents.CHAT_FROM, myName);
                intent.putExtra(GoGouIntents.CHAT_TO, mMyFriends.get(position).getUserName());
				startActivity(intent);
			}
		});

		//返回按钮
        mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, MyMessageActivity.this, false);
            }
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

    private  class MyMessageAdapter extends BaseAdapter {
        private Activity mActivity;
        private List<Friend> mFriends;
        private LayoutInflater mInflater=null;
        //public ImageLoader imageLoader;

        public MyMessageAdapter(Activity a, List<Friend> friends) {
            mActivity = a;
            mFriends = friends;
            mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //imageLoader=new ImageLoader(activity.getApplicationContext());
        }

        @Override
        public int getCount() {
            return mFriends.size();
        }

        @Override
        public Object getItem(int position) {
            return mFriends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = mInflater.inflate(R.layout.my_message_item_listview, null);

            TextView nameTV = (TextView)vi.findViewById(R.id.user_name_text); // friend name
            ImageView avatar = (ImageView)vi.findViewById(R.id.user_image_view); // friend avatar
            Friend friend = mFriends.get(position);

            nameTV.setText(friend.getUserName());

            return vi;
        }
    }

}