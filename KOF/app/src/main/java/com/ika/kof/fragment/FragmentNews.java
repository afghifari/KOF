package com.ika.kof.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ika.kof.Aktifitas.WebviewDetil;
import com.ika.kof.Constant;
import com.ika.kof.R;
import com.ika.kof.adapters.RecyclerNews;

/**
 * Created by user on 7/6/2017.
 * error converting bytecode to dex exception parsing classes
 */

public class FragmentNews extends Fragment {

    RecyclerView mRecyclerView;

    private Toast toast;

    private RecyclerNews mRecyclerNews;

    private RecyclerNews.OnArtikelClickListener mOnArtikelClickListener;
    private RecyclerNews.OnButtonShareClickListener mOnButtonShareClickListener;

    private String juduartikel = "";
    private String linkartikel = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        Log.d("FragmentNews", "onCreateView");


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_numbers);

        inisialisasiListener();

        inisialisasiTampilan();

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentNews", " onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FragmentNews", " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentNews", " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FragmentNews", " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("FragmentNews", " onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FragmentNews", " onDestroyView");
    }

    private void inisialisasiListener() {
        mOnArtikelClickListener = new RecyclerNews.OnArtikelClickListener() {
            @Override
            public void onClick(int posisi) {
                try {
                    if (toast != null) {
                        toast.cancel();
                    }

                    Log.d("KLIK Posisi : ",posisi + "");

                    switch (posisi) {
                        case 0:
                            Intent intentdetil = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil.putExtra(Constant.TAG_URL_NEWS,  Constant.link1);
                            intentdetil.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul1);

                            FragmentNews.this.startActivity(intentdetil);
                            break;

                        case 1:
                            Intent intentdetil1 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil1.putExtra(Constant.TAG_URL_NEWS,  Constant.link2);
                            intentdetil1.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul2);

                            FragmentNews.this.startActivity(intentdetil1);
                            break;

                        case 2:
                            Intent intentdetil2 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil2.putExtra(Constant.TAG_URL_NEWS,  Constant.link3);
                            intentdetil2.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul3);

                            FragmentNews.this.startActivity(intentdetil2);
                            break;

                        case 3:
                            Intent intentdetil3 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil3.putExtra(Constant.TAG_URL_NEWS,  Constant.link4);
                            intentdetil3.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul4);

                            FragmentNews.this.startActivity(intentdetil3);
                            break;

                        case 4:
                            Intent intentdetil4 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil4.putExtra(Constant.TAG_URL_NEWS,  Constant.link5);
                            intentdetil4.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul5);

                            FragmentNews.this.startActivity(intentdetil4);
                            break;

                        case 5:
                            Intent intentdetil5 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil5.putExtra(Constant.TAG_URL_NEWS,  Constant.link6);
                            intentdetil5.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul6);

                            FragmentNews.this.startActivity(intentdetil5);
                            break;

                        case 6:
                            Intent intentdetil6 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil6.putExtra(Constant.TAG_URL_NEWS,  Constant.link7);
                            intentdetil6.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul7);

                            FragmentNews.this.startActivity(intentdetil6);
                            break;

                        case 7:
                            Intent intentdetil7 = new Intent(FragmentNews.this.getActivity(), WebviewDetil.class);
                            intentdetil7.putExtra(Constant.TAG_URL_NEWS,  Constant.link8);
                            intentdetil7.putExtra(Constant.TAG_JUDUL_NEWS, Constant.judul8);

                            FragmentNews.this.startActivity(intentdetil7);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mOnButtonShareClickListener = new RecyclerNews.OnButtonShareClickListener() {
            @Override
            public void onClick(int posisi) {

                try {
                    switch (posisi) {
                        case 0:
                            juduartikel = Constant.judul1;
                            linkartikel = Constant.link1;

                            break;
                        case 1:
                            juduartikel = Constant.judul2;
                            linkartikel = Constant.link2;

                            break;

                        case 2:
                            juduartikel = Constant.judul3;
                            linkartikel = Constant.link3;

                            break;

                        case 3:
                            juduartikel = Constant.judul4;
                            linkartikel = Constant.link4;

                            break;

                        case 4:
                            juduartikel = Constant.judul5;
                            linkartikel = Constant.link5;

                            break;

                        case 5:
                            juduartikel = Constant.judul6;
                            linkartikel = Constant.link6;

                            break;

                        case 6:
                            juduartikel = Constant.judul7;
                            linkartikel = Constant.link7;

                            break;

                        case 7:
                            juduartikel = Constant.judul8;
                            linkartikel = Constant.link8;

                            break;
                    }

                    buatShareIntents();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void inisialisasiTampilan() {
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(FragmentNews.this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerNews = new RecyclerNews(FragmentNews.this.getActivity());
        mRecyclerNews.setOnArtikelClickListener(mOnArtikelClickListener);
        mRecyclerNews.setOnButtonShareClickListener(mOnButtonShareClickListener);
        mRecyclerView.setAdapter(mRecyclerNews);
    }

    //BAGIKAN ARTIKEL DENGAN INTENT
    private void buatShareIntents() {

        ShareCompat.IntentBuilder buildershare = ShareCompat.IntentBuilder.from(FragmentNews.this.getActivity());
        buildershare.setType("text/plain");
        buildershare.setText("[Article]" + " \n" + juduartikel + "  \n" + " " + linkartikel);
        buildershare.setChooserTitle("Share Article");

        Intent intentshare = buildershare.createChooserIntent();

        try {
            FragmentNews.this.startActivity(intentshare);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}