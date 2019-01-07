package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.sgmdata.weeg.myapplication.DataTypeCollect.CommHisMondatabean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

class GetHisSwitchMonLogView extends BasePager {

    View view = null;
    public Activity mActivity;
    public TimePickerView pvTime;

    public Button mBut;
    public EditText mserial;
    public ListView mlistinfo;
    public TextView mtimeSelecttextview;

    public ArrayList<CommHisMondatabean> mydata;
    public ctrlistviewadpater myadpater;

    public GetHisSwitchMonLogView(Activity activity) {
        super(activity);
        mActivity=activity;
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.get_his_mon_data_layout,null);
        mserial = view.findViewById(R.id.comm_status_data_sgmpserial);
        mBut = view.findViewById(R.id.comm_status_data_but);
        mlistinfo = view.findViewById(R.id.comm_status_data_list);
        Createdatapicker();
        mtimeSelecttextview = view.findViewById(R.id.get_his_data_mon_id);
        mtimeSelecttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

        mydata = new ArrayList<>();
        myadpater = new ctrlistviewadpater();

        mlistinfo.setAdapter(myadpater);
        return view;
    }
    private void Createdatapicker() {
        Calendar calendar=null;
        calendar= Calendar.getInstance();
        pvTime = new TimePickerBuilder(mActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String datastring;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                String dateString = formatter.format(date);
                datastring=dateString.substring(0,6);
                mtimeSelecttextview.setText(datastring);
            }
        })
                .setDate(calendar)
                .setType(new boolean[]{true,true,false,false,false,false})
                .setLabel("年","月",null,null,null,null)
                .build();
    }
    private void initdata() {
        mserial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(b)
                {
                    imm.hideSoftInputFromWindow(((Activity)mActivity).getWindow().getDecorView().getWindowToken(), HIDE_NOT_ALWAYS);
                }
                else
                {
                    imm.hideSoftInputFromWindow(((Activity)mActivity).getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });
        mBut.setOnClickListener(new butchicklisternerimpl1());
    }
    private class butchicklisternerimpl1 implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            mydata.clear();
            myadpater.notifyDataSetChanged();
            accesshttppost();
        }
    }
    private class ctrlistviewadpater extends BaseAdapter
    {

        @Override
        public int getCount() {
            int count;
            if(mydata.size()==0)
            {
                count=0;
            }
            else
            {
                count=mydata.get(0).Mondata.size();
            }
//           Log.d("zl","On adpater getcount:"+count);
            return count;
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(0).Mondata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
                view= View.inflate(mActivity,R.layout.get_his_mon_data_item_layout,null);
            TextView label = view.findViewById(R.id.get_his_mom_data_item_lables);
            TextView detials = view.findViewById(R.id.get_his_mom_data_item_details);
            label.setText(mydata.get(0).Mondata.get(i).get("label"));
            detials.setText(mydata.get(0).Mondata.get(i).get("detail"));
//            Log.d("zl","on adpater" + mydata.get(0).Mondata.get(i).get("label")+"--"+mydata.get(0).Mondata.get(i).get("detail"));
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
        String Mondatetime=mtimeSelecttextview.getText().toString();
        if(Mondatetime.isEmpty())
        {
            Toast.makeText(mActivity,"请选择查询年月",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://112.21.191.16:8000/SGMPServices/SgmpInfoInterface.asmx/GetSGMPHisMonthSwitchData?userid=root&userpasswd=weeg@20080725&sgmpserial=%s&month=%s";
        url= String.format(url,testert,Mondatetime);
        Log.d("zl","URL"+url);
        RequestParams params = new RequestParams(url);
        params.setHeader("Host","112.21.191.16");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("zl",result);
                xmldecodedemo(result);
                Log.d("zl",""+mydata.size());
                Log.d("zl",""+mydata.get(0).Mondata.size());
                myadpater.notifyDataSetChanged();
//                Log.d("zl","finish  myadpater.notifyDataSetChanged(); ");
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
        CommHisMondatabean actimeCommData=null;
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
                    if("sgmp_his_switch".equals(elementName))
                    {
                        actimeCommData=new CommHisMondatabean();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    elementName=parser.getName();
                    if("sgmp_his_switch".equals(elementName))
                    {
                        mydata.add(actimeCommData);
                    }
                    break;
                case XmlPullParser.TEXT:
                    if(parser.getText().indexOf("\n")==-1)
                    {
                        Map<String,String> map;
                        map = new HashMap<>();
                        map.put("label",elementName);
                        String gateinfo = parser.getText();
                        if(gateinfo.equals("85"))
                            map.put("detail","开");
                        else if(gateinfo.equals("153"))
                            map.put("detail","关");
                        else
                            map.put("detail",gateinfo);
                        actimeCommData.Mondata.add(map);
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
