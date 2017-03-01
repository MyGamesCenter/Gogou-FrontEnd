package info.gogou.gogou.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.common.util.ResourceUtils;
import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by lxu on 2016-04-27.
 */
public class DemandListAdapter extends BaseAdapter {
    private static final String TAG = "DemandListAdapter";

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mData;
    private LayoutInflater mInflater = null;
    //public ImageLoader imageLoader;

    public DemandListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        mActivity = a;
        mData = d;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (vi == null) {
            vi = mInflater.inflate(R.layout.demand_list_row, null);

            holder = new ViewHolder();
            holder.productOrigin = (TextView) vi.findViewById(R.id.demandSubtitle); // product origin
            holder.collectLocation = (TextView) vi.findViewById(R.id.demandSubtitle2); // collect location
            holder.productName = (TextView) vi.findViewById(R.id.demandTitle); // product name
            holder.categoryImage = (ImageView) vi.findViewById(R.id.list_image); // thumb image

            vi.setTag(holder);
        } else {
            holder = (ViewHolder)vi.getTag();
        }

        HashMap<String, String> items = new HashMap<String, String>();
        items = mData.get(position);

        // Setting all values in listview
        holder.collectLocation.setText(items.get(GoGouConstants.KEY_DESTINATION));
        holder.productOrigin.setText(items.get(GoGouConstants.KEY_ORIGIN));
        holder.productName.setText(items.get(GoGouConstants.KEY_PRODUCT_NAME));
        String imageName = items.get(GoGouConstants.KEY_IMAGE_NAME);
        if (imageName != null) {
            Log.d(TAG, "Category image name is: " + imageName);
            holder.categoryImage.setImageDrawable(ResourceUtils.getDrawableFromResourceByName(mActivity, imageName));
        }
        return vi;
    }

    private static class ViewHolder {
        TextView productOrigin;
        TextView collectLocation;
        TextView productName;
        ImageView categoryImage;
    }
}
