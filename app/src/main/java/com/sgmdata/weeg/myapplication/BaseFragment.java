package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment鍩虹�?
 * 
 * @author Kevin
 * 
 */
public abstract class BaseFragment extends Fragment {

	public Activity mActivity;
	
	// fragment
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	//
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initViews();
	}

	//
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}

	// 抽象函数必须实现
	public abstract View initViews();

	// 可以实现可以不实现
	public void initData() {

	}

}
