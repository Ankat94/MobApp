package esiapp.esi.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import esiapp.esi.R;
import esiapp.esi.ui.fragment.UpdateAdhaarNumberFragment;
import esiapp.esi.ui.fragment.VerifyAdhaarUpdateWithOtp;

/**
 * Created by Randhir Kumar on 2/14/2017.
 */

public class UpdateAdhaarActivity extends BaseActivity implements View.OnClickListener, UpdateAdhaarNumberFragment.OnFragmentInteractionListener, VerifyAdhaarUpdateWithOtp.OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private String mInsSeqNo;
    private String mPatientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_adhaar);
        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        mToolbarTitle = (TextView) mToolbar.findViewById(R.id.activity_title);
        ImageView logoImageView = (ImageView) findViewById(R.id.imageView3);
        logoImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_logo_1));
        logoImageView.setBackgroundColor(Color.TRANSPARENT);
        mToolbarTitle.setText(R.string.update_adhaar);
        if (getIntent() != null) {
            mInsSeqNo = getIntent().getStringExtra("InsSeqNo");
            mPatientName = getIntent().getStringExtra("patient_name");
        }
        UpdateAdhaarNumberFragment mUpdateAdhaarCardFragment = new UpdateAdhaarNumberFragment();

        Bundle bundle = new Bundle();
        bundle.putString("InsSeqNo", mInsSeqNo);
        bundle.putString("patient_name", mPatientName);
        mUpdateAdhaarCardFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.update_adhaar_fragment, mUpdateAdhaarCardFragment).commit();


    }

    public void setCustomTitle(String title) {
        mToolbarTitle.setText(title);
    }

    public void setCustomTitle(int title) {
        mToolbarTitle.setText(title);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
