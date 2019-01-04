package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.view.View;

class GetHisSwitchMonLogView extends BasePager {
    View view = null;
    public Activity mActivity;
    public GetHisSwitchMonLogView(Activity activity) {
        super(activity);
        mActivity=activity;
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.get_his_switch_mon_log_layout,null);
        return view;
    }

    private void initdata() {
    }

}
