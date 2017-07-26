package com.ika.kof.fragment;

/**
 * Created by Ghifari on 7/6/2017.
 */


import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ika.kof.Database.DataGraphic;
import com.ika.kof.MainActivity;
import com.ika.kof.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class FragmentGraph extends Fragment {

    private DataGraphic dataGraphic;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series;

    private String tabGraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = (GraphView) rootView.findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
        });


        series.setColor(Color.RED);
        graph.addSeries(series);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inisialisasiDataAwal();
        inisialisasiListener();
        inisialisasiTampilan();
    }

    public void inisialisasiDataAwal() {
        tabGraph = getTag();
        ((MainActivity)getActivity()).setTabGraph(tabGraph);
        dataGraphic = new DataGraphic(FragmentGraph.this.getActivity());
    }

    private void inisialisasiTampilan() {
    }

    private void inisialisasiListener() {
    }

    public void addDataGraph(int x, int y) {
        series.appendData(new DataPoint(x,y),true,12);
        graph.addSeries(series);
        showToast("dataReceived : "+"("+x+","+y+")");

        dataGraphic.checkToday();
    }


    private void showToast(String message) {
        Toast.makeText(FragmentGraph.this.getActivity(),message,Toast.LENGTH_LONG).show();
    }
}