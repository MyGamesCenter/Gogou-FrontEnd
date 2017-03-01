package info.gogou.gogou.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.common.util.ResourceUtils;
import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.model.OrderStatus;

/**
 * Created by lxu on 2016-05-01.
 */
public class OrderListAdapter extends BaseAdapter {
    private static final String TAG = "OrderListAdapter";

    public enum OrderType {
        CREATED_ORDER,
        RECEIVED_ORDER
    }

    public enum OrderOperation {
        GET_DETAIL_ORDER,
        CANCEL_ORDER,
        CONFIRM_ORDER,
        PAY_ORDER,
        DELIVER_ORDER,
        COLLECT_ORDER,
        REMOVE_ORDER,
        REFUND_ORDER
    }

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mData;
    private static LayoutInflater mInflater=null;
    private OrderType mOrderType;

    public OrderListAdapter(Activity a, OrderType orderType, ArrayList<HashMap<String, String>> d) {
        mActivity = a;
        mOrderType = orderType;
        mData = d;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi = convertView;
        ViewHolder holder;
        if (vi == null) {
            vi = mInflater.inflate(R.layout.my_order_item_listview, null);

            holder = new ViewHolder();
            holder.clickableView = (LinearLayout) vi.findViewById(R.id.orderMainLayout);
            holder.priceRangeLayout = (RelativeLayout)vi.findViewById(R.id.buyerPriceRange);
            holder.finalPriceLayout = (RelativeLayout)vi.findViewById(R.id.finalPriceArea);
            holder.orderStatus = (TextView) vi.findViewById(R.id.orderStatus);
            holder.productName = (TextView) vi.findViewById(R.id.orderProductName);
            holder.productDetail = (TextView) vi.findViewById(R.id.orderProductDetails);
            holder.orderQuantity = (TextView) vi.findViewById(R.id.orderQuantity);
            holder.minPrice = (TextView) vi.findViewById(R.id.buyerMinPrice);
            holder.maxPrice = (TextView) vi.findViewById(R.id.buyerMaxPrice);
            holder.orderPrice = (TextView) vi.findViewById(R.id.orderPrice);
            holder.orderImage = (ImageView) vi.findViewById(R.id.orderImage);
            holder.cancelBtn = (Button) vi.findViewById(R.id.cancelOrderButton);
            holder.removeBtn = (Button) vi.findViewById(R.id.deleteOrderButton);
            holder.payBtn = (Button) vi.findViewById(R.id.payOrderButton);
            holder.confirmBtn = (Button) vi.findViewById(R.id.confirmOrderButton);
            holder.deliverBtn = (Button) vi.findViewById(R.id.deliverOrderButton);
            holder.collectBtn = (Button) vi.findViewById(R.id.collectOrderButton);

            holder.clickableView.setOnClickListener(onItemBtnClickListener);
            holder.cancelBtn.setOnClickListener(onItemBtnClickListener);
            holder.removeBtn.setOnClickListener(onItemBtnClickListener);
            holder.payBtn.setOnClickListener(onItemBtnClickListener);
            holder.confirmBtn.setOnClickListener(onItemBtnClickListener);
            holder.deliverBtn.setOnClickListener(onItemBtnClickListener);
            holder.collectBtn.setOnClickListener(onItemBtnClickListener);

            if (mOrderType == OrderType. CREATED_ORDER) {
                holder.travellerLabel = (TextView) vi.findViewById(R.id.traveller4Order);
                holder.traveller = (TextView) vi.findViewById(R.id.orderTraveller);
                holder.ownerLabel = (TextView) vi.findViewById(R.id.orderOwnerLabel);
                holder.owner = (TextView) vi.findViewById(R.id.orderOwnerText);
                holder.ownerLabel.setVisibility(View.GONE);
                holder.owner.setVisibility(View.GONE);
                holder.confirmBtn.setVisibility(View.GONE);
                holder.deliverBtn.setVisibility(View.GONE);
            } else {
                holder.travellerLabel = (TextView) vi.findViewById(R.id.traveller4Order);
                holder.traveller = (TextView) vi.findViewById(R.id.orderTraveller);
                holder.ownerLabel = (TextView) vi.findViewById(R.id.orderOwnerLabel);
                holder.owner = (TextView) vi.findViewById(R.id.orderOwnerText);
                holder.travellerLabel.setVisibility(View.GONE);
                holder.traveller.setVisibility(View.GONE);
                holder.cancelBtn.setVisibility(View.GONE);
                holder.payBtn.setVisibility(View.GONE);
                holder.collectBtn.setVisibility(View.GONE);
            }

            vi.setTag(holder);
        } else {
            holder = (ViewHolder)vi.getTag();
        }

        holder.clickableView.setTag(position);
        holder.cancelBtn.setTag(position);
        holder.removeBtn.setTag(position);
        holder.payBtn.setTag(position);
        holder.confirmBtn.setTag(position);
        holder.deliverBtn.setTag(position);
        holder.collectBtn.setTag(position);

        HashMap<String, String> items = new HashMap<String, String>();
        items = mData.get(position);
        String orderStatusStr = items.get(GoGouConstants.KEY_ORDER_STATUS_EN);

        if (OrderStatus.PREORDERED.getValue().equals(orderStatusStr)) {
            holder.priceRangeLayout.setVisibility(View.VISIBLE);
            holder.finalPriceLayout.setVisibility(View.GONE);
        } else {
            holder.priceRangeLayout.setVisibility(View.GONE);
            holder.finalPriceLayout.setVisibility(View.VISIBLE);
        }

        if (mOrderType == OrderType.CREATED_ORDER) {
            if (OrderStatus.PREORDERED.getValue().equals(orderStatusStr)) {
                holder.cancelBtn.setVisibility(View.VISIBLE);
                holder.payBtn.setVisibility(View.VISIBLE);
                holder.collectBtn.setVisibility(View.GONE);
                holder.payBtn.setEnabled(false);
            }
            else if (OrderStatus.CONFIRMED.getValue().equals(orderStatusStr)) {
                holder.cancelBtn.setVisibility(View.VISIBLE);
                holder.payBtn.setVisibility(View.VISIBLE);
                holder.collectBtn.setVisibility(View.GONE);
                holder.payBtn.setEnabled(true);
            }
            else if (OrderStatus.ORDERED.getValue().equals(orderStatusStr)) {
                holder.cancelBtn.setVisibility(View.GONE);
                holder.payBtn.setVisibility(View.GONE);
                holder.collectBtn.setVisibility(View.VISIBLE);
                holder.collectBtn.setEnabled(false);
            }
            else if (OrderStatus.DELIVERED.getValue().equals(orderStatusStr)) {
                holder.cancelBtn.setVisibility(View.GONE);
                holder.payBtn.setVisibility(View.GONE);
                holder.collectBtn.setVisibility(View.VISIBLE);
                holder.collectBtn.setEnabled(true);
            }
            else if (OrderStatus.COLLECTED.getValue().equals(orderStatusStr)) {
                holder.cancelBtn.setVisibility(View.GONE);
                holder.payBtn.setVisibility(View.GONE);
                holder.collectBtn.setVisibility(View.GONE);
            }
            holder.traveller.setText(items.get(GoGouConstants.KEY_ORDER_TRAVELLER));
        } else {
            if (OrderStatus.PREORDERED.getValue().equals(orderStatusStr)) {
                holder.deliverBtn.setVisibility(View.GONE);
                holder.confirmBtn.setVisibility(View.VISIBLE);
            }
            else if (OrderStatus.CONFIRMED.getValue().equals(orderStatusStr)) {
                holder.deliverBtn.setVisibility(View.VISIBLE);
                holder.confirmBtn.setVisibility(View.GONE);
                holder.deliverBtn.setEnabled(false);
            }
            else if (OrderStatus.ORDERED.getValue().equals(orderStatusStr)) {
                holder.confirmBtn.setVisibility(View.GONE);
                holder.deliverBtn.setVisibility(View.VISIBLE);
                holder.deliverBtn.setEnabled(true);
            }
            else if (OrderStatus.DELIVERED.getValue().equals(orderStatusStr) ||
                     OrderStatus.COLLECTED.getValue().equals(orderStatusStr) ) {
                holder.confirmBtn.setVisibility(View.GONE);
                holder.deliverBtn.setVisibility(View.GONE);
            }

            holder.owner.setText(items.get(GoGouConstants.KEY_ORDER_BUYER));
        }

        holder.orderStatus.setText(items.get(GoGouConstants.KEY_ORDER_STATUS));
        holder.productName.setText(items.get(GoGouConstants.KEY_ORDER_PRODUCT));
        holder.productDetail.setText(items.get(GoGouConstants.KEY_ORDER_PRODUCT_DETAIL));
        holder.orderQuantity.setText(items.get(GoGouConstants.KEY_ORDER_QUANTITY));
        holder.minPrice.setText(items.get(GoGouConstants.KEY_ORDER_MIN_PRICE));
        holder.maxPrice.setText(items.get(GoGouConstants.KEY_ORDER_MAX_PRICE));
        holder.orderPrice.setText(items.get(GoGouConstants.KEY_ORDER_PRICE));
        String imageName = items.get(GoGouConstants.KEY_IMAGE_NAME);
        if (imageName != null) {
            Log.d(TAG, "order image name is: " + imageName);
            holder.orderImage.setImageDrawable(ResourceUtils.getDrawableFromResourceByName(mActivity, imageName));
        }
        return vi;
    }

    View.OnClickListener onItemBtnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int position = (Integer)view.getTag();
            OrderOperation operation = OrderOperation.GET_DETAIL_ORDER;
            switch(view.getId()) {
                case R.id.cancelOrderButton:
                    Log.d(TAG, "Order item: " + position + " cancel button is pressed.");
                    operation = OrderOperation.CANCEL_ORDER;
                    break;
                case R.id.deleteOrderButton:
                    Log.d(TAG, "Order item: " + position + " remove button is pressed.");
                    operation = OrderOperation.DELIVER_ORDER;
                    break;
                case R.id.payOrderButton:
                    Log.d(TAG, "Order item: " + position + " pay button is pressed.");
                    operation = OrderOperation.PAY_ORDER;
                    break;
                case R.id.confirmOrderButton:
                    Log.d(TAG, "Order item: " + position + " confirm button is pressed.");
                    operation = OrderOperation.CONFIRM_ORDER;
                    break;
                case R.id.deliverOrderButton:
                    Log.d(TAG, "Order item: " + position + " deliver button is pressed.");
                    operation = OrderOperation.DELIVER_ORDER;
                    break;
                case R.id.collectOrderButton:
                    Log.d(TAG, "Order item: " + position + " collect button is pressed.");
                    operation = OrderOperation.COLLECT_ORDER;
                    break;
                default:
                    Log.d(TAG, "Order item: " + position + " is clicked.");
                    break;
            }
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(operation, position);
            }
        }
    };

    private static class ViewHolder {
        LinearLayout clickableView;
        RelativeLayout priceRangeLayout;
        RelativeLayout finalPriceLayout;
        TextView travellerLabel;
        TextView traveller;
        TextView ownerLabel;
        TextView owner;
        TextView orderStatus;
        TextView productName;
        TextView productDetail;
        TextView orderQuantity;
        TextView minPrice;
        TextView maxPrice;
        TextView orderPrice;
        Button cancelBtn;
        Button removeBtn;
        Button payBtn;
        Button confirmBtn;
        Button deliverBtn;
        Button collectBtn;
        ImageView orderImage;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(OrderOperation operation, int position);
    }
}
