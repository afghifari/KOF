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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ika.kof.Constant;
import com.ika.kof.Database.DataGraphic;
import com.ika.kof.MainActivity;
import com.ika.kof.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentGraph extends Fragment {

    private SharedPreferences mPrefs;

    private DataGraphic dataGraphic;
    private GraphView graph;

    private LineGraphSeries<DataPoint> series;
    private PointsGraphSeries<DataPoint> series2;

    private TextView mTextViewDateToday;
    private TextView mTextViewCountToday;
    private TextView mTextViewHighestDate;
    private TextView mTextViewHighestCount;

    private String tabGraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        Log.w("FragmentGraph", " onCreateView");

        graph = (GraphView) rootView.findViewById(R.id.graph);

        mTextViewDateToday = (TextView) rootView.findViewById(R.id.date_today_text);
        mTextViewCountToday = (TextView) rootView.findViewById(R.id.freqtoday_number);
        mTextViewHighestDate = (TextView) rootView.findViewById(R.id.date_highest_text);
        mTextViewHighestCount = (TextView) rootView.findViewById(R.id.freqhighest_number);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w("FragmentGraph", " onViewCreated");

        inisialisasiDataAwal();
        inisialisasiListener();
        inisialisasiTampilan();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w("FragmentGraph", " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("FragmentGraph", " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("FragmentGraph", " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("FragmentGraph", " onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w("FragmentGraph", " onDestroyView");
    }


    public void inisialisasiDataAwal() {
        tabGraph = getTag();
        ((MainActivity)getActivity()).setTabGraph(tabGraph);
        dataGraphic = new DataGraphic(FragmentGraph.this.getActivity());

        Context context = getActivity();
        mPrefs = context.getSharedPreferences(getString(R.string.this_app), Context.MODE_PRIVATE);
    }

    private void inisialisasiTampilan() {
        makeNewGraph();
        loadGraphFromMonthYearNow();
        setFrequencyToday();
        setHighestFrequencyByLoadData();
    }

    private void setFrequencyToday() {
        int integerSumPress = mPrefs.getInt(Constant.sumpress,0);

        mTextViewDateToday.setText(getDateString());
        mTextViewCountToday.setText(integerSumPress + "");
    }

    private void inisialisasiListener() {

    }


    private int getIntDayFromString(String curDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        LocalDate date = formatter.parseLocalDate(curDate);

        return date.getDayOfMonth();
    }

    private String getTimeStringFromParameterDate(Date x) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String currentTime = df.format(x.getTime());

        return currentTime;
    }

    private String getDateString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String currentTime = df.format(c.getTime());

        return currentTime;
    }

    private void loadGraphFromMonthYearNow() {
        String date = mPrefs.getString(Constant.currentDate,null);
        int integerSumPress = mPrefs.getInt(Constant.sumpress,0);
        boolean isInsertedBoolean = mPrefs.getBoolean(Constant.isInsertedToDatabase,false);

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

                addDataGraphWithoutMakeNewGraph(myDate,integerSumPress);
            } else if (isInsertedBoolean && date != null) {
                Log.d("kondisi2:","enter");
                Date myDate = df.parse(date);
                loadGraph();

                String currentTime = getDateString();

                if (date != null && !date.contentEquals(currentTime) ){
                    Log.d("kondisi2-","if : "+date);
                }
                else {
                    addDataGraphWithoutMakeNewGraph(myDate,integerSumPress);
                    Log.d("kondisi2-","else :" + myDate);
                }
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
                int freq = cursor.getInt(2);
                myDate = df.parse(cursor.getString(1));
                addDataGraphWithoutMakeNewGraph(myDate, freq);
                searchHighestFrequency(myDate, freq);
                Log.d("1dataku:",myDate+"");
                Log.d("2dataku:",cursor.getInt(1)+"");
            } catch (Exception e) {
                Log.e("error on :","loadGraph");
                e.printStackTrace();
            }
        }
    }

    private void searchHighestFrequency(Date myDate, int freq) {
        String curTime = getTimeStringFromParameterDate(myDate);

        int highestFrequency = mPrefs.getInt(Constant.highestFrequency,0);
        if (freq >= highestFrequency) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();

            highestFrequency = freq;
            prefsEditor.putInt(Constant.highestFrequency,highestFrequency);
            prefsEditor.putString(Constant.highestFrequencyDate,curTime);

            setHighestFrequency(curTime, highestFrequency);

            prefsEditor.commit();

        }
    }

    private void setHighestFrequency(String highestFrequencyDate, int highestFrequencyInt) {
        mTextViewHighestDate.setText(highestFrequencyDate);
        mTextViewHighestCount.setText(Integer.toString(highestFrequencyInt));
    }

    private void setHighestFrequencyByLoadData() {
        String highestFrequencyDate = mPrefs.getString(Constant.highestFrequencyDate,null);
        int highestFrequencyInt = mPrefs.getInt(Constant.highestFrequency,0);

        if (highestFrequencyDate != null)
            mTextViewHighestDate.setText(highestFrequencyDate);
        else
            mTextViewHighestDate.setText("---");
        mTextViewHighestCount.setText(Integer.toString(highestFrequencyInt));
    }

    private void makeNewGraph() {
        series = new LineGraphSeries<>();
        series.setColor(Color.RED);
        series2 = new PointsGraphSeries<>();
        series2.setSize(10);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                showToast("Sum of Pressed : " + (int) dataPoint.getY());
            }
        });

        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                showToast("Sum of Pressed : " + (int) dataPoint.getY());
            }
        });

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);

        setMinMaxXBound();

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);

        // enable scalling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);

        graph.addSeries(series);
        graph.addSeries(series2);

        graph.getGridLabelRenderer().setTextSize(16);

        // set date label formatter
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(),dateFormat));
    }

    public void addDataGraphWithoutMakeNewGraph(Date x, int y) {
        series.appendData(new DataPoint(x,y),true,10,false);
        series2.appendData(new DataPoint(x,y),true,10,false);

        Log.d("timestring : ",getTimeStringFromParameterDate(x) + "");

        setMinMaxXBound();
    }

    public void addDataGraph(Date x, int y) {
//        loadGraph();

        searchHighestFrequency(x,y);

        series.appendData(new DataPoint(x,y),true,10,false);
        series2.appendData(new DataPoint(x,y),true,10,false);
        series.resetData(new DataPoint[]{

        });
        series2.resetData(new DataPoint[]{

        });

        loadGraphFromMonthYearNow();
        setMinMaxXBound();
        setFrequencyToday();

        showToast("dataReceived : "+"("+getTimeStringFromParameterDate(x)+","+y+")");
    }

    public void setMinMaxXBound() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        Date d2 = calendar.getTime();

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d2.getTime());
    }

    private void showToast(String message) {
        Toast.makeText(FragmentGraph.this.getActivity(),message,Toast.LENGTH_LONG).show();
    }
}