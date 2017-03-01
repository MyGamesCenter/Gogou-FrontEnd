package info.gogou.gogou.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.trinea.android.common.util.ResourceUtils;
import info.gogou.gogou.R;
import info.gogou.gogou.model.Category;

/**
 * Created by lxu on 2016-01-04.
 */
public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    private static final String TAG = "CategorySpinnerAdapter";

    private LayoutInflater mInflater=null;
    List<Category> mCategoryList;
    Context mContext;

    public CategorySpinnerAdapter(Context context, int resource, List<Category> categories)
    {
        super(context, resource, categories);
        mContext = context;
        mCategoryList = categories;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View spinnerRow = mInflater.inflate(R.layout.gogou_spinner_row, null);
        ImageView spinnerImage = (ImageView)spinnerRow.findViewById(R.id.spinner_image);
        TextView spinnerText = (TextView)spinnerRow.findViewById(R.id.spinner_text);

        spinnerText.setText(mCategoryList.get(position).getDisplayName());
        String imageName = mCategoryList.get(position).getImagePath();
        if (imageName != null) {
            Log.d(TAG, "Category image name is: " + imageName);
            spinnerImage.setImageDrawable(ResourceUtils.getDrawableFromResourceByName(mContext, imageName));
        }

        return spinnerRow;
    }
}
