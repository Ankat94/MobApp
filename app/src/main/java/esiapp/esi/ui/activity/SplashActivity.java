package esiapp.esi.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import esiapp.esi.R;
import esiapp.esi.application.BaseApplication;
import esiapp.esi.util.EsiPrefUtil;

public class SplashActivity extends BaseActivity {

    private final static String TAG = SplashActivity.class.getName();
    private static final int SPLASH_SCREEN_TIME = 6000;
    private ImageView mSplashImage;
    private TextView mScreenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashImage = (ImageView) findViewById(R.id.esi_splash_image);
        mScreenMessage = (TextView) findViewById(R.id.textView);

        Log.d(TAG, "onCreate: " + Locale.getDefault().getDisplayLanguage());
        Log.d(TAG, "onCreate: " + EsiPrefUtil.getKeyLocaleLang(mContext));
        if (!TextUtils.isEmpty(EsiPrefUtil.getKeyLocaleLang(mContext))) {
            Locale myLocale = new Locale(EsiPrefUtil.getKeyLocaleLang(mContext));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        if ("hi".equals(EsiPrefUtil.getKeyLocaleLang(mContext))) {
            mSplashImage.setImageDrawable(getResources().getDrawable(R.drawable.splash_screen_hin));
        } else {
            mSplashImage.setImageDrawable(getResources().getDrawable(R.drawable.splash_screen_eng));
        }

        mScreenMessage.setText(getString(R.string.splash_screen_esi_s_digital_india_initiative));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent startIntent;
                if (EsiPrefUtil.isUserLoggedIn(SplashActivity.this)) {
                    startIntent = new Intent(SplashActivity.this, LandingActivity.class);
                } else {
                    startIntent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(startIntent);
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_TIME);
    }

}
