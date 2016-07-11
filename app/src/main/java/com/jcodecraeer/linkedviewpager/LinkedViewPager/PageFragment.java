package com.jcodecraeer.linkedviewpager.LinkedViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.lahphim.mamoon.R;

public class PageFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "pageNumber";

    public static PageFragment create(int pageNumber) {
        PageFragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        Button btItem = (Button) rootView.findViewById(R.id.bt_item);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            btItem.setText(
                    getString(R.string.page_number_1d,
                            arguments.getInt(ARG_PAGE_NUMBER) + 1));
            btItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getString(R.string.page_number_1d,
                                    arguments.getInt(ARG_PAGE_NUMBER) + 1))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        } else {
            btItem.setVisibility(View.GONE);
        }
        return rootView;
    }

}