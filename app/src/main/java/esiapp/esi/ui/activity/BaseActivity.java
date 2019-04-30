package esiapp.esi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import esiapp.esi.R;

/**
 * Created by soorianarayanan on 7/2/17.
 */

public class BaseActivity extends AppCompatActivity {

    public static Activity mActivity;
    public static Context mContext;

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        mActivity = this;
    }

    protected void setTitle(String title){
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setTitle("");

        TextView toolBarTitle = (TextView) findViewById(R.id.activity_title);

        if (toolBarTitle != null) {
            toolBarTitle.setText(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
