package com.sgmdata.weeg.myapplication;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

import org.xutils.x;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<View> mPagerList;
    private ViewPager myviewpager;
    private TabPageIndicator indicator;
    private String[] Tapinfo={"实时数据","运时统计","通信数据","统计信息","阀控状态","单月数据","单月阀门","设置状态","运行状态","阀控记录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPagerList =new ArrayList<View>();
        addviewpager();
        myviewpager=(ViewPager)super.findViewById(R.id.viewpager);
        myviewpager.setAdapter(new MyviewPageradpater());
        indicator= (TabPageIndicator) super.findViewById(R.id.titles); //TabPageIndicator TitlePageIndicator titles
        indicator.setViewPager(myviewpager);
        x.Ext.init(getApplication());
  //      x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

    private void addviewpager() {
        mPagerList.add(new currentdata(this).initViews());
        mPagerList.add(new detailview(this).initViews());
        mPagerList.add(new GetCmmdLogView(this).initViews());
        mPagerList.add(new GetCmmStatusdataView(this).initViews());
        mPagerList.add(new GetCmmControlView(this).initViews());
        mPagerList.add(new GetHisMonthdataView(this).initViews());
        mPagerList.add(new GetHisSwitchMonLogView(this).initViews());
        mPagerList.add(new GetParammeterCmdView(this).initViews());
        mPagerList.add(new GetSGMPStatusdata(this).initViews());
        mPagerList.add(new GetSGMPSwitchLogDataView(this).initViews());
    }

    class MyviewPageradpater extends PagerAdapter
    {
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View pager = mPagerList.get(position);
            container.addView(pager);
            return pager;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
             return Tapinfo[position];
        }
    }
}

