package com.ika.kof.fragment;

/**
 * Created by Ghifari on 7/6/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ika.kof.Constant;
import com.ika.kof.R;
import com.ika.kof.adapters.RecyclerContact;

public class FragmentContact extends Fragment{

    RecyclerView mRecyclerView;

    private Toast toast;

    private RecyclerContact mRecyclerContact;

    private RecyclerContact.OnArtikelClickListener mOnArtikelClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_numbers);

        inisialisasiListener();

        inisialisasiTampilan();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("VIEW Contact", "FRAGMENT onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("START CONTACT", "FRAGMENT onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("RESUME CONTACT", "FRAGMENT onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("PAUSE CONTACT", "FRAGMENT onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("STOP CONTACT", "FRAGMENT onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("DESTROY VIEW CONTACT", "FRAGMENT onDestroyView");
    }

    private void inisialisasiListener() {
        mOnArtikelClickListener = new RecyclerContact.OnArtikelClickListener() {
            @Override
            public void onClick(int posisi) {
                try {
                    if (toast != null) {
                        toast.cancel();
                    }
                    String message;
                    if (posisi == 0) {
                        message = "Call the Lab";
                        toast = Toast.makeText(FragmentContact.this.getActivity(), message, Toast.LENGTH_LONG);
                        toast.show();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Constant.ika_phone));
                        startActivity(callIntent);

                    } else {
                        message = "Open Email Menu";
                        toast = Toast.makeText(FragmentContact.this.getActivity(), message, Toast.LENGTH_LONG);
                        toast.show();
                         /* Create the Intent */
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                        /* Fill it with Data */
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Constant.ika_email});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Konsultasi Kesehatan");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                        /* Send it off to the Activity-Chooser */
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void inisialisasiTampilan() {
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(FragmentContact.this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerContact = new RecyclerContact(FragmentContact.this.getActivity());
        mRecyclerContact.setOnArtikelClickListener(mOnArtikelClickListener);
        mRecyclerView.setAdapter(mRecyclerContact);
    }

}
