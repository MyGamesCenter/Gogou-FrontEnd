package info.gogou.gogou.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import info.gogou.gogou.R;
import info.gogou.gogou.model.Address;
import info.gogou.gogou.model.AddressList;

/**
 * Created by lxu on 2016-05-01.
 */
public class AddressListAdapter extends BaseAdapter {
    private static final String TAG = "AddressListAdapter";

    private Activity mActivity;
    private AddressList mAddressList;
    private static LayoutInflater mInflater=null;

    public AddressListAdapter(Activity a, AddressList addressList) {
        mActivity = a;
        mAddressList = addressList;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mAddressList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (vi == null) {
            vi = mInflater.inflate(R.layout.address_list_row, null);

            holder = new ViewHolder();

            holder.addressReceiver = (TextView) vi.findViewById(R.id.orderDetailCollectInfoText);
            holder.addressPhone = (TextView) vi.findViewById(R.id.orderDetailPhoneText);
            holder.addressDetail = (TextView) vi.findViewById(R.id.orderDetailAddressText);

            /*holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer)view.getTag();
                    Log.d(TAG, "Order item: " + position + " cancel button is pressed.");
                }
            });

            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer)view.getTag();
                    Log.d(TAG, "Order item: " + position + " remove button is pressed.");
                }
            });

            holder.payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer)view.getTag();
                    Log.d(TAG, "Order item: " + position + " pay button is pressed.");
                }
            });*/

            vi.setTag(holder);
        } else {
            holder = (ViewHolder)vi.getTag();
        }

        /*holder.cancelBtn.setTag(position);
        holder.removeBtn.setTag(position);
        holder.payBtn.setTag(position);*/

        Address address = mAddressList.get(position);

        // Setting all values in listView
        holder.addressReceiver.setText(address.getLastName() + " " +  address.getFirstName());
        holder.addressPhone.setText(address.getTelephone());
        holder.addressDetail.setText( ((address.getState() != null) ? address.getState() : "") +
                ((address.getCity() != null) ? address.getCity() : "") +
                ((address.getStreetAddress() != null) ? address.getStreetAddress() : "") +
                ((address.getPostcode()!= null) ? address.getPostcode() : "") );

        return vi;
    }

    private static class ViewHolder {
        TextView addressReceiver;
        TextView addressPhone;
        TextView addressDetail;

        // TODO (lxu) add buttons after
        /*Button cancelBtn;
        Button removeBtn;
        Button payBtn;*/
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
