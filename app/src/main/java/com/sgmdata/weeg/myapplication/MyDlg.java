package com.sgmdata.weeg.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/24.
 */

public class MyDlg extends Dialog {
    DatePicker mydata=null;
    TimePicker mytime=null;
    Onsetfinishedlistener listerner;
    String curdate;
    String curtime;
    public MyDlg(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat    formatter  ;
        formatter    =   new SimpleDateFormat("HH:mm:ss");
        curtime=formatter.format(date);
        formatter    =   new SimpleDateFormat("yyyy-MM-dd");
        curdate=formatter.format(date);
        setContentView(R.layout.dateandtimerpicker);
        mydata=super.findViewById(R.id.dateselect);
        mytime=super.findViewById(R.id.timeselect);
//        mydata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                curdate= ""+mydata.getYear()+"-"+mydata.getMonth()
//                        +"-"+mydata.getDayOfMonth();
//            }
//        });
        //mydata.updateDate(date.getYear(),date.getMonth(),date.getDate());
        mytime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
        {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hourtemp;
                String minutetemp;
                if(hourOfDay<10)
                {
                    hourtemp="0"+hourOfDay;
                }
                else
                {
                    hourtemp=""+hourOfDay;
                }
                if(minute<10)
                {
                    minutetemp="0"+minute;
                }
                else
                {
                    minutetemp=""+minute;
                }
                curtime=""+hourtemp+":"+minutetemp+":"+"00";
//                if(hourOfDay>10)
//                     curtime=""+hourOfDay+":"+minute+":"+"00";
//                else
//                    curtime="0"+hourOfDay+":"+minute+":"+"00";
            }
        });
        Button but=(Button) super.findViewById(R.id.selected);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strmonth;
                String strday;
                if(mydata.getDayOfMonth()<10)
                {
                    strday="0"+mydata.getDayOfMonth();
                }
                else
                {
                    strday=""+mydata.getDayOfMonth();
                }
                if((mydata.getMonth()+1)<10)
                {
                    strmonth="0"+(mydata.getMonth()+1);
                }
                else
                {
                    strmonth=""+(mydata.getMonth()+1);
                }
                if(MyDlg.this.listerner!=null)
                {
                    MyDlg.this.listerner.settingsget(""+mydata.getYear()+"-"+strmonth
                            +"-"+strday,curtime);
                }
                dismiss();
            }
        });
    }

    interface Onsetfinishedlistener
    {
        void settingsget(String  datePickerset, String timePickerset);
    };
    public void SetOnOnsetfinishedlistener(Onsetfinishedlistener listerner)
    {
        this.listerner=listerner;
    }
}
