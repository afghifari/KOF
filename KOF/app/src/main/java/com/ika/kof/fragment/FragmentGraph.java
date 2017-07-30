package com.ika.kof.fragment;

/**
 * Created by Ghifari on 7/6/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ika.kof.Constant;
import com.ika.kof.Database.DataGraphic;
import com.ika.kof.MainActivity;
import com.ika.kof.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentGraph extends Fragment {

    private DataGraphic dataGraphic;
    private Button button;
    private GraphView graph;

    private LineGraphSeries<DataPoint> series;
    private PointsGraphSeries<DataPoint> series2;

    private String tabGraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        graph = (GraphView) rootView.findViewById(R.id.graph);
        button = (Button) rootView.findViewById(R.id.button_month);

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
        makeNewGraph();
        loadGraphFromMonthYearNow();
    }

    private void inisialisasiListener() {

    }

    private int getIntDayFromString(String curDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        LocalDate date = formatter.parseLocalDate(curDate);

        return date.getDayOfMonth();
    }

    private String getDateString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String currentTime = df.format(c.getTime());

        return currentTime;
    }

    private void loadGraphFromMonthYearNow() {
        Context context = getActivity();
        SharedPreferences mPrefs;
        mPrefs = context.getSharedPreferences(getString(R.string.this_app), Context.MODE_PRIVATE);

        String date = mPrefs.getString(Constant.currentDate,null);
        int integerSumPress = mPrefs.getInt(Constant.sumpress,0);
        boolean isInsertedBoolean = mPrefs.getBoolean(Constant.isInsertedToDatabase,false);

        showToast(date);

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            /*
            Kondisi
            1. belum masuk db sqlite tapi udah di simpan di sharedpreference
            2. udah masuk ke db sqlite dan udah di simpan di sharedpreference
         */
            if (!isInsertedBoolean && date != null) {
                Log.d("kondisi1:","enter");
                Date myDate = df.parse(date);
                showToast(myDate.toString());
                addDataGraph(myDate,integerSumPress);
            } else if (isInsertedBoolean && date != null) {
                Log.d("kondisi2:","enter");
                Date myDate = df.parse(date);
                loadGraph();

                String currentTime = getDateString();

                if (date != null && !date.contentEquals(currentTime) ){}
                else addDataGraph(myDate,integerSumPress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void loadGraph() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate;
        Cursor cursor = dataGraphic.getDataFromMonthYearNow();
        Log.d("kondisi2a:","enter");
        while (cursor.moveToNext()) {
            Log.d("kondisi2b:","enter");
            try {
                myDate = df.parse(cursor.getString(1));
                addDataGraphWithoutMakeNewGraph(myDate, cursor.getInt(2));
                Log.d("1dataku:",myDate+"");
                Log.d("2dataku:",cursor.getInt(1)+"");
            } catch (Exception e) {
                Log.e("error on :","loadGraph");
                e.printStackTrace();
            }
        }
    }

    private void makeNewGraph() {
        series = new LineGraphSeries<>();
        series.setColor(Color.RED);
        series2 = new PointsGraphSeries<>();
        series2.setSize(10);

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 5);
        Date d2 = calendar.getTime();

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d2.getTime());

        // enable scalling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);
        graph.addSeries(series2);

        // set date label formatter
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(),dateFormat));
    }

    public void addDataGraphWithoutMakeNewGraph(Date x, int y) {
        series.appendData(new DataPoint(x,y),true,10,false);
        series2.appendData(new DataPoint(x,y),true,10,false);

//        graph.addSeries(series);
//        graph.addSeries(series2);
    }

    public void addDataGraph(Date x, int y) {
        loadGraph();

        series.appendData(new DataPoint(x,y),true,10,false);
        series2.appendData(new DataPoint(x,y),true,10,false);

//        graph.addSeries(series);
//        graph.addSeries(series2);
        showToast("dataReceived : "+"("+x+","+y+")");
    }


    private void showToast(String message) {
        Toast.makeText(FragmentGraph.this.getActivity(),message,Toast.LENGTH_LONG).show();
    }
}