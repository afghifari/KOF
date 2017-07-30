package com.ika.kof.Aktifitas;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ika.kof.Constant;
import com.ika.kof.Internet.CheckInternet;
import com.ika.kof.R;

/**
 * Created by Alhudaghifari on 7/30/2017.
 */
public class WebviewDetil extends AppCompatActivity {

    private Toolbar tolbar;
    private ActionBar aksibar;

    private CheckInternet checkInternet;

    private LinearLayout layoutprogress;
    private ProgressBar progresbarweb;
    private ProgressDialog progdialog;

    private String linkartikel = "";
    private String strjudulberita = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_detil);

        Bundle bundeldata = WebviewDetil.this.getIntent().getExtras();
        linkartikel = bundeldata.getString(Constant.TAG_URL_NEWS);
        strjudulberita = bundeldata.getString(Constant.TAG_JUDUL_NEWS);

        layoutprogress = (LinearLayout) findViewById(R.id.layoutprogress);
        progresbarweb = (ProgressBar) findViewById(R.id.progressBarweb);
        checkInternet = new CheckInternet(WebviewDetil.this);

        tolbar = (Toolbar) findViewById(R.id.toolbar);
        if (tolbar != null) {
            WebviewDetil.this.setSupportActionBar(tolbar);
        }

        aksibar = WebviewDetil.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);
        aksibar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        aksibar.setTitle(R.string.detil_artikel);

        cekKoneksiInternet();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("WEBVIEW :","onSTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("WEBVIEW :","onRESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("WEBVIEW :","onPAUSE");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("WEBVIEW :","onSTOP");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("WEBVIEW :","onDESTROY");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        WebviewDetil.this.getMenuInflater().inflate(R.menu.menu_detil_artikel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                WebviewDetil.this.finish();
                return true;

            case R.id.action_bagikan:

                buatShareIntents();
                return true;

            case R.id.action_segarkan:

                cekKoneksiInternet();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //CEK KONEKSI INTERNET
    private void cekKoneksiInternet() {

        munculProgressBar();

        boolean isInternetAda = checkInternet.cekStatusInternet();
        if (isInternetAda) {

            inisialiasiTampilan();

        } else {

            //peringatan internet tidak tersedia
            progdialog.dismiss();
        }

    }

    public void inisialiasiTampilan() {
        WebView myWebView = (WebView) findViewById(R.id.webview2);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int proses)
            {
                try {
                    if (layoutprogress != null && progresbarweb != null) {
                        layoutprogress.setVisibility(View.VISIBLE);
                        progresbarweb.setProgress(proses);

                        if (proses == 100) {
                            layoutprogress.setVisibility(View.GONE);
                            progresbarweb.setProgress(1);
                        } else if (proses > 40 && proses < 100) {
                            progdialog.dismiss();
                        } else {
                            layoutprogress.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(linkartikel);
    }

    //BAGIKAN ARTIKEL DENGAN INTENT
    private void buatShareIntents() {

        ShareCompat.IntentBuilder buildershare = ShareCompat.IntentBuilder.from(WebviewDetil.this);
        buildershare.setType("text/plain");
        buildershare.setText("" + strjudulberita + "  \n" + " " + linkartikel);
        buildershare.setChooserTitle("Bagikan artikel");

        Intent intentshare = buildershare.createChooserIntent();

        try {
            WebviewDetil.this.startActivity(intentshare);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //MUNCUL PROGRES BAR
    private void munculProgressBar() {
        progdialog = new ProgressDialog(WebviewDetil.this);
        progdialog.setMessage("Memuat data...");
        progdialog.setCancelable(false);
        progdialog.show();
    }

}
