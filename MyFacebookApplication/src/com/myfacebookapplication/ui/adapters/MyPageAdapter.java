package com.myfacebookapplication.ui.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {

	ArrayList<TabItem>	mListFragment;

	public MyPageAdapter(FragmentManager fm) {
		super(fm);
		mListFragment = new ArrayList<TabItem>();
	}

	public void setUpTab(String a_Tag, String a_Text, Fragment a_Fragment) {
		TabItem a_TabItem = new TabItem(a_Fragment, a_Text, a_Tag);
		mListFragment.add(a_TabItem);
	}

	@Override
	public Fragment getItem(int position) {

		if (mListFragment != null) {
			return mListFragment.get(position).mFragment;
		}

		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (mListFragment != null) {
			return mListFragment.get(position).mViewLabel;
		}
		return null;
	}

	@Override
	public int getCount() {
		if (mListFragment != null) {
			return mListFragment.size();
		}
		return 0;
	}

	public class TabItem {
		Fragment	mFragment;
		String		mViewLabel;
		String		mId;

		public TabItem(Fragment a_Fragment, String a_ViewLabel, String a_Id) {
			mFragment = a_Fragment;
			mViewLabel = a_ViewLabel;
			mId = a_Id;
		}
	}

}
