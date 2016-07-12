package com.jcodecraeer.linkedviewpager.LinkedViewPager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import io.lahphim.mamoon.R;

/**
 * 成功的实现了TextView显示不同的日期
 */
public class PagerActivityOk extends FragmentActivity {
    private RelativeLayout[] relativeLayouts;
    private int mCurrentYearMonthPosition = Integer.MAX_VALUE / 2;
    private ViewPager main_scrolllayout;
    private MyPagerAdapter mFramePageAdapter;
    private ArrayList<View> mFramePageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_ok);

        final MultiViewPagerOk pager = (MultiViewPagerOk) findViewById(R.id.pager);
        main_scrolllayout = (ViewPager) findViewById(R.id.main_scrolllayout);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View frameView1 = inflater.inflate(R.layout.transparent_layer_image, null);
        initFramePagerView(frameView1 ,R.drawable.frame_view1);
        View frameView2 = inflater.inflate(R.layout.transparent_layer_image, null);
        initFramePagerView(frameView2 ,R.drawable.frame_view2);
        View frameView3 = inflater.inflate(R.layout.transparent_layer_image, null);
        initFramePagerView(frameView3 ,R.drawable.frame_view3);
        View frameView4 = inflater.inflate(R.layout.transparent_layer_image, null);
        initFramePagerView(frameView4 ,R.drawable.frame_view4);
        View frameView5 = inflater.inflate(R.layout.transparent_layer_image, null);
        initFramePagerView(frameView5 ,R.drawable.frame_view5);

        mFramePageViews = new ArrayList<>();
        mFramePageViews.add(frameView1);
        mFramePageViews.add(frameView2);
        mFramePageViews.add(frameView3);
        mFramePageViews.add(frameView4);
        mFramePageViews.add(frameView5);

        mFramePageAdapter = new MyPagerAdapter(mFramePageViews);
        main_scrolllayout.setAdapter(mFramePageAdapter);

        final MyAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(mCurrentYearMonthPosition);

        relativeLayouts = new RelativeLayout[9];
        for(int i=0; i< relativeLayouts.length; i++){
            relativeLayouts[i] = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.date_page, null);
        }

        pager.setFollowViewPager(main_scrolllayout);
    }

    public void initFramePagerView(View frameView ,int drawable){
        ImageView image = (ImageView)frameView.findViewById(R.id.image);
        image.setImageResource(drawable);

    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        /**
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // 对ViewPager页号求模取出View列表中要显示的项
            String yearMonth = getYearMonth(position);

            int newPos = position % relativeLayouts.length;
            if (newPos < 0) {
                newPos = relativeLayouts.length + newPos;
            }
            RelativeLayout view = relativeLayouts[newPos];
            TextView textView = (TextView)view.findViewById(R.id.date_text);
            textView.setText(yearMonth);

            // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent viewGroup = view.getParent();
            if (viewGroup != null) {
                ViewGroup parent = (ViewGroup) viewGroup;
                parent.removeView(view);
            }
            container.addView(view);
            // add listeners here if necessary
            return view;
        }
    }

    /**
     * 获取对应的年月值
     * @param position
     * @return
     */
    private String getYearMonth(int position) {
        int value = position - mCurrentYearMonthPosition;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, value);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        return String.valueOf(year) + "年" + String.valueOf(month) + "月";
    }
}