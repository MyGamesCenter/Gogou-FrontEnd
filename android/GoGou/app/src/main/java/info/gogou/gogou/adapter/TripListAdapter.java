package info.gogou.gogou.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by lxu on 2016-04-28.
 */
public class TripListAdapter extends BaseAdapter {

    private static final String TAG = "TripListAdapter";

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mData;
    private static LayoutInflater mInflater=null;
    //public ImageLoader imageLoader;

    public TripListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        mActivity = a;
        mData = d;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        if(vi == null) {
            vi = mInflater.inflate(R.layout.schedule_list_row, null);

            holder = new ViewHolder();
            holder.departure = (TextView)vi.findViewById(R.id.departureTV); // departure
            holder.destination = (TextView)vi.findViewById(R.id.destinationTV); // destination
            holder.arrivalDate = (TextView)vi.findViewById(R.id.arrivalDateTV); // arrival Date
            holder.departureDate = (TextView)vi.findViewById(R.id.departDateTV); // depart Date

            vi.setTag(holder);
        } else {
            holder = (ViewHolder)vi.getTag();
        }

        HashMap<String, String> items = new HashMap<String, String>();
        items = mData.get(position);

        // Setting all values in listview
        holder.departure.setText(items.get(GoGouConstants.KEY_DEPART));
        holder.destination.setText(items.get(GoGouConstants.KEY_DESTINATION));
        holder.arrivalDate.setText(items.get(GoGouConstants.KEY_ARRIVALDATE));
        holder.departureDate.setText(items.get(GoGouConstants.KEY_DEPARTDATE));

        return vi;
    }

    private static class ViewHolder {
        TextView departure;
        TextView destination;
        TextView departureDate;
        TextView arrivalDate;
    }
}
