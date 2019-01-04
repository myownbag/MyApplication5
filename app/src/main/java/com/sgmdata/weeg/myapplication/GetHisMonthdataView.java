package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class GetHisMonthdataView extends BasePager {

    View view = null;
    public Activity mActivity;

    public Button mBut;
    public EditText mserial;
    public ListView mlistinfo;
    public TextView mHeadviewdevserial;
    public TextView mHeadviewchunnelserial;
    public TextView mHeadviewmetercode;

    public GetHisMonthdataView(Activity activity) {
        super(activity);
        mActivity=activity;
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.get_cmmd_status_data_layout,null);
        mserial = view.findViewById(R.id.comm_status_data_sgmpserial);
        mBut = view.findViewById(R.id.comm_status_data_but);
        mlistinfo = view.findViewById(R.id.comm_status_data_list);
        return view;
    }

    private void initdata() {
            View headview = View.inflate(mActivity,R.layout.get_his_mon_data_head_layout,null);
            mHeadviewdevserial = headview.findViewById(R.id.get_his_mom_data_head_chunnelserial);
            mHeadviewchunnelserial = headview.findViewById(R.id.get_his_mom_data_head_chunnelserial);
            mHeadviewmetercode  = headview.findViewById(R.id.get_his_mom_data_head_metercode);

            mlistinfo.addHeaderView(headview);
    }
}
