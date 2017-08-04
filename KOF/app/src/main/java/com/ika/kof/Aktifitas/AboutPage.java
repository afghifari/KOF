package com.ika.kof.Aktifitas;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ika.kof.R;

/**
 * Created by Alhudaghifari on 8/3/2017.
 */
public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.halfblue)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AboutPage :","onSTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AboutPage :","onRESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AboutPage :","onPAUSE");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AboutPage :","onSTOP");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("AboutPage :","onDESTROY");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AboutPage.this.getMenuInflater().inflate(R.menu.menu_about_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_donee:

                AboutPage.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
