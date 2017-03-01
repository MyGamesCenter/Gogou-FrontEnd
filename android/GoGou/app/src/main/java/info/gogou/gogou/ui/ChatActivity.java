package info.gogou.gogou.ui;

import roboguice.activity.RoboActionBarActivity;

/**
 * Created by lxu on 2016-01-16.
 */
public class ChatActivity extends RoboActionBarActivity {

    /*private static final String TAG = "ChatActivity";

    @InjectView(R.id.chatBackBtn)
    private Button mBackBtn;
    @InjectView(R.id.toUsernameText)
    private TextView mToUsernameTv;
    @InjectView(R.id.input_message_edit)
    private EditText mChatEditBox;
    @InjectView(R.id.add_message_button)
    private Button mSendMessageBtn;
    @InjectView(R.id.chat_message_listView)
    private ListView mChatListView;

    private String mFromUsername;
    private String mToUsername;
    private Class<?> mPreActivity;
    private int mDisplayWidth = 0;
    private ChatMsgViewAdapter mMsgViewAdapter;
    private List<Chat> mChatMsgs = new ArrayList<Chat>();
    private ChatDao mChatDao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);

        Intent intent = this.getIntent();
        String uniqueId = intent.getStringExtra(GoGouIntents.UNIQUE_ID);
        if ("DemandDetailActivity".equals(uniqueId))
        {
            mPreActivity = RequirementDetailActivity.class;
        }
        else if ("ItiDetailActivity".equals(uniqueId))
        {
            mPreActivity = ScheduleDetailActivity.class;
        }
        mToUsername = intent.getStringExtra(GoGouIntents.CHAT_TO);
        mFromUsername = intent.getStringExtra(GoGouIntents.CHAT_FROM);
        mToUsernameTv.setText(mToUsername);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, ChatActivity.this, true);
            }
        });

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mChatEditBox.getText().toString().trim();
                DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format2));
                String date = df.format(new Date());
                Chat chatEntry = new Chat();
                chatEntry.setFrom(mFromUsername);
                chatEntry.setTo(mToUsername);
                chatEntry.setLayoutId(R.layout.list_say_me_item);
                chatEntry.setPostDate(date);
                chatEntry.setContent(str);
                mChatDao.postChat(chatEntry);
                mChatEditBox.setText("");
                mChatMsgs.add(chatEntry);
                mMsgViewAdapter.notifyDataSetChanged();
                mChatListView.setSelection(mChatListView.getCount());
                sendChatMessageToGcm(chatEntry);
            }
        });

        //获取手机屏幕像素
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDisplayWidth = dm.widthPixels;

        loadExistingChats();

        FriendDao friendDao = new FriendDao(ChatActivity.this);
        friendDao.open();
        // TODO (lxu) retrieve from back-end
        Subscriber subscriber = new Subscriber();
        subscriber.setEmailAddress("fake@fake.com");
        subscriber.setUserName(mToUsername);
        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format2));
        String date = df.format(new Date());
        if (!friendDao.isFriend(subscriber))
            friendDao.addFriend(subscriber, date);
        friendDao.close();
        registerReceiver(broadcastReceiver, new IntentFilter(GoGouConstants.MSG_ACTION_MESSAGE_ADD));

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle data = intent.getBundleExtra(GoGouIntents.CHAT_MESSAGE);
            Chat chatEntry = new Chat();
            chatEntry.setFrom(data.getString(GoGouConstants.MSG_KEY_FROM_SUBSCRIBER));
            chatEntry.setTo(data.getString(GoGouConstants.MSG_KEY_TO_SUBSCRIBER));
            chatEntry.setLayoutId(R.layout.list_say_he_item);
            DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format2));
            String date = df.format(new Date());
            chatEntry.setPostDate(date);
            chatEntry.setContent(data.getString(GoGouConstants.MSG_KEY_CONTENT));
            //mChatDao.postChat(chatEntry);
            mChatMsgs.add(chatEntry);
            mMsgViewAdapter.notifyDataSetChanged();
            mChatListView.setSelection(mChatListView.getCount());
            Log.d(TAG, "onReceive: " + data.getString(GoGouConstants.MSG_KEY_CONTENT));
        }
    };

    private void loadExistingChats() {
        mChatDao = new ChatDao(ChatActivity.this);
        mChatDao.open();
        mChatMsgs = mChatDao.listAllChats(mFromUsername, mToUsername, true);
        Log.d(TAG, "Found chat message number: " + mChatMsgs.size());
        mMsgViewAdapter = new ChatMsgViewAdapter(ChatActivity.this, mChatMsgs, mDisplayWidth);
        mChatListView.setAdapter(mMsgViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_detail_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_home:
                break;
            case R.id.menu_order:
                startActivity(new Intent(ChatActivity.this, MyOrderActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mChatDao.close();
        unregisterReceiver(broadcastReceiver);
    }

    private void sendChatMessageToGcm(Chat chatEntry) {
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        final Bundle data = new Bundle();
        data.putString(GoGouConstants.MSG_KEY_FROM_SUBSCRIBER, chatEntry.getFrom());
        data.putString(GoGouConstants.MSG_KEY_TO_SUBSCRIBER, chatEntry.getTo());
        data.putString(GoGouConstants.MSG_KEY_CONTENT, chatEntry.getContent());
        data.putString(GoGouConstants.MSG_KEY_ACTION, GoGouConstants.MSG_ACTION_MESSAGE);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    gcm.send(GoGouConstants.GCM_SENDERID + "@gcm.googleapis.com", nextMessageId(), 10000, data);
                    Log.i(TAG, "Successfully sent upstream message");
                    return null;
                } catch (IOException ex) {
                    Log.e(TAG, "Error sending upstream message", ex);
                    return "Error sending upstream message:" + ex.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    Toast.makeText(ChatActivity.this, "send message failed: " + result, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    public String nextMessageId() {
        return "m-" + UUID.randomUUID().toString();
    }*/
}
