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

import com.sgmdata.weeg.myapplication.DataTypeCollect.CommLogBean;
import com.sgmdata.weeg.myapplication.utilsview.CustomDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class GetCmmdLogView extends BasePager {

    View view = null;
    public Activity mActivity;

    EditText mInputSGMP;
    Button  mBut;
    ListView infolist;
    ArrayList<CommLogBean> mydata;
    listviewadpater myadpater;
    CustomDialog mydlg;

    public GetCmmdLogView(Activity activity) {
        super(activity);
        mActivity=activity;
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.get_cmmd_log_layout,null);

        mInputSGMP = view.findViewById(R.id.commdlog_input_edit);
        mBut = view.findViewById(R.id.commlog_search_but);
        infolist = view.findViewById(R.id.commlog_list_info);

        mydlg = CustomDialog.createProgressDialog(mActivity, 60000, new CustomDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(CustomDialog dialog) {
                dialog.dismiss();
                Toast.makeText(mActivity,"服务器无响应",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void initdata() {
        mBut.setOnClickListener( new butchicklisternerimpl());
        mydata =  new ArrayList<CommLogBean>();
        myadpater = new listviewadpater();
        infolist.setAdapter(myadpater);
    }

    private class butchicklisternerimpl implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mydata.clear();
            myadpater.notifyDataSetChanged();
            accesshttppost();
        }
    }
    private class listviewadpater extends BaseAdapter
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
                view= View.inflate(mActivity,R.layout.comm_log_list_layout,null);
            TextView ser= view.findViewById(R.id.comm_log_list_item_devserial);
            TextView info=view.findViewById(R.id.comm_log_list_item_datainfo);
            TextView infotime = view.findViewById(R.id.comm_log_list_item_datatime);

            ser.setText(mydata.get(i).devserial);
            info.setText(mydata.get(i).datainfo);
            infotime.setText(mydata.get(i).writedaytime);
            return view;
        }
    }
    private void accesshttppost() {

        String testert=mInputSGMP.getText().toString();
        if(testert.isEmpty())
        {
            Toast.makeText(mActivity,"请输入表具号",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://112.21.191.16:8000/SGMPServices/SgmpInfoInterface.asmx/GetSGMPCommLogData?userid=root&userpasswd=weeg@20080725&sgmpserial=%s";
        url= String.format(url,testert);
        RequestParams params = new RequestParams(url);
        params.setHeader("Host","112.21.191.16");
        mydlg.show();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("zl",result);
                xmldecodedemo(result);
//                for(CommLogBean listbean:mydata)
//                {
//                    Log.d("zl","1:"+listbean.devserial+" 2:"+listbean.datainfo+" 3"+listbean.writedaytime);
//                }
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
                mydlg.dismiss();
            }
        });
    }

    private void xmldecodedemo(String string)
    {
        Boolean isstart=false;
        CommLogBean actimeCommData=null;
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
                    if("sgmp_his_comm_log".equals(elementName))
                    {
                        isstart=true;
                        actimeCommData=new CommLogBean();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    elementName=parser.getName();
                    if("sgmp_his_comm_log".equals(elementName))
                    {
                        isstart=false;
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
                        if("datainfo".equals(elementName))
                        {
                            actimeCommData.datainfo =parser.getText();
                        }
                        if("writedaytime".equals(elementName))
                        {
                            actimeCommData.writedaytime =parser.getText();
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
