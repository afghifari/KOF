package com.ika.kof.Internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created and edited by Alhudaghifari on 7/30/2017.
 * with source code from Gulajava Ministudio.
 */

public class CheckInternet {

    private Context konteks;

    ConnectivityManager conmanager = null;
    NetworkInfo netinfo = null;
    boolean isInternet = false;


    public CheckInternet(Context ctx) {

        isInternet = false;
        conmanager = null;
        netinfo = null;

        this.konteks = ctx;

        conmanager = (ConnectivityManager) konteks.getSystemService(Context.CONNECTIVITY_SERVICE);

    }


    public boolean cekStatusInternet() {

        netinfo = conmanager.getActiveNetworkInfo();

        isInternet = netinfo != null && netinfo.isConnected();

        return isInternet;
    }

}
