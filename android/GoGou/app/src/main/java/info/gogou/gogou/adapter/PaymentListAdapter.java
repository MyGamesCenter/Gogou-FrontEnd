package info.gogou.gogou.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import info.gogou.gogou.R;
import info.gogou.gogou.model.PaymentMethod;
import info.gogou.gogou.model.PaymentMethodList;

/**
 * Created by lxu on 2016-05-01.
 */
public class PaymentListAdapter extends BaseAdapter {
    private static final String TAG = "PaymentListAdapter";

    private Activity mActivity;
    private PaymentMethodList mPaymentList;
    private static LayoutInflater mInflater=null;

    public PaymentListAdapter(Activity a, PaymentMethodList paymentList) {
        mActivity = a;
        mPaymentList = paymentList;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mPaymentList.size();
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
            vi = mInflater.inflate(R.layout.payment_list_row, null);

            holder = new ViewHolder();

            holder.paymentLogo = (ImageView) vi.findViewById(R.id.paymentLogo);
            holder.paymentAccount = (TextView) vi.findViewById(R.id.paymentAccount);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder)vi.getTag();
        }

        PaymentMethod paymentMethod = mPaymentList.get(position);
        // Setting all values in listView
        holder.paymentAccount.setText(paymentMethod.getPaymentId());

        return vi;
    }

    private static class ViewHolder {
        ImageView paymentLogo;
        TextView paymentAccount;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
