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
import info.gogou.gogou.adapter.TripListAdapter;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.model.TripFilter;
import info.gogou.gogou.model.TripList;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.fragment.RoboFragment;

public class TripFragment extends RoboFragment {

    private static final String TAG = "TripFragment";

    private Activity mActivity;
	private DropDownListView mItineraryListView;
    private DropDownMenu mItineraryFilterMenu;

	private ArrayList<HashMap<String,String>> mTripList = new ArrayList<HashMap<String,String>>();
    // cache the trip list for now
    private TripList mCachedList =  new TripList();
	private TripListAdapter mTripListAdapter;

    private TripFilter mTripFilter = new TripFilter();

    private int mPageSize = 10; //number of items to load for one REST request
    private int mCurrentPage = 1; // current item index from where REST request loads items
    private int mSizeofCurPage = -1; // number of item in current page
    private boolean mIsLoading = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View itineraryView = inflater.inflate(R.layout.schedule_layout, container,false);
        mItineraryFilterMenu = (DropDownMenu)itineraryView.findViewById(R.id.itinerary_filter_menu);
        initItineraryFilters();
		/**
		 * 行程列表
		 */
        mItineraryListView=(DropDownListView)itineraryView.findViewById(R.id.itiListView);
        mItineraryListView.setHeaderDefaultText("");
        mItineraryListView.setFooterDefaultText("");

        mTripListAdapter= new TripListAdapter(mActivity, mTripList);
        mItineraryListView.setAdapter(mTripListAdapter);
        mItineraryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.itiListView:
                        if (position > 0) position -= 1;
                        Intent intent = new Intent(mActivity, ScheduleDetailActivity.class);
                        Log.d(TAG, "Row id in the list: " + position);
                        Trip trip = mCachedList.get(position);
                        intent.putExtra(GoGouIntents.TRIP, trip);
                        Log.d(TAG, "Selected demand ID: " + trip.getId());
                        startActivity(intent);
                }
            }
        });

        mItineraryListView.setOnScrollListener(new ManualScrollListener());

        // set drop down listener
        mItineraryListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

            @Override
            public void onDropDown() {
                Log.d(TAG, "It should be only called when reaching top header.");
                if (!mIsLoading)
                    performRequest(true);
            }
        });

        // set on bottom listener
        mItineraryListView.setOnBottomListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "It should be only called when reaching bottom.");
                if (!mIsLoading)
                    performRequest(false);
            }
        });

		return itineraryView;
	}

    private void initItineraryFilters()
    {
        String[] strings = getResources().getStringArray(R.array.itinerary_filter);

        CategoryList categories = CacheManager.getCachedCategories(mActivity);
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories)
        {
            categoryNames.add(category.getDisplayName());
        }

        String[] categoryArray = new String[categoryNames.size()];
        categoryArray = categoryNames.toArray(categoryArray);
        String[] cityArray1 = getResources().getStringArray(R.array.cities);
        String[] cityArray2 = getResources().getStringArray(R.array.cities);
        String[] arrivalTimeArray = getResources().getStringArray(R.array.arrivaltimes);

        mItineraryFilterMenu.setmMenuCount(4);
        mItineraryFilterMenu.setmShowCount(8);
        mItineraryFilterMenu.setShowCheck(true);
        mItineraryFilterMenu.setmMenuTitleTextSize(14);
        mItineraryFilterMenu.setmMenuTitleTextColor(Color.GRAY);
        mItineraryFilterMenu.setmMenuListTextSize(16);
        mItineraryFilterMenu.setmMenuListTextColor(Color.BLACK);
        mItineraryFilterMenu.setmMenuBackColor(Color.LTGRAY);
        mItineraryFilterMenu.setmMenuPressedBackColor(getResources().getColor(R.color.appMainColor));
        mItineraryFilterMenu.setmMenuPressedTitleTextColor(Color.BLACK);
        mItineraryFilterMenu.setmCheckIcon(R.drawable.ico_make);
        mItineraryFilterMenu.setmUpArrow(R.drawable.arrow_up);
        mItineraryFilterMenu.setmDownArrow(R.drawable.arrow_down);
        mItineraryFilterMenu.setDefaultMenuTitle(strings);
        mItineraryFilterMenu.setShowDivider(true);
        mItineraryFilterMenu.setmMenuListBackColor(getResources().getColor(R.color.white));
        mItineraryFilterMenu.setmMenuListSelectorRes(R.color.white);
        mItineraryFilterMenu.setmArrowMarginTitle(20);

        final List<String[]> items = new ArrayList<>();
        items.add(cityArray1);
        items.add(cityArray2);
        items.add(categoryArray);
        items.add(arrivalTimeArray);
        mItineraryFilterMenu.setmMenuItems(items);
        mItineraryFilterMenu.setIsDebug(true);

        mTripFilter.setPageSize(mPageSize);
        mTripFilter.setDeparture("desc");
        mTripFilter.setRatingOrder("desc");
        mTripFilter.setLanguageCode(getResources().getString(R.string.language_code));

        mItineraryFilterMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                switch (ColumnIndex) {
                    case 0:
                        if (RowIndex > 0) {
                            mTripFilter.setOrigin(items.get(ColumnIndex)[RowIndex]);
                        } else {
                            mTripFilter.setOrigin(null);
                        }
                        break;
                    case 1:
                        if (RowIndex > 0) {
                            mTripFilter.setDestination(items.get(ColumnIndex)[RowIndex]);
                        } else {
                            mTripFilter.setDestination(null);
                        }
                        break;
                    case 2:
                        if (RowIndex > 0) {
                            mTripFilter.setCategoryName(items.get(ColumnIndex)[RowIndex]);
                        } else {
                            mTripFilter.setCategoryName(null);
                        }
                        break;
                    case 3:
                        if (RowIndex > 1) {
                            mTripFilter.setDeparture("asc");
                        } else {
                            mTripFilter.setDeparture("desc");
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
    public void addTripItemToList(Trip trip)
    {
        // if number of item in current page is already equal to loading page size,
        // then the item could be retrieved to display from next list request
        if (mSizeofCurPage < mPageSize && mSizeofCurPage > 0) {
            addTripItemToListInternal(trip);
            mCachedList.add(trip);
            mSizeofCurPage++;
        }
        mTripListAdapter.notifyDataSetChanged();
    }

    private void addTripItemToListInternal(Trip trip)
    {
        HashMap<String,String> tripItem=new HashMap<String,String>();
        tripItem.put(GoGouConstants.KEY_DEPART, trip.getOrigin());
        tripItem.put(GoGouConstants.KEY_DESTINATION, trip.getDestination());
        tripItem.put(GoGouConstants.KEY_ARRIVALDATE, trip.getArrival());
        tripItem.put(GoGouConstants.KEY_DEPARTDATE, trip.getDeparture());
        mTripList.add(tripItem);
    }

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

    @Override
    public void onResume() {
        Log.d(TAG, "Trip fragment is resumed!");
        super.onResume();

        performRequest(true);
    }

    private void performRequest(final boolean isDropDown) {
        if (isDropDown) {
            cleanCachedList();
        }
        mTripFilter.setCurrentPage(mCurrentPage);

        mIsLoading = true;
        RESTRequestUtils.performTripListRequest(mTripFilter, new RESTRequestListener<TripList>() {

            @Override
            public void onGogouRESTRequestFailure() {
                if (isDropDown) {
                    mItineraryListView.onDropDownComplete();
                } else {
                    mItineraryListView.onBottomComplete();
                }
                Toast.makeText(mActivity, R.string.notif_get_trip_list_failed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGogouRESTRequestSuccess(TripList tripList) {

                Log.d(TAG, "There are " + tripList.size() + " trips in this request");
                // only retrieved list size is greater than 0, increase the current page
                if (tripList.size() > 0) {
                    mCurrentPage++;
                    mCachedList.addAll(tripList);
                    mSizeofCurPage = tripList.size();
                }
                for (Trip trip : tripList) {
                    addTripItemToListInternal(trip);
                }
                mTripListAdapter.notifyDataSetChanged();
                mIsLoading = false;
                if (isDropDown) {
                    mItineraryListView.onDropDownComplete();
                } else {
                    mItineraryListView.onBottomComplete();
                }
            }
        });

    }

    private void cleanCachedList() {
        mCurrentPage = 1;
        mCachedList.clear();
        mTripList.clear();
        mTripListAdapter.notifyDataSetChanged();
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
                mTripFilter.setCurrentPage(mCurrentPage);
                mTripFilter.setPageSize(mPageSize);
                mTripFilter.setDeparture("desc");
                mTripFilter.setRatingOrder("desc");
                performRequest();
                mIsLoading = true;
                performLoading = false;
            }*/
        }
    }
}
