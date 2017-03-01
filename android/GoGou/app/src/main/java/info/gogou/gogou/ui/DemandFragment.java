package info.gogou.gogou.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trinea.android.common.view.DropDownListView;
import info.gogou.gogou.R;
import info.gogou.gogou.adapter.DemandListAdapter;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.model.Demand;
import info.gogou.gogou.model.DemandFilter;
import info.gogou.gogou.model.DemandList;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.fragment.RoboFragment;

public class DemandFragment extends RoboFragment {

    private static final String TAG = "DemandFragment";

	private Activity mActivity;
	private DropDownListView mDemandListView;
    private DropDownMenu mDemandFilterMenu;

    private ArrayList<HashMap<String,String>> mDemandList = new ArrayList<HashMap<String,String>>();
    // cache the demand list for now
    private DemandList mCachedList =  new DemandList();
    private DemandListAdapter mDemandListAdapter;

    private DemandFilter mDemandFilter = new DemandFilter();

    private int mPageSize = 10; //number of items to load for one REST request
    private int mCurrentPage = 1; // current item index from where REST request loads items
    private int mSizeofCurPage = -1; // number of item in current page
    private boolean mIsLoading = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View demandView = inflater.inflate(R.layout.requirement_layout, container,false);
        mDemandFilterMenu = (DropDownMenu)demandView.findViewById(R.id.demand_filter_menu);
        initDemandFilters();
        /**
         * 需求列表
         */
        mDemandListView=(DropDownListView)demandView.findViewById(R.id.demandListView);
        mDemandListView.setHeaderDefaultText("");
        mDemandListView.setFooterDefaultText("");

        mDemandListAdapter = new DemandListAdapter(mActivity, mDemandList);
        mDemandListView.setAdapter(mDemandListAdapter);
        mDemandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch(parent.getId())
				{
					case R.id.demandListView:
                        if (position > 0) position -= 1;
						Intent intent = new Intent(mActivity, RequirementDetailActivity.class);
                        Log.d(TAG, "Row id in the list: " + position);
                        Demand demand = mCachedList.get(position);
						intent.putExtra(GoGouIntents.DEMAND, demand);
                        Log.d(TAG, "Selected demand ID: " + demand.getId());
						startActivity(intent);
				}

			}
		});
        mDemandListView.setOnScrollListener(new ManualScrollListener());

        // set drop down listener
        mDemandListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

            @Override
            public void onDropDown() {
                Log.d(TAG, "It should be only called when reaching top header.");
                if (!mIsLoading)
                    performRequest(true);
            }
        });

        // set on bottom listener
        mDemandListView.setOnBottomListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "It should be only called when reaching bottom.");
                if (!mIsLoading)
                    performRequest(false);
            }
        });

		return demandView;

	}

    private void initDemandFilters()
    {
        String[] strings = getResources().getStringArray(R.array.demand_filter);

        CategoryList categories = CacheManager.getCachedCategories(mActivity);
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories)
        {
            categoryNames.add(category.getDisplayName());
        }

        String[] categoryArray = new String[categoryNames.size()];
        categoryArray = categoryNames.toArray(categoryArray);
        String[] countryArray = getResources().getStringArray(R.array.countries);
        String[] cityArray = getResources().getStringArray(R.array.cities);

        mDemandFilterMenu.setmMenuCount(3);
        mDemandFilterMenu.setmShowCount(8);
        mDemandFilterMenu.setShowCheck(true);
        mDemandFilterMenu.setmMenuTitleTextSize(14);
        mDemandFilterMenu.setmMenuTitleTextColor(Color.GRAY);
        mDemandFilterMenu.setmMenuListTextSize(16);
        mDemandFilterMenu.setmMenuListTextColor(Color.BLACK);
        mDemandFilterMenu.setmMenuBackColor(Color.LTGRAY);
        mDemandFilterMenu.setmMenuPressedBackColor(getResources().getColor(R.color.appMainColor));
        mDemandFilterMenu.setmMenuPressedTitleTextColor(Color.BLACK);
        mDemandFilterMenu.setmCheckIcon(R.drawable.ico_make);
        mDemandFilterMenu.setmUpArrow(R.drawable.arrow_up);
        mDemandFilterMenu.setmDownArrow(R.drawable.arrow_down);
        mDemandFilterMenu.setDefaultMenuTitle(strings);
        mDemandFilterMenu.setShowDivider(true);
        mDemandFilterMenu.setmMenuListBackColor(getResources().getColor(R.color.white));
        mDemandFilterMenu.setmMenuListSelectorRes(R.color.white);
        mDemandFilterMenu.setmArrowMarginTitle(20);

        final List<String[]> items = new ArrayList<>();
        items.add(categoryArray);
        items.add(countryArray);
        items.add(cityArray);
        mDemandFilterMenu.setmMenuItems(items);
        mDemandFilterMenu.setIsDebug(true);

        mDemandFilter.setPageSize(mPageSize);
        mDemandFilter.setServiceFeeOrder("asc");
        mDemandFilter.setRatingOrder("desc");
        mDemandFilter.setLanguageCode(getResources().getString(R.string.language_code));

        mDemandFilterMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                switch (ColumnIndex) {
                    case 0:
                        if (RowIndex > 0) {
                            mDemandFilter.setCategoryName(items.get(ColumnIndex)[RowIndex]);
                        } else {
                            mDemandFilter.setCategoryName(null);
                        }
                        break;
                    case 1:
                        if (RowIndex > 0) {
                            mDemandFilter.setOrigin(items.get(ColumnIndex)[RowIndex]);
                        } else {
                            mDemandFilter.setOrigin(null);
                        }
                        break;
                    case 2:
                        if (RowIndex > 0) {
                            mDemandFilter.setDestination(items.get(ColumnIndex)[RowIndex]);
                        } else {
                            mDemandFilter.setDestination(null);
                        }
                        break;
                    default:
                        break;
                }

                if (!mIsLoading)
                    performRequest(true);
            }
        });
    }

    // Add a demand item to the adapter
    public void addDemandItemToList(Demand demand)
    {
        // if number of item in current page is already equal to loading page size,
        // then the item could be retrieved to display from next list request
        if (mSizeofCurPage < mPageSize && mSizeofCurPage > 0) {
            addDemandItemToListInternal(demand);
            mCachedList.add(demand);
            mSizeofCurPage++;
        }
        mDemandListAdapter.notifyDataSetChanged();
    }

    private void addDemandItemToListInternal(Demand demand)
    {
        HashMap<String,String> demandItem=new HashMap<String,String>();
        demandItem.put(GoGouConstants.KEY_DESTINATION, demand.getDestination());
        demandItem.put(GoGouConstants.KEY_ORIGIN, demand.getOrigin());
        demandItem.put(GoGouConstants.KEY_PRODUCT_NAME, demand.getProductname());
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
    public void onResume() {
        Log.d(TAG, "Demand fragment is resumed!");
        super.onResume();

        performRequest(true);
    }

    private void performRequest(final boolean isDropDown) {
        if (isDropDown) {
            cleanCachedList();
        }
        mDemandFilter.setCurrentPage(mCurrentPage);

        mIsLoading = true;
        RESTRequestUtils.performDemandListRequest(mDemandFilter, new RESTRequestListener<DemandList>() {

            @Override
            public void onGogouRESTRequestFailure() {
                if (isDropDown) {
                    mDemandListView.onDropDownComplete();
                } else {
                    mDemandListView.onBottomComplete();
                }
                Toast.makeText(mActivity, R.string.notif_get_demand_list_failed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGogouRESTRequestSuccess(DemandList demandList) {
                // only retrieved list size is greater than 0, increase the current page
                Log.d(TAG, "There are " + demandList.size() + " demands in this request");
                if (demandList.size() > 0)
                {
                    mCurrentPage++;
                    mCachedList.addAll(demandList);
                    mSizeofCurPage = demandList.size();
                }
                for (Demand demand : demandList)
                {
                    addDemandItemToListInternal(demand);
                }
                mDemandListAdapter.notifyDataSetChanged();
                mIsLoading = false;
                if (isDropDown) {
                    mDemandListView.onDropDownComplete();
                } else {
                    mDemandListView.onBottomComplete();
                }
            }
        });
    }

    private void cleanCachedList() {
        mCurrentPage = 1;
        mCachedList.clear();
        mDemandList.clear();
        mDemandListAdapter.notifyDataSetChanged();
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
            /*if ((firstVisibleItem + visibleItemCount) >= totalItemCount)
            {
                performLoading = true;
            }*/
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG, "Scroll state is changed to : " + scrollState);
            /*if (scrollState ==  SCROLL_STATE_TOUCH_SCROLL && !mIsLoading && performLoading)
            {
                Log.d(TAG, "Perform a read loading request...");
                mDemandFilter.setCurrentPage(mCurrentPage);
                mDemandFilter.setPageSize(mPageSize);
                mDemandFilter.setServiceFeeOrder("asc");
                mDemandFilter.setRatingOrder("desc");
                performRequest();
                mIsLoading = true;
                performLoading = false;
            }*/
        }
    }
}
