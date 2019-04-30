package esiapp.esi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import esiapp.esi.BuildConfig;
import esiapp.esi.model.retroModel.OtpValidationResp;

/**
 * Created by Randhir Kumar on 2/21/2017.
 */

public final class EsiPrefUtil {

    public static final int INVALID_DATA = -1;

    private static final String APP_PREFS = BuildConfig.APPLICATION_ID + ".APP_PREFS";


    ///////////////////////////////////////////////////////////////////////////
    // Preference Keys
    ///////////////////////////////////////////////////////////////////////////

    private static final String KEY_IPNUMBER = "ipnumber";
    private static final String KEY_USER_SESSION = "user.session";
    private static final String KEY_CREATED_DATE = "createdtime";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_LOCALE_LANG = "language";
    private static final String KEY_BUILD_VERSION = "build_version";

    private static SharedPreferences mSharedPreferences;


    /**
     * Initailize the shared preference for use
     *
     * @param context Context to initialize the App Preference
     */
    public static void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        }
    }


    /**
     * Store the IpNumber Information in the SharedPreference
     *
     * @param context           Context for initialize the SharedPreference
     * @param otpValidationResp Instance of the OtpValidationResp
     */
    public static void storeIpInfo(Context context, @NonNull OtpValidationResp otpValidationResp) {
        mSharedPreferences.edit()
                .putString(KEY_IPNUMBER, otpValidationResp.getIPNumber())
                .putString(KEY_USER_SESSION, otpValidationResp.getSessionId())
                .putString(KEY_CREATED_DATE, otpValidationResp.getCreatedDate())
                .apply();
    }

    public static void storeIpInfo(Context context, String ipNumber, String sessionId, String mobileNumber) {
        mSharedPreferences.edit()
                .putString(KEY_IPNUMBER, ipNumber)
                .putString(KEY_USER_SESSION, sessionId)
                .putString(KEY_MOBILE_NUMBER, mobileNumber)
                .apply();
    }

    public static void storePrefferedLanguage(Context context, String lang) {
        mSharedPreferences.edit().putString(KEY_LOCALE_LANG, lang).apply();
    }

    public static void storeBuildVersion(Context context, String buildVersion) {
        mSharedPreferences.edit().putString(KEY_BUILD_VERSION, buildVersion).apply();
    }

    /**
     * Get the IpNumber Information in the SharedPreference
     *
     * @param context Context for initialize the SharedPreference
     * @return If user exits it'll return the  ipNumber otherwise null
     */
    @Nullable
    public static String getIpNumber(Context context) {
        String ipNumber = mSharedPreferences.getString(KEY_IPNUMBER, null);
        if (!TextUtils.isEmpty(ipNumber)) {
            return ipNumber;
        }
        return null;
    }

    /**
     * Clear the IpNumber information in the SharedPreferences
     *
     * @param context Context for initialize the SharedPreference
     */
    public static void clearUserInfo(Context context) {
        mSharedPreferences.edit()
                .remove(KEY_IPNUMBER)
                .remove(KEY_USER_SESSION)
                .apply();
    }

    /**
     * Check User logged in or not
     *
     * @param context Context for initialize the SharedPreference
     * @return If user logged in true otherwise false
     */
    public static boolean isUserLoggedIn(Context context) {
        String sessionId = getKeyUserSession(context);
        String ipNumber = getIpNumber(context);

        return (!TextUtils.isEmpty(sessionId) && !TextUtils.isEmpty(ipNumber));
    }

    /**
     * It'll return the user session value
     *
     * @param context Context for initialize the SharedPreference
     * @return Session value if it's exists otherwise null
     */
    @Nullable
    public static String getKeyUserSession(Context context) {
        return getStringValue(context, KEY_USER_SESSION);
    }

    /**
     * It'll return the mobile number
     *
     * @param context Context for initialize the SharedPreference
     * @return Mobile Number if it's exists otherwise null
     */
    public static String getKeyMobileNumber(Context context) {
        return getStringValue(context, KEY_MOBILE_NUMBER);
    }

    /**
     * It'll return the locale language
     *
     * @param context Context for initialize the SharedPreference
     * @return Locale language if it's exists otherwise null
     */
    public static String getKeyLocaleLang(Context context) {
        return getStringValue(context, KEY_LOCALE_LANG);
    }

    public static String getKeyBuildVersion() {
        return getStringValue(KEY_BUILD_VERSION);
    }
    ///////////////////////////////////////////////////////////////////////////
    // Methods for storing & retrieve data in shared preferences
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Get the String value from SharedPreference
     *
     * @param context Context for initialize the SharedPreference
     * @param key     Key need to check inside the SharedPreference
     * @return Value if it's exits otherwise null
     */
    @Nullable
    private static String getStringValue(Context context, String key) {
        return mSharedPreferences.getString(key, null);
    }

    /**
     * Get the String value from SharedPreference
     *
     * @param key Key need to check inside the SharedPreference
     * @return Value if it's exits otherwise null
     */
    @Nullable
    private static String getStringValue(String key) {
        return mSharedPreferences.getString(key, null);
    }

    /**
     * Store the long value into the SharedPreferences
     *
     * @param key   Key used for storing the value
     * @param value value which need to be stored
     */
    private static void storeLongValue(@NonNull String key, long value) {
        mSharedPreferences.edit()
                .putLong(key, value)
                .apply();
    }


    /**
     * Get the long value from the SharedPreferences
     *
     * @param key Key for which value need to be fetched
     * @return if the value exits it'll be return otherwise {@link #INVALID_DATA}
     */
    private static long getLongValue(@NonNull String key) {
        return mSharedPreferences.getLong(key, INVALID_DATA);
    }

}
