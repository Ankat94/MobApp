package esiapp.esi.ui.activity;

import android.os.Bundle;

import esiapp.esi.R;

/**
 * Created by Randhir Kumar on 2/20/2017.
 */

public class AddNewDependencyActivity extends BaseActivity {

    private final static String TAG = AddNewDependencyActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_dependency_layout);
    }
}
