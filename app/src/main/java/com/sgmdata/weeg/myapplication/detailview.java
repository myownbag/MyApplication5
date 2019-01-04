package com.sgmdata.weeg.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.body.InputStreamBody;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/20.
 */

public class detailview extends BasePager {
    //TextView listviewinfo;
   // private String temptest = "http://192.168.1.232:8090/WeegSpring/registerDevice?verifyCode=\%22863703030097377\%22&nodeId=\%22863703030097377\%22";
    private LinearLayout footView;
    Button testbut=null;
    TextView atstarttime=null;
    TextView atendtime=null;
    TextView getstarttime=null;
    TextView getendtime=null;
    itemadpater listadpater=null;
    String startinfo;
    String endinfo;
    EditText seriup;
    ListView showitems;
    ArrayList<ActimeCommData> mydata=new ArrayList<ActimeCommData>();
    public detailview(Activity activity) {
        super(activity);
        initViews();
        initdata();
    }

    @Override
    public View initViews() {
        if(view==null)
            view =View.inflate(mActivity,R.layout.detail,null);
            seriup=(EditText)view.findViewById(R.id.serdevice);
            testbut=(Button)view.findViewById(R.id.testbut);
            showitems=(ListView)view.findViewById(R.id.activitydatalist);
            footView = (LinearLayout) LinearLayout.inflate(mActivity,R.layout.listfoot, null);//得到尾部的布局
           // listviewinfo=(TextView) footView.findViewById(R.id.listviewtotle);
                showitems.addFooterView(footView);
            listadpater=new itemadpater();
            showitems.setAdapter(listadpater);
            //Utility.setListViewHeightBasedOnChildren(showitems);
        testbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           SimpleDateFormat test1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
           Date d1,d2;
                d1=new Date();
                d2=new Date();
                try {
                     d1=test1.parse(startinfo);
                  //Log.d("zl","start:"+d1.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                     d2=test1.parse(endinfo);
                  // Log.d("zl","End:"+d2.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(d2.getTime()>d1.getTime())
                {
                    if(seriup.getText().toString().length()==12)
                    {
                        mydata.clear();
                        test(seriup.getText().toString(),startinfo,endinfo);
                    }
                }
            }
        });
        atstarttime=(TextView)view.findViewById(R.id.showinfo);
        atendtime  =(TextView)view.findViewById(R.id.showinfo1);
        getstarttime=(TextView)view.findViewById(R.id.starttime);
        getendtime  =(TextView)view.findViewById(R.id.endtime);
        atstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDlg startDlg=new MyDlg(mActivity);
                startDlg.setTitle("请设置开始日期及时间");
                startDlg.SetOnOnsetfinishedlistener(new MyDlg.Onsetfinishedlistener() {
                    @Override
                    public void settingsget(String datePickerset, String timePickerset) {
                        startinfo=datePickerset+"T"+timePickerset;
                       // Log.d("zl","start:"+startinfo);
                        getstarttime.setText(startinfo);
                    }
                });
                startDlg.show();
            }
        });
        atendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDlg mydlg=new MyDlg(mActivity);
                mydlg.setTitle("请设置结束日期及时间");
              mydlg.SetOnOnsetfinishedlistener(new settingdatelisterner());
                mydlg.show();
            }
        });
        return view;
    }
    public void initdata()
    {

    }
    public void test(String ser,String start,String end)
    {
        InputStream in=null;
        in=mActivity.getClass().getClassLoader().getResourceAsStream("assets/actimecomm.xml");
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
        String myxml=String.format(xml,ser,start,end);
       // String myxml=String.format(xml,"201611002291","2017-10-24T00:00:00","2017-10-24T10:00:00");
        String url ="http://112.21.191.16:8000/ProductWebservice/ProductInterface.asmx?op=GetSGMPActimeCommData";
        RequestParams params = new RequestParams(url);
        params.setHeader("Host","112.21.191.16");
        params.setRequestBody(new InputStreamBody(new ByteArrayInputStream(myxml.getBytes()),"application/soap+xml; charset=utf-8"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
              // Log.d("zl",result);
                String temp;
                xmldecodedemo(result);
                listadpater.notifyDataSetChanged();
                //va.notifyDataSetChanged();
//                Log.d("zl","totle:"+mydata.size());
//                for(int i=0;i<mydata.size();i++)
//                {
//                    Log.d("zl",mydata.get(i).devserial+mydata.get(i).devdata8+mydata.get(i).recvtime);
//                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity,"出错了",Toast.LENGTH_SHORT).show();
                Log.d("zl",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    class settingdatelisterner implements MyDlg.Onsetfinishedlistener
    {
        int curour=-1;
        int curmin=-1;


        @Override
        public void settingsget(String datePickerset, String timePickerset) {
            endinfo=datePickerset+"T"+timePickerset;
            //Log.d("zl","end:"+startinfo);
            getendtime.setText(endinfo);
        }
    }
    class  itemadpater extends BaseAdapter
    {

        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
            {

            }
            convertView=View.inflate(mActivity,R.layout.detailitem,null);
            if(mydata.size()>0)
            {

                TextView serview=(TextView) convertView.findViewById(R.id.detailserid);
                serview.setText(mydata.get(position).devserial);
                TextView timerview=(TextView) convertView.findViewById(R.id.updatetime1);
                timerview.setText(mydata.get(position).recvtime);
                TextView gatestate=(TextView)convertView.findViewById(R.id.detailitemgate);
                gatestate.setText(mydata.get(position).devdata8);
                TextView meterdata=(TextView)convertView.findViewById(R.id.detailitemmeterdate);
                meterdata.setText(mydata.get(position).devdata5);
            }

            return convertView;
        }
    }
    private void xmldecodedemo(String string)
    {
        ActimeCommData actimeCommData=null;
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
                    if("sgmp_activecomm_data".equals(elementName))
                    {
                        actimeCommData=new ActimeCommData();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    elementName=parser.getName();
                    if("sgmp_activecomm_data".equals(elementName))
                    {
                        mydata.add(actimeCommData);
                    }
                    break;
                case XmlPullParser.TEXT:

                    if("devserial".equals(elementName))
                    {
                        actimeCommData.devserial=parser.getText();
                    }
                    if("devdata1".equals(elementName))
                    {
                        actimeCommData.devdata1=parser.getText();
                    }
                    if("devdata2".equals(elementName))
                    {
                        actimeCommData.devdata2=parser.getText();
                    }
                    if("devdata5".equals(elementName))
                    {
                        actimeCommData.devdata5=parser.getText();
                    }
                    if("devdata8".equals(elementName))
                    {
                        String temp=parser.getText();
                        if(temp.equals("85"))
                            actimeCommData.devdata8="阀门开";
                        else if(temp.equals("153"))
                            actimeCommData.devdata8="阀门关";//map.put("infos","阀门关");
                        else
                            actimeCommData.devdata8="阀门异常";// map.put("infos","阀门异常");
                    }
                    if("devdata6".equals(elementName))
                    {
                        actimeCommData.devdata6=parser.getText();

                    }
                    if("recvtime".equals(elementName))
                    {
                        actimeCommData.recvtime=parser.getText();
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
    /**
     * 日期时间选择框框
     *
     * @param defDate
     *            默认显示时间
     * @param minDate
     *            最小时间限制
     * @param maxDate
     *            最大时间限制
     * @param activity
     *            容器
     * @param listener
     *            选完时间后的回调
     * @param title
     *            标题
     * @throws Exception
     */

//    public static void showDateTime(Date defDate, final Date minDate, final Date maxDate, final Activity activity,
//                                    final DialogCloseListener listener, final String title) throws Exception {
//        View viewDialog = activity.getLayoutInflater().inflate(R.layout.dialog_datetime, null);
//        final DatePicker datePicker = (DatePicker) viewDialog.findViewById(R.id.datePicker);
//        final TimePicker timePicker = (TimePicker) viewDialog.findViewById(R.id.timePicker);
//        final TextView txtDateTime = (TextView) viewDialog.findViewById(R.id.txtDateTime);
//        txtDateTime.setText("请设置" + title);
//        final Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat format = DateUtil.formatDate();
//        if (null != defDate) {
//            calendar.setTime(defDate);
//        }
//        int year = calendar.get(Calendar.YEAR);
//        int monthOfYear = calendar.get(Calendar.MONTH);
//        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, monthOfYear);
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                txtDateTime.setText(title + "：" + DateUtil.formatDateTimeJieStr(calendar.getTime()));
//            }
//        });
//        if (null != minDate) {
//            datePicker.setMinDate(format.parse(format.format(minDate)).getTime());
//        }
//        if (null != maxDate) {
//            datePicker.setMaxDate(format.parse(format.format(maxDate)).getTime());
//        }
//        timePicker.setIs24HourView(true);
//        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
//        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                calendar.set(Calendar.MINUTE, minute);
//                txtDateTime.setText(title + "：" + DateUtil.formatDateTimeJieStr(calendar.getTime()));
//                if (null != minDate && DateUtil.getDateDiff(calendar.getTime(), minDate) > 0) {
//                    resetDate(minDate);
//                }
//                if (null != maxDate && DateUtil.getDateDiff(maxDate, calendar.getTime()) > 0) {
//                    resetDate(maxDate);
//                }
//            }
//
//            private void resetDate(Date date) {
//                calendar.setTime(date);
//                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
//                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
//            }
//        });
//        viewDialog.findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                closeDialog();
//            }
//        });
//        viewDialog.findViewById(R.id.btnConfirm).setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                closeDialog();
//                if (null != listener) {
//                    listener.onClose(ToolUtil.createMap(new String[] { "date" }, new Object[] { calendar.getTime() }));
//                }
//            }
//        });
//        int width = LjhooUtil.size[0].multiply(BigDecimal.valueOf(0.9)).intValue();
//        dialog = new Dialog(activity, R.style.dialog);
//        dialog.setContentView(viewDialog, new LayoutParams(width, LayoutParams.WRAP_CONTENT));
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//    }
}
