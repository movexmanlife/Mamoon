package com.jcodecraeer.linkedviewpager.LinkedViewPager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import io.lahphim.mamoon.R;

public class PagerActivityYearMonth extends FragmentActivity {
    private RelativeLayout[] relativeLayouts;
    private int currentDatePosition = Integer.MAX_VALUE / 2;
    private int currentYearMonthValue = getCurrentYearMonthValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final MultiViewPager pager = (MultiViewPager) findViewById(R.id.pager);

        final MyAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentDatePosition);

        relativeLayouts = new RelativeLayout[9];
        for(int i=0; i< relativeLayouts.length; i++){
            relativeLayouts[i] = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.date_page, null);
        }

//        setYearMonth(currentDatePosition);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                position %= relativeLayouts.length;
//                if (position < 0) {
//                    position = relativeLayouts.length + position;
//                }
//                RelativeLayout view = relativeLayouts[position];
//                TextView textView = (TextView)view.findViewById(R.id.date_text);
//                textView.setText(getYearMonth());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setYearMonth(int position) {
        position %= relativeLayouts.length;
        if (position < 0) {
            position = relativeLayouts.length + position;
        }
        RelativeLayout view = relativeLayouts[position];
        TextView textView = (TextView)view.findViewById(R.id.date_text);


//        textView.setText(getYearMonth());
    }

    public class MyAdapter extends android.support.v4.view.PagerAdapter {

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
            int newPosition = position % relativeLayouts.length;
            if (newPosition < 0) {
                newPosition = relativeLayouts.length + newPosition;
            }
            RelativeLayout view = relativeLayouts[newPosition];
            TextView textView = (TextView)view.findViewById(R.id.date_text);

            int value = position - currentDatePosition;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, value);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            textView.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");

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
     * 获取当前的年月
     * @return
     */
    private String getCurrentYearMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;

        return String.valueOf(year) + "年" + String.valueOf(month) + "月";
    }

    /**
     * 获取当前的年月所对应的数值
     * @return
     */
    private int getCurrentYearMonthValue() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;

        return Integer.parseInt(String.valueOf(year) + String.valueOf(month));
    }
}