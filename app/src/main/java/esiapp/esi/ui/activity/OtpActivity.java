package esiapp.esi.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.CreateSessionIdResp;
import esiapp.esi.model.retroModel.OtpValidationResp;
import esiapp.esi.model.retroModel.ValidateSessionIdResp;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = OtpActivity.class.getName();

    private Button enterOTP;
    private String extraString;
    private LinearLayout manageDetails;

    private TextView name;
    private TextView date;
    private TextView seconds, waitingTextView, secondsTextView;
    private TextView resendOtp;
    private EditText otpEdtxt;
    private String mOtp;
    private String mIpNumber;
    private String mMobileNumber;
    private ProgressDialog progress;
    private String mSessionId;
    private String mPatientName;
    private String mAppointmentSchedule;
    private boolean mIsFromCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle(getString(R.string.activity_main_title));
        progress = new ProgressDialog(this);
        if (getIntent().hasExtra(Constant.OTP_EXTRA)) {
            extraString = getIntent().getStringExtra(Constant.OTP_EXTRA);
            mPatientName = getIntent().getStringExtra(Constant.APPOINTMENT_NAME);
            mAppointmentSchedule = getIntent().getStringExtra(Constant.APPOINTMENT_SCHEDULE);
            mIsFromCancel = getIntent().getBooleanExtra(Constant.APPOINTMENT_IS_FROM_CANCEL, false);
        }

        manageDetails = (LinearLayout) findViewById(R.id.otp_activity_manage_details);

        name = (TextView) findViewById(R.id.otp_activity_manage_name);
        date = (TextView) findViewById(R.id.otp_activity_manage_date);
        waitingTextView = (TextView) findViewById(R.id.otp_activity_manage_waiting_txt);
        secondsTextView = (TextView) findViewById(R.id.otp_activity_manage_seconds_txt);
        seconds = (TextView) findViewById(R.id.otp_activity_manage_seconds);
        resendOtp = (TextView) findViewById(R.id.otp_activity_manage_resend_otp_txt);
        otpEdtxt = (EditText) findViewById(R.id.otp_activity_manage_otp_edtxt);

        enterOTP = (Button) findViewById(R.id.activity_otp_enterotp_button);
        enterOTP.setOnClickListener(this);

        if (extraString != null && extraString.equals(Constant.MANAGE_ACTIVITY)) {
            manageDetails.setVisibility(View.VISIBLE);
        } else {
            manageDetails.setVisibility(View.INVISIBLE);
        }
        name.setText(mPatientName);
        date.setText(mAppointmentSchedule);

//        mOtp = getIntent().getStringExtra("otp");
        mIpNumber = getIntent().getStringExtra("ipNumber");
        mMobileNumber = getIntent().getStringExtra("mobileNumber");
        mSessionId = getIntent().getStringExtra("sessionId");

        mOtp = String.valueOf(otpEdtxt.getText());
        resendOtp.setOnClickListener(this);
        timerMethod();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_otp_enterotp_button:
                if (extraString != null && extraString.equals(Constant.MANAGE_ACTIVITY)) {

                } else {
                    mOtp = String.valueOf(otpEdtxt.getText()).replace(" ", "");
                    if (TextUtils.isEmpty(mOtp)) {
                        Toast.makeText(this, getString(R.string.please_enter_otp_message), Toast.LENGTH_LONG).show();
                    } else {
                        validateOtpNumber(mIpNumber, mMobileNumber, mSessionId, mOtp);
                    }
                }
                break;

            case R.id.otp_activity_manage_resend_otp_txt:
                doCallResendOtp(mIpNumber, mSessionId, mMobileNumber);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("otp"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.d(TAG, "onReceive: " + message);
//                String otp = message.replace(getString(R.string.extract_otp_from_message_1), "");
//                otp = otp.replace(getString(R.string.extract_otp_from_message_2), "");
//                Log.d(TAG, "onReceive: " + otp);
//                mOtp = otp;
//                mOtp = mOtp.replace(" ", "");

                Pattern pattern = Pattern.compile("(\\d{4})");

                //   \d is for a digit
                //   {} is the number of digits here 4.
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    mOtp = matcher.group(1);  // 4 digit number
                }
                if (mOtp != null) {
                    mOtp = mOtp.replace(" ", "");
                    if (mOtp.length() == 4) {
                        otpEdtxt.setText(mOtp);
                    }
                }
            }
        }
    };

    /**
     * Api for validating otp
     *
     * @param ipNumber
     * @param mobileNumber
     * @param sessionId
     * @param otp
     */
    public void validateOtpNumber(String ipNumber, String mobileNumber, String sessionId, String otp) {
        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("MobileNumber", mobileNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("OTPNumber", otp);
        jsonArray.add(jsonObject);
        Log.d(TAG, "validateOtpNumber: " + jsonArray.toString());
        Call<List<OtpValidationResp>> call = networkClient.getOtpValidationRespCall(jsonArray);
        call.enqueue(new Callback<List<OtpValidationResp>>() {
            @Override
            public void onResponse(Call<List<OtpValidationResp>> call, final Response<List<OtpValidationResp>> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (response != null) {
                            Log.d(TAG, "onResponse: ");
                            List<OtpValidationResp> otpValidationRespList = response.body();
                            OtpValidationResp otpValidationResp = otpValidationRespList.get(0);
                            if (otpValidationResp != null && otpValidationResp.getResponseCode() != null) {
                                if (otpValidationResp.getResponseCode().equals(Constant.ERROR_CODE_1100)) {
                                    EsiPrefUtil.storeIpInfo(OtpActivity.this, mIpNumber, mSessionId, mMobileNumber);
                                    Log.d(TAG, "run: " + EsiPrefUtil.getIpNumber(OtpActivity.this));
                                    Log.d(TAG, "run: " + EsiPrefUtil.getKeyUserSession(OtpActivity.this));
                                    Intent intent = new Intent(OtpActivity.this, LandingActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else if (otpValidationResp.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(OtpActivity.this, R.string.session_id_check, Toast.LENGTH_LONG).show();
                                    EsiPrefUtil.clearUserInfo(mContext);
                                    NoSqlDao.getInstance().deleteAll();
                                    Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else if (otpValidationResp.getResponseCode().equals(Constant.ERROR_CODE_1101)) {
                                    Toast.makeText(OtpActivity.this, R.string.invalid_otp, Toast.LENGTH_LONG).show();
                                } else if (otpValidationResp.getResponseCode().equals(Constant.ERROR_CODE_1102)) {
                                    Toast.makeText(OtpActivity.this, R.string.failed_at_server, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(OtpActivity.this, getString(R.string.unable_to_verify_message), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(OtpActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<List<OtpValidationResp>> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(OtpActivity.this, getString(R.string.unable_to_validate_otp), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }


    /**
     * Api for resend otp
     *
     * @param ipNumber
     * @param sessionId
     * @param mobileNumber
     */
    public void doCallResendOtp(String ipNumber, String sessionId, String mobileNumber) {
        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("MobileNumber", mobileNumber);
        jsonArray.add(jsonObject);

        Call<List<BaseModel>> call = networkClient.getResendEligibilityCall(jsonArray);
        call.enqueue(new Callback<List<BaseModel>>() {
            @Override
            public void onResponse(Call<List<BaseModel>> call, final Response<List<BaseModel>> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (response != null) {
                            Log.d(TAG, "onResponse: ");

                            List<BaseModel> baseModelResponse = response.body();
                            if (baseModelResponse != null) {
                                BaseModel baseModel = baseModelResponse.get(0);
                                if (baseModel != null && baseModel.getResponseCode() != null) {

                                    if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2400)) {
                                        Toast.makeText(OtpActivity.this, getString(R.string.error_code_2400), Toast.LENGTH_LONG).show();
                                        waitingTextView.setVisibility(View.VISIBLE);
                                        seconds.setVisibility(View.VISIBLE);
                                        secondsTextView.setVisibility(View.VISIBLE);
                                        resendOtp.setEnabled(false);
                                        timerMethod();
                                    } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                        Toast.makeText(OtpActivity.this, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                    } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                        Toast.makeText(mContext, getString(R.string.session_id_check), Toast.LENGTH_LONG).show();
                                        EsiPrefUtil.clearUserInfo(mContext);
                                        NoSqlDao.getInstance().deleteAll();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2401)) {
                                        Toast.makeText(OtpActivity.this, getString(R.string.error_code_2401), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(OtpActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(OtpActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<BaseModel>> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(OtpActivity.this, getString(R.string.resend_otp_message), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }

    /**
     * Method for otp timer
     */
    private void timerMethod() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                seconds.setText("" + (l / 1000) + " ");
            }

            @Override
            public void onFinish() {
                seconds.setText(" 0 ");
                waitingTextView.setVisibility(View.GONE);
                seconds.setVisibility(View.GONE);
                secondsTextView.setVisibility(View.GONE);
                resendOtp.setEnabled(true);
                otpEdtxt.setText("");
            }
        };
        countDownTimer.start();
    }
}
