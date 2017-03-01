package info.gogou.gogou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lxu on 2016-05-04.
 */

public class MyFragmentPageAadpter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentsList;
    private String[] fragmentTitles;

    public MyFragmentPageAadpter(FragmentManager fm) {super(fm);}

    public MyFragmentPageAadpter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
        this.fragmentTitles = null;
    }

    public MyFragmentPageAadpter(FragmentManager fm, ArrayList<Fragment> fragments, String[] fragmentTitles) {
        super(fm);
        this.fragmentsList = fragments;
        this.fragmentTitles = fragmentTitles;
    }

    @Override
    public Fragment getItem(int index) {
        return fragmentsList.get(index);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (fragmentTitles != null)
            return fragmentTitles[position];

        return null;
    }

}

