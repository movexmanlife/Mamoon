package com.jcodecraeer.linkedviewpager.LinkedViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.lahphim.mamoon.R;

public class PagerActivityTest extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final MultiViewPager pager = (MultiViewPager) findViewById(R.id.pager);

        final FragmentStatePagerAdapter adapter = new MyAdapter();
        pager.setAdapter(adapter);
    }

    /**
     * 07-11 17:08:28.315 5036-5036/io.lahphim.mamoon E/AndroidRuntime: FATAL EXCEPTION: main
     java.lang.IllegalStateException: Fragment already added: PageFragment{42096eb8 #0 id=0x7f080051}
     at android.support.v4.app.FragmentManagerImpl.addFragment(FragmentManager.java:1209)
     at android.support.v4.app.BackStackRecord.run(BackStackRecord.java:674)
     at android.support.v4.app.FragmentManagerImpl.execPendingActions(FragmentManager.java:1501)
     at android.support.v4.app.FragmentManagerImpl.executePendingTransactions(FragmentManager.java:490)
     at android.support.v4.app.FragmentStatePagerAdapter.finishUpdate(FragmentStatePagerAdapter.java:163)
     at android.support.v4.view.ViewPager.populate(ViewPager.java:1105)

     http://stackoverflow.com/questions/6976027/reusing-fragments-in-a-fragmentpageradapter

     http://stackoverflow.com/questions/14035090/how-to-get-existing-fragments-when-using-fragmentpageradapter?noredirect=1&lq=1
     */
    private class MyAdapter extends FragmentStatePagerAdapter {
        private List<PageFragment> lists = new ArrayList<PageFragment>();

        public MyAdapter() {
            super(getSupportFragmentManager());
            lists.add(PageFragment.create(0));
            lists.add(PageFragment.create(1));
            lists.add(PageFragment.create(2));
            lists.add(PageFragment.create(3));
            lists.add(PageFragment.create(4));
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println(position);
            return lists.get(position % 5);
        }
    }
}