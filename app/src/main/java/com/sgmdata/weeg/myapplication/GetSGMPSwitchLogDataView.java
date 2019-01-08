package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgmdata.weeg.myapplication.DataTypeCollect.CommSGMPStatusDataBean;
import com.sgmdata.weeg.myapplication.DataTypeCollect.CommSGMPSwitchLogDataBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class GetSGMPSwitchLogDataView extends BasePager {
    View view = null;
    public Activity mActivity;
    public Button mBut;
    public EditText mserial;
    public ListView mlistinfo;
    public ArrayList<CommSGMPSwitchLogDataBean> mydata;
    public listviewadpater1 myadpater;
    public GetSGMPSwitchLogDataView(Activity activity) {
        super(activity);
        mActivity=activity;
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.get_cmmd_control_layout,null);
        mserial = view.findViewById(R.id.comm_control_data_sgmpserial);
        mBut = view.findViewById(R.id.comm_control_data_but);
        mlistinfo = view.findViewById(R.id.comm_control_data_list);
        return view;
    }

    private void initdata() {
        mydata=new ArrayList<>();
        myadpater = new listviewadpater1();
        mBut.setOnClickListener( new butchicklisternerimpl1());
        mlistinfo.setAdapter(myadpater);
    }

    private class butchicklisternerimpl1 implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mydata.clear();
            myadpater.notifyDataSetChanged();
            accesshttppost();
        }
    }

    private class listviewadpater1 extends BaseAdapter
    {

        @Override
        public int getCount() {
            if(mydata==null)
            {
                return 0;
            }
            else
            {
                return mydata.size();
            }
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
                view= View.inflate(mActivity,R.layout.get_sgmp_switch_log_item_layout,null);

            TextView devserial=view.findViewById(R.id.comm_sgmp_status_data_devserial);
            TextView cmdflag = view.findViewById(R.id.comm_sgmp_status_data_cmdflag);
            TextView cmdsource = view.findViewById(R.id.comm_sgmp_status_data_cmdsource );
            TextView cmdpacket = view.findViewById(R.id.comm_sgmp_status_data_cmdpacket);
            TextView cmdtime= view.findViewById(R.id.comm_sgmp_status_data_cmdtime);
            TextView cmdnote = view.findViewById(R.id.comm_sgmp_status_data_cmdnote);


            devserial.setText(mydata.get(i).devserial);
            cmdflag.setText(mydata.get(i).cmdflag);
            cmdsource.setText(mydata.get(i).cmdsource);
            cmdpacket.setText(mydata.get(i).cmdpacket);
            cmdtime.setText(mydata.get(i).cmdtime);
            cmdnote.setText(mydata.get(i).cmdnote);
            return view;
        }
    }

    private void accesshttppost() {

        String testert=mserial.getText().toString();
        if(testert.isEmpty())
        {
            Toast.makeText(mActivity,"请输入表具号",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://112.21.191.16:8000/SGMPServices/SgmpInfoInterface.asmx/GetSGMPSwitchLogData?userid=root&userpasswd=weeg@20080725&sgmpserial=%s";
        url= String.format(url,testert);
        Log.d("zl","URL"+url);
        RequestParams params = new RequestParams(url);
        params.setHeader("Host","112.21.191.16");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("zl",result);
                xmldecodedemo(result);
//                Log.d("zl","count:"+mydata.size());

//                CommStatusBean temp;
//                temp=mydata.get(0);
//                Log.d("zl","1"+temp.chunnelserial+temp.devserial);
                myadpater.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("zl","ERROR:"+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void xmldecodedemo(String string)
    {
        CommSGMPSwitchLogDataBean actimeCommData=null;
        int event=-1;
        String elementName=null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(string));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            event = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        while (event!= XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    elementName=parser.getName();
                    if("sgmp_switch_log".equals(elementName))
                    {
                        actimeCommData=new CommSGMPSwitchLogDataBean();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    elementName=parser.getName();
                    if("sgmp_switch_log".equals(elementName))
                    {
                        mydata.add(actimeCommData);
                    }
                    break;
                case XmlPullParser.TEXT:
                    if(parser.getText().indexOf("\n")==-1)
                    {
                        if("devserial".equals(elementName))
                        {
                            actimeCommData.devserial =parser.getText();
                        }
                        else if("cmdflag".equals(elementName))
                        {
                            actimeCommData.cmdflag =parser.getText();
                        }
                        else if("cmdsource".equals(elementName))
                        {
                            actimeCommData.cmdsource =parser.getText();
                        }
                        else if("cmdnote".equals(elementName))
                        {
                            actimeCommData.cmdnote =parser.getText();
                        }
                        else if("cmdpacket".equals(elementName))
                        {
                            actimeCommData.cmdpacket =parser.getText();
                        }
                        else if("cmdtime".equals(elementName))
                        {
                            actimeCommData.cmdtime =parser.getText();
                        }
                    }
                    break;

            }
            try {
                event = parser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
