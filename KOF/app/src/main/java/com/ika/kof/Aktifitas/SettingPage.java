package com.ika.kof.Aktifitas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ika.kof.Constant;
import com.ika.kof.R;
import com.ika.kof.fragment.FragmentBluetooth;

/**
 * Created by Alhudaghifari on 8/5/2017.
 */

public class SettingPage extends AppCompatActivity {

    private Button mButtonResetFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ika.kof.R.layout.setting_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.ika.kof.R.color.halfblue)));

        inisialisasiDataAwal();
        inisialisasiListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SettingPage :","onSTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SettingPage :","onRESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SettingPage :","onPAUSE");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SettingPage :","onSTOP");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SettingPage :","onDESTROY");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SettingPage.this.getMenuInflater().inflate(R.menu.menu_about_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_donee:

                SettingPage.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inisialisasiDataAwal() {
        mButtonResetFilter = (Button) findViewById(R.id.reset_button);
    }

    private void inisialisasiListener() {
        mButtonResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.reset_filter_message1)
                .setTitle(R.string.reset_filter_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Context context = SettingPage.this;
                        SharedPreferences mPrefs;
                        mPrefs = context.getSharedPreferences(getString(R.string.this_app), Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();

                        prefsEditor.putInt(Constant.totalCounter,0);
                        prefsEditor.commit();

                        showToast(getResources().getString(R.string.filter_resetted));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * method ini untuk menampilkan toast agar pemanggilan lebih sederhana
     * @param s pesan
     */
    private void showToast(String s) {
        Toast.makeText(SettingPage.this,s,Toast.LENGTH_LONG).show();
    }
}
