package com.ika.kof.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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


/**
 * Created by Ghifari on 7/6/2017.
 */
public class FragmentGraph extends Fragment {

    private SharedPreferences mPrefs;

    private DataGraphic dataGraphic;
    private GraphView graph;
    private ProgressDialog progress;

    private LineGraphSeries<DataPoint> series;
    private PointsGraphSeries<DataPoint> series2;

    private TextView mTextViewDateToday;
    private TextView mTextViewCountToday;
    private TextView mTextViewHighestDate;
    private TextView mTextViewHighestCount;
    private TextView mTextViewTotalCount;

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
        mTextViewTotalCount = (TextView) rootView.findViewById(R.id.total_count_number);


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

    /**
     * method ini berfungsi untuk inisiasi data awal yang diperlukan
     */
    public void inisialisasiDataAwal() {
        tabGraph = getTag();
        ((MainActivity)getActivity()).setTabGraph(tabGraph);
        dataGraphic = new DataGraphic(FragmentGraph.this.getActivity());

        Context context = getActivity();
        mPrefs = context.getSharedPreferences(getString(R.string.this_app), Context.MODE_PRIVATE);
    }

    /**
     * method ini berfungsi untuk pengaturan tampilan
     */
    private void inisialisasiTampilan() {
        makeNewGraph();
        loadGraphWithCondition();
        setFrequencyToday();
        setTotalCount();
        setHighestFrequencyByLoadData();
    }

    /**
     * method ini berfungsi untuk pengaturan listener
     */
    private void inisialisasiListener() {

    }

    /**
     * menampilkan Frequency hari ini pada layar
     */
    private void setFrequencyToday() {
        int integerSumPress = mPrefs.getInt(Constant.sumpress,0);

        mTextViewDateToday.setText(getDateStringMonthLatin());
        mTextViewCountToday.setText(integerSumPress + "");
    }

    /**
     * menampilkan total tombol yang ditekan pada layar
     */
    private void setTotalCount() {
        int totCounter = mPrefs.getInt(Constant.totalCounter,0);

        mTextViewTotalCount.setText("Total Count : " + totCounter);
    }

    /**
     * menampilan frequency tertinggi pada layar dengan input parameter
     * @param highestFrequencyDate tanggal frekuensi tertingi
     * @param highestFrequencyInt jumlah penekanan tertinggi pada hari x
     */
    private void setHighestFrequency(String highestFrequencyDate, int highestFrequencyInt) {
        mTextViewHighestDate.setText(highestFrequencyDate);
        mTextViewHighestCount.setText(Integer.toString(highestFrequencyInt));
    }

    /**
     * menampilkan frequency tertinggi pada layar dengan load data dari sharedPreference
     */
    private void setHighestFrequencyByLoadData() {
        String highestFrequencyDate = mPrefs.getString(Constant.highestFrequencyDate,null);
        int highestFrequencyInt = mPrefs.getInt(Constant.highestFrequency,0);

        if (highestFrequencyDate != null)
            mTextViewHighestDate.setText(highestFrequencyDate);
        else
            mTextViewHighestDate.setText("---");
        mTextViewHighestCount.setText(Integer.toString(highestFrequencyInt));
    }

    /**
     * untuk mendapatkan tanggal hari ini(hanya tanggal tidak dengan bulan tahun) dari parameter
     * @param curDate tanggal hari ini dalam bentuk string
     * @return tanggal dalam bentuk integer
     */
    private int getIntDayFromString(String curDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        LocalDate date = formatter.parseLocalDate(curDate);

        return date.getDayOfMonth();
    }

    /**
     * mendapatkan tanggal dengan nama bulan bentuk 3 huruf dari input parameter Date
     * @param x input tanggal
     * @return misal 3-AUG-2017
     */
    private String getTimeStringFromParameterDateMonthLatin(Date x) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String currentTime = df.format(x.getTime());

        return currentTime;
    }

    /**
     * menampilkan tanggal hari ini dalam bentuk string dengan format, misal 2-8-2017
     * @return tanggal hari ini
     */
    private String getDateString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String currentTime = df.format(c.getTime());

        return currentTime;
    }

    /**
     * menampilkan tanggal hari ini dalam bentuk string dengan format, misal 2-AUG-2017
     * @return misal 2-AUG-2016
     */
    private String getDateStringMonthLatin() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String currentTime = df.format(c.getTime());

        return currentTime;
    }

    /**
     * untuk menampilkan data ke graph dengan beberapa kondisi.
     * Kondisi :
     * 1. belum masuk db sqlite tapi udah di simpan di sharedpreference
     * 2. udah masuk ke db sqlite dan udah di simpan di sharedpreference
     */
    private void loadGraphWithCondition() {
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

    /**
     * me-load graph dati database SQLite
     */
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

    /**
     * mencari frequency tertinggi dari tanggal tiap hari.
     * method ini tidak melakukan looping.
     * data input yang dimasukkan adalah data tiap hari, sehingga method ini dapat dipanggil saat loadGraph.
     * tujuan dari method ini adalah meminimalisir terjadinya looping.
     * @param myDate tanggal input
     * @param freq frequency pada tanggal tersebut
     */
    private void searchHighestFrequency(Date myDate, int freq) {
        String curTime = getTimeStringFromParameterDateMonthLatin(myDate);

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

    /**
     * membuat graph baru (inisialisasi dari awal)
     */
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

    /**
     * menambahkan data tanpa melakukan load graph
     * @param x input tanggal
     * @param y input frequency
     */
    public void addDataGraphWithoutMakeNewGraph(Date x, int y) {
        series.appendData(new DataPoint(x,y),true,10,false);
        series2.appendData(new DataPoint(x,y),true,10,false);
        setMinMaxXBound();
    }

    /**
     * menambahkan data dengan melakukan load graph
     * method ini dipanggil saat ada pesan bluetooth masuk
     * @param x
     * @param y
     */
    public void addDataGraph(Date x, int y) {
        try {
            searchHighestFrequency(x,y);

            series.appendData(new DataPoint(x,y),true,10,false);
            series2.appendData(new DataPoint(x,y),true,10,false);
            series.resetData(new DataPoint[]{

            });
            series2.resetData(new DataPoint[]{

            });

            loadGraphWithCondition();
            setMinMaxXBound();
            setFrequencyToday();
            setTotalCount();

            showToast("data received : "+"("+getTimeStringFromParameterDateMonthLatin(x)+","+y+")");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog();
        }
    }

    /**
     * method untuk pengaturan minimal dan maksimal dari koordinat x
     */
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

    /**
     * berfungsi untuk menampilkan dialog eror.
     * method ini dibuat untuk menangani masalah apabila user mengubah tanggal menjadi lebih kurang dari tanggal hari ini
     * atau tanggal yang ada di graph.
     */
    private void showErrorDialog() {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.error_message_subtitle)
                    .setTitle(R.string.error_message_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    /**
     * method untuk menampilkan toast message agar lebih sederhana.
     * @param message pesan
     */
    private void showToast(String message) {
        Toast.makeText(FragmentGraph.this.getActivity(),message,Toast.LENGTH_LONG).show();
    }
}