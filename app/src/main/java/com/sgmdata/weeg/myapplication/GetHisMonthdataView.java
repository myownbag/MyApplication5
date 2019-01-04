package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.view.View;

public class GetHisMonthdataView extends BasePager {

    View view = null;
    public Activity mActivity;

    public GetHisMonthdataView(Activity activity) {
        super(activity);
        mActivity=activity;
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.get_his_mon_data_layout,null);
        return view;
    }

    private void initdata() {
    }
}
