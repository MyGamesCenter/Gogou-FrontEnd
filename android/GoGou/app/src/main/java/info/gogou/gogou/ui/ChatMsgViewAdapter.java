
package info.gogou.gogou.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.model.Chat;


public class ChatMsgViewAdapter extends BaseAdapter {

    private static final String TAG = "ChatMsgViewAdapter";

    private List<Chat> mChats;

    private Context mContext;
    private int mDisplayWidth;
    private LayoutInflater mLayoutInflater;

    public ChatMsgViewAdapter(Context context, List<Chat> chats, int displayWidth) {
        mContext = context;
        this.mChats = chats;
        this.mDisplayWidth = displayWidth;

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int arg0) {
        return false;
    }

    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Object getItem(int position) {
        return mChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat entry = mChats.get(position);
        int itemLayout = entry.getLayoutId();

        LinearLayout layout = new LinearLayout(mContext);
        mLayoutInflater.inflate(itemLayout, layout, true);

        TextView tvName = (TextView) layout.findViewById(R.id.messagedetail_row_name);
        tvName.setText(entry.getFrom());
        tvName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        TextPaint paintname = tvName.getPaint();
        int lename = (int) paintname.measureText(entry.getFrom());

        TextView tvText = (TextView) layout.findViewById(R.id.messagedetail_row_text);
        tvText.setText(entry.getContent());
        TextPaint paint = tvText.getPaint();
        int len = (int) paint.measureText(entry.getContent());
        
        TextView tvDate = (TextView) layout.findViewById(R.id.messagedetail_row_date);
        tvDate.setText(entry.getPostDate());
        tvDate.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        TextPaint paintdate = tvDate.getPaint();
        int lendate = (int) paintdate.measureText(entry.getPostDate());
        
        if ( len > mDisplayWidth){
            Log.d(TAG, "Display width: " + mDisplayWidth + "  len: " + len + " lename: " + lename + " lendate: " + lendate);
        	int lendateint = (int)(lendate*1.8);
        	tvDate.setPadding(mDisplayWidth - (lename + lendateint - 2), 0, 0, 0);
        }else{
        	tvDate.setPadding(len, 0, 0, 0);
        }
        
        return layout;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
