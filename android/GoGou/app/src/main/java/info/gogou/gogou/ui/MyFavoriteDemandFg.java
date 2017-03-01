package info.gogou.gogou.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import info.gogou.gogou.R;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.Demand;
import info.gogou.gogou.model.DemandFilter;
import info.gogou.gogou.model.DemandList;
import info.gogou.gogou.request.DemandRequest;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class MyFavoriteDemandFg extends RoboFragment {

    private static final String TAG = "MyFavoriteDemandFg";

    @InjectView(R.id.myFavorite_demand_list)
    private ListView demand_listview;

    private Activity mActivity;

    private DemandList mCachedList =  new DemandList();

    private ArrayList<HashMap<String,String>> mDemandList = new ArrayList<HashMap<String,String>>();

    private ProgressDialog mProgressSpinner;

    private DemandListAdapter mDemandListAdapter;


    private SpiceManager mSpiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);

    private DemandFilter mDemandFilter = new DemandFilter();

    private int mPageSize = 40; //number of items to load for one REST request
    private int mCurrentPage = 1; // current item index from where REST request loads items
    private int mSizeofCurPage = 0; // number of item in current page
    private boolean mIsLoading = false;

    private String mUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mProgressSpinner = new ProgressDialog(mActivity, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        View demand_view = inflater.inflate(R.layout.myfavorite_demandfg_layout, container,false);

        Bundle bundle = getArguments();
        mUserName = bundle.getString(GoGouConstants.KEY_USER_NAME);
        Log.d(TAG, "My release demand of user: " + mUserName);

        demand_listview=(ListView)demand_view.findViewById(R.id.myFavorite_demand_list);
        mDemandListAdapter = new DemandListAdapter(mActivity, mDemandList);
        demand_listview.setAdapter(mDemandListAdapter);


        demand_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.myFavorite_demand_list:
                        Intent intent = new Intent(mActivity, RequirementDetailActivity.class);
                        Log.d(TAG, "Row id in the list: " + position);
                        Demand demand = mCachedList.get(position);
                        intent.putExtra(GoGouIntents.DEMAND, demand);
                        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
                        startActivity(intent);
                }

            }
        });

//        demand_listview.setOnScrollListener(new ManualScrollListener());

        return demand_view;
    }

    private void addDemandItemToListInternal(Demand demand)
    {
        HashMap<String,String> demandItem=new HashMap<String,String>();
        demandItem.put(GoGouConstants.KEY_DESTINATION, demand.getDestination());
        demandItem.put(GoGouConstants.KEY_ORIGIN, demand.getOrigin());
        demandItem.put(GoGouConstants.KEY_DETAIL, demand.getProductname());
        Category category = CacheManager.findCategoryByName(demand.getCategoryName());
        demandItem.put(GoGouConstants.KEY_IMAGE_NAME, category != null ? category.getImagePath() : null);
        mDemandList.add(demandItem);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onStart() {
        mSpiceManager.start(mActivity);
        super.onStart();
    }

    @Override
    public void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDemandFilter.setCurrentPage(mCurrentPage);
        mDemandFilter.setPageSize(mPageSize);
        mDemandFilter.setServiceFeeOrder("asc");
        mDemandFilter.setRatingOrder("desc");
        mCachedList.clear();
        mDemandList.clear();
        performRequest();
    }

    private void performRequest() {
        mProgressSpinner.show();

        DemandRequest.GetDemandListRequest request = new DemandRequest.GetDemandListRequest(mDemandFilter);
        mSpiceManager.execute(request, new GetDemandListRequestListener());
    }

    private class GetDemandListRequestListener implements
            RequestListener<DemandList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(mActivity,
                    "Error during request: " + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
            mProgressSpinner.dismiss();
        }

        @Override
        public void onRequestSuccess(DemandList demandList) {

            mProgressSpinner.dismiss();
            if (demandList == null)
            {
                Toast.makeText(mActivity,
                        "Getting demand list failed", Toast.LENGTH_LONG);

            }
            else
            {
                Log.d(TAG, "There are " + demandList.size() + " demands");

                List<Long> favorite_demand_list = read();

                if(favorite_demand_list != null){
                    favorite_demand_list = removeDuplicate(read());
                    Log.d(TAG, "favorite demand size"+ favorite_demand_list.size());

                    for(int i=0; i< favorite_demand_list.size(); i++){

                        for (Demand demand : demandList)
                        {
                            if(demand.getId().equals(favorite_demand_list.get(i))){
                                addDemandItemToListInternal(demand);
                                mCachedList.add(demand);
                            }
                        }


                    }
                }

                mDemandListAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        }
    }


    public static List<Long> removeDuplicate(List<Long> list)
    {
        Set set = new LinkedHashSet<Long>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }


    public List<Long> read() {
        try {
            FileInputStream inStream = getActivity().openFileInput("Demand.txt");
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }

            String regex = ",|，|\\s+";
            String strAry[] = sb.toString().split(regex);
            List<Long> list = new ArrayList<Long>();

            if(strAry != null){
                for (int i = 0; i < strAry.length; i++) {
                    list.add(Long.parseLong(strAry[i]));
                    Log.d(TAG, "favorite demand id" + Long.parseLong(strAry[i]));
                }
            }

            inStream.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ManualScrollListener implements AbsListView.OnScrollListener {

        private boolean performLoading = false;

        public ManualScrollListener() {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            Log.d(TAG, "First visible item index: " + firstVisibleItem);
            Log.d(TAG, "Number of visible cells: " + visibleItemCount);
            Log.d(TAG, "Number of items in the list adaptor: " + totalItemCount);

            // when the view reaches to the last item, a request is performed
            if ((firstVisibleItem + visibleItemCount) >= totalItemCount)
            {
                performLoading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG, "Scroll state is changed to : " + scrollState);
            if (scrollState ==  SCROLL_STATE_TOUCH_SCROLL && !mIsLoading && performLoading)
            {
                Log.d(TAG, "Perform a read loading request...");
                mDemandFilter.setCurrentPage(mCurrentPage);
                mDemandFilter.setPageSize(mPageSize);
                mDemandFilter.setServiceFeeOrder("asc");
                mDemandFilter.setRatingOrder("desc");
                //mDemandFilter.setUserName(mUserName);
                mCachedList.clear();
                mDemandList.clear();
                performRequest();
                mIsLoading = true;
                performLoading = false;
            }
        }
    }

    private  class DemandListAdapter extends BaseAdapter {
        private Activity mActivity;
        private ArrayList<HashMap<String, String>> mData;
        private LayoutInflater mInflater=null;
        //public ImageLoader imageLoader;

        public DemandListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            mActivity = a;
            mData = d;
            mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            View vi=convertView;
            if(convertView==null)
                vi = mInflater.inflate(R.layout.demand_list_row, null);

            TextView originTV = (TextView)vi.findViewById(R.id.demandSubtitle); // product origin
            TextView destinationTV = (TextView)vi.findViewById(R.id.demandSubtitle2); // destination
            TextView detailTV = (TextView)vi.findViewById(R.id.demandTitle); // product detail
            ImageView categoryImage=(ImageView)vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> items = new HashMap<String, String>();
            items = mData.get(position);

            // Setting all values in listview
            destinationTV.setText(items.get(GoGouConstants.KEY_DESTINATION));
            originTV.setText(items.get(GoGouConstants.KEY_ORIGIN));
            detailTV.setText(items.get(GoGouConstants.KEY_DETAIL));
            String imageName = items.get(GoGouConstants.KEY_IMAGE_NAME);
            if (imageName != null)
            {
                Log.d(TAG, "Category image name is: " + imageName);
                categoryImage.setImageDrawable(getDrawableFromResourceByName(imageName));
            }
            return vi;
        }
    }

    private Drawable getDrawableFromResourceByName(String name)
    {
        Resources resources = mActivity.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                mActivity.getPackageName());

        try {
            return resources.getDrawable(resourceId);
        } catch (Resources.NotFoundException e)
        {
            return null;
        }
    }

}
