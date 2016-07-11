package com.jcodecraeer.linkedviewpager.LinkedViewPager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import io.lahphim.mamoon.R;

public class PagerActivityTextView extends FragmentActivity {
    private TextView[] mTextViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final MultiViewPager pager = (MultiViewPager) findViewById(R.id.pager);

        final MyAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);

        mTextViews = new TextView[5];
        for(int i=0; i< mTextViews.length; i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setText("data" + i);
            mTextViews[i] = textView;
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
            ((MultiViewPager) container).removeView(mTextViews[position % mTextViews.length]);

        }

        /**
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((MultiViewPager) container).addView(mTextViews[position % mTextViews.length], 0);
            return mTextViews[position % mTextViews.length];
        }
    }
}