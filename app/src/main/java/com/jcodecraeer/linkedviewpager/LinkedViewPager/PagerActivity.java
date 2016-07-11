package com.jcodecraeer.linkedviewpager.LinkedViewPager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.lahphim.mamoon.R;

public class PagerActivity extends FragmentActivity {
    private RelativeLayout[] relativeLayouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final MultiViewPager pager = (MultiViewPager) findViewById(R.id.pager);

        final MyAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);

        relativeLayouts = new RelativeLayout[5];
        for(int i=0; i< relativeLayouts.length; i++){
            relativeLayouts[i] = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.fragment_page, null);
        }

    }

    public class MyAdapter extends android.support.v4.view.PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((MultiViewPager) container).removeView(relativeLayouts[position % relativeLayouts.length]);
        }

        /**
         */
        @Override
        public Object instantiateItem(View container, int position) {
            View currentView = relativeLayouts[position % relativeLayouts.length];
            if (currentView.getParent() == null) {
                ((MultiViewPager) container).addView(currentView, 0);
            }
            return currentView;
        }
    }
}