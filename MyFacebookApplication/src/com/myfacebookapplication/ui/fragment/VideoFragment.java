package com.myfacebookapplication.ui.fragment;

import com.myfacebookapplication.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoFragment extends Fragment {
	private View	mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mContentView = inflater.inflate(R.layout.video_frag, null);

		return mContentView;
		// super.onCreateView(inflater, container, savedInstanceState);
	}

}
