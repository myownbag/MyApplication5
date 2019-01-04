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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.body.InputStreamBody;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Administrator on 2017/10/20.
 */

public class currentdata extends BasePager {
    ListView currentview=null;
    String itemtext[]={"表头序号","当前读数","阀门状态","信号强度","电池电压","内嵌版本","上传时间"};
    EditText sernum=null;
    Button datasearchbut=null;
    InputStream in=null;
    viewadpater va=null;
    ArrayList<Map<String,String>> content=new ArrayList<Map<String,String>>();

    public currentdata(Activity activity) {
        super(activity);
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view=View.inflate(mActivity,R.layout.current_show,null);
        currentview=(ListView)view.findViewById(R.id.currentshow);
        sernum=(EditText)view.findViewById(R.id.sernum);
        datasearchbut=(Button)view.findViewById(R.id.getcurrentbut);

        return view;
    }
    public void initdata()
    {

        int i=0;
        content.clear();
        for(i=0;i<itemtext.length;i++)
        {
            Map<String,String> map=new HashMap<String, String>();
            map.put("items",itemtext[i]);
            map.put("infos","");
            //map.put("infos","2017-10-23 10:22:30");
            content.add(map);
        }
        va=new viewadpater();
        currentview.setAdapter(va);
        datasearchbut.setOnClickListener(new butclickedlistenerimp());
    }
    class viewadpater extends BaseAdapter
    {

        @Override
        public int getCount() {
            return content.size();
        }

        @Override
        public Object getItem(int i) {
            return content.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=View.inflate(mActivity,R.layout.current_item,null);
            }
            TextView itemsshow=(TextView) view.findViewById(R.id.currentkey);
            TextView valuesshow=(TextView) view.findViewById(R.id.currentvalue);
            itemsshow.setText(content.get(i).get("items"));
            valuesshow.setText(content.get(i).get("infos"));
            return view;
        }
    }
    class butclickedlistenerimp implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.getcurrentbut:
                    //Toast.makeText(mActivity,"test",Toast.LENGTH_SHORT).show();

                    int i=0;
                    for(i=0;i<content.size();i++)
                    {
                        Map<String,String> map=content.get(i);
                        map.remove("infos");
                        map.put("infos","");
                        content.set(i,map);
                    }
                    va.notifyDataSetChanged();
                    accesswebservice();
                    break;
            }
        }
    }

    private void accesswebservice() {
        in=mActivity.getClass().getClassLoader().getResourceAsStream("assets/mysoap.xml");
        String xml = null;
        try {
            xml = getXml(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(xml==null)
        {
            return;
        }
        String testert=sernum.getText().toString();
        if(testert.isEmpty())
        {
            Toast.makeText(mActivity,"请输入表具号",Toast.LENGTH_SHORT).show();
            return;
        }
        String myxml=String.format(xml,testert);
        String url = "http://112.21.191.16:8000/ProductWebservice/ProductInterface.asmx?op=GetSGMPCurrentData";
        RequestParams params = new RequestParams(url);
        params.setHeader("Host","112.21.191.16");
        //Content-Type: application/soap+xml; charset=utf-8
        params.setRequestBody(new InputStreamBody(new ByteArrayInputStream(myxml.getBytes()),"application/soap+xml; charset=utf-8"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //Log.d("zl",result);
                xmldecodedemo(result);
                va.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity,"出错了",Toast.LENGTH_SHORT).show();
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
                    break;
                case XmlPullParser.END_TAG:

                    break;
                case XmlPullParser.TEXT:
                    if("devserial".equals(elementName))
                    {
                        Log.d("zl","devserial="+parser.getText());
                        Map<String,String> map=content.get(0);
                        map.remove("infos");
                        map.put("infos",parser.getText());
                        content.set(0,map);
                    }
                    if("devdata5".equals(elementName))
                    {
                        Log.d("zl","devdata5="+parser.getText());
                        Map<String,String> map=content.get(1);
                        map.remove("infos");
                        map.put("infos",parser.getText());
                        content.set(1,map);
                    }
                    if("devdata8".equals(elementName))
                    {
                        String temp=parser.getText();
                        Log.d("zl","devdata8="+temp);
                        Map<String,String> map=content.get(2);
                        map.remove("infos");
                        if(temp.equals("85"))
                            map.put("infos","阀门开");
                        else if(temp.equals("153"))
                            map.put("infos","阀门关");
                        else
                            map.put("infos","阀门异常");
                        content.set(2,map);
                    }
                    if("devdata6".equals(elementName))
                    {
                        Log.d("zl","devdata6="+parser.getText());
                        Map<String,String> map=content.get(3);
                        map.remove("infos");
                        map.put("infos",parser.getText());
                        content.set(3,map);

                    }
                    if("devdata2".equals(elementName))
                    {
                        Log.d("zl","devdata2="+parser.getText());
                        Map<String,String> map=content.get(4);
                        map.remove("infos");
                        map.put("infos",parser.getText());
                        content.set(4,map);

                    }
                    if("devdata7".equals(elementName))
                    {
                        Log.d("zl","devdata7="+parser.getText());
                        Map<String,String> map=content.get(5);
                        map.remove("infos");
                        int tempver = Integer.valueOf(parser.getText());
                        String tempstr = String.format("%x",tempver);
                        map.put("infos",tempstr);
                        content.set(5,map);
                    }
                    if("updatedaytime".equals(elementName))
                    {
                        Log.d("zl","updatedaytime="+parser.getText());
                        Map<String,String> map=content.get(6);
                        map.remove("infos");
                        map.put("infos",parser.getText());
                        content.set(6,map);
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
