package esiapp.esi.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import esiapp.esi.R;
import esiapp.esi.ui.fragment.ChoosePatientFragment;

/**
 * Created by Randhir Kumar on 2/23/2017.
 */

public class DependantDetailsActivity extends BaseActivity {

    private final static String TAG = DependantDetailsActivity.class.getName();
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deependant_deatils);
        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.activity_title);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ChoosePatientFragment choosePatientFragment = new ChoosePatientFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isDependant", true);
        choosePatientFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_book_appointment_fragment_continer, choosePatientFragment).commit();
        mToolbarTitle.setText(R.string.dependant_details);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
