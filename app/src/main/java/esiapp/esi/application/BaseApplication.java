package esiapp.esi.application;

import android.app.Application;
import android.content.res.Configuration;

import esiapp.esi.BuildConfig;
import esiapp.esi.db.DBManager;
import esiapp.esi.util.EsiPrefUtil;
import io.fabric.sdk.android.Fabric;


import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.Locale;

/**
 * Created by soorianarayanan on 7/2/17.
 */

public class BaseApplication extends Application {
    public static Application mApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Fabric fabric = new Fabric.Builder(this).debuggable(true).kits(new Crashlytics()).build();
        Fabric.with(fabric);

        DBManager.initDataBase(this);

        EsiPrefUtil.init(this);

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public BaseApplication() {
        super();
    }
}
