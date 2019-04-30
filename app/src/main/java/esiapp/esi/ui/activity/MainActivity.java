package esiapp.esi.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import esiapp.esi.R;
import esiapp.esi.util.EsiPrefUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private Button ipNum;
    private Button ipNumU;
    private Button esicEmp;
    private Button esicPen;
    private Button hindi;
    private Button english;
    private SwitchCompat mUrlSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.activity_main_title));
        ipNum = (Button) findViewById(R.id.activity_main_ip_no);
        ipNumU = (Button) findViewById(R.id.activity_main_ip_no_u);
        esicEmp = (Button) findViewById(R.id.activity_main_esic_emp);
        esicPen = (Button) findViewById(R.id.activity_main_ip_esic_pen);
        hindi = (Button) findViewById(R.id.activity_main_hindi);
        english = (Button) findViewById(R.id.activity_main_english);
        mUrlSwitch = (SwitchCompat) findViewById(R.id.url_toogle);
        ipNum.setOnClickListener(this);
        hindi.setOnClickListener(this);
        english.setOnClickListener(this);

        EsiPrefUtil.storeBuildVersion(mContext, "stage");
        mUrlSwitch.setVisibility(View.INVISIBLE);
        mUrlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EsiPrefUtil.storeBuildVersion(mContext, "production");
                    Toast.makeText(MainActivity.this, "Production", Toast.LENGTH_LONG).show();
                } else {
                    EsiPrefUtil.storeBuildVersion(mContext, "stage");
                    Toast.makeText(MainActivity.this, "Stage", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_ip_no:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.activity_main_ip_no_u:
                break;
            case R.id.activity_main_esic_emp:
                break;
            case R.id.activity_main_ip_esic_pen:
                break;
            case R.id.activity_main_hindi:
                setLocale("hi");
                EsiPrefUtil.storePrefferedLanguage(mContext, "hi");
                break;
            case R.id.activity_main_english:
                setLocale("en");
                EsiPrefUtil.storePrefferedLanguage(mContext, "en");
                break;
        }
    }
}
