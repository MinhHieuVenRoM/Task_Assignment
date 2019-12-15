package com.appsnipp.loginsamples.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.chat.ChatFragment;
import com.appsnipp.loginsamples.conclude.SummaryAttendanceFragment;
import com.appsnipp.loginsamples.conclude.SummaryTaskFragment;

import java.util.ArrayList;


public class SetionsPagerAdapterSummary extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.attendance, R.string.task};
    private final Context mContext;
    private ArrayList<Fragment> mListFragment = new ArrayList<>();

    private void initFragment(){
        mListFragment.add(new SummaryAttendanceFragment());
        mListFragment.add(new SummaryTaskFragment());
    }

    public SetionsPagerAdapterSummary(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        initFragment();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mListFragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}
