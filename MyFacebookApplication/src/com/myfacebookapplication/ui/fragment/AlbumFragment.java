package com.myfacebookapplication.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.myfacebookapplication.R;

public class AlbumFragment extends Fragment {
	private View mContentView;
	private GridView gridAlbum;

	// Array of strings storing country names
	String[] countries = new String[] { "India", "Pakistan", "Sri Lanka",
			"China", "Bangladesh", "Nepal", "Afghanistan", "North Korea",
			"South Korea", "Japan" };

	// Array of integers points to images stored in /res/drawable-ldpi/
	int[] flags = new int[] { R.drawable.india, R.drawable.pakistan,
			R.drawable.srilanka, R.drawable.china, R.drawable.bangladesh,
			R.drawable.nepal, R.drawable.afghanistan, R.drawable.nkorea,
			R.drawable.skorea, R.drawable.japan };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContentView = inflater.inflate(R.layout.album_frag, null);
		gridAlbum = (GridView) mContentView.findViewById(R.id.grid_album);
		doLoadGridWithAlbums();
		return mContentView;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void doLoadGridWithAlbums() {

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < 10; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("txt", countries[i]);
			hm.put("flag", Integer.toString(flags[i]));
			aList.add(hm);
		}

		String[] from = { "flag", "txt" };
		int[] to = { R.id.item_image, R.id.item_text };

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList,
				R.layout.griditem, from, to);

		gridAlbum.setAdapter(adapter);

	}

}
