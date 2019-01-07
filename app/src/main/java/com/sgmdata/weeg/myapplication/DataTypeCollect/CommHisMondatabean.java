package com.sgmdata.weeg.myapplication.DataTypeCollect;

import java.util.ArrayList;
import java.util.Map;

public class CommHisMondatabean {
//    public String devserial;
//    public String chunnelserial;
//    public String metercode;
    public ArrayList<Map<String,String>> Mondata;

    public CommHisMondatabean(){
        Mondata = new ArrayList<>();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Mondata=null;
    }
}
