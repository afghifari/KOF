package com.ika.kof.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ika.kof.R;

/**
 * Created by Ghifari on 7/6/2017.
 */

public class FragmentBluetooth extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        return rootView;
    }
}