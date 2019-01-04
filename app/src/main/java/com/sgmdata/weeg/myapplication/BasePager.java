package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BasePager {
	View view = null;
	public Activity mActivity;
	//public View mRootView;// 布局对象

	//public TextView tvTitle;// 标题对象

	//public FrameLayout flContent;// 内容

	//public ImageButton btnMenu;// 菜单按钮
	//public ImageButton btnPhoto;// 组图切换按钮

	public BasePager(Activity activity) {
		mActivity = activity;
		//initViews();
	}

	/**
	 * 初始化布局
	 */
	public  View initViews()
	{

		return  view;
	}
	public  View getView()
	{
		return  view;
	}
	public String getXml(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		in.close();
		String result = new String(out.toByteArray(), "utf-8");
		out.close();
		return result;
	}
}
