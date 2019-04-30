package esiapp.esi.ui.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.model.retroModel.UpdateAdhaarResponse;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.activity.LandingActivity;
import esiapp.esi.ui.activity.MainActivity;
import esiapp.esi.ui.activity.OtpActivity;
import esiapp.esi.ui.activity.UpdateAdhaarActivity;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static esiapp.esi.util.Constant.IPNUMBER_DETAILS;

/**
 * Created by Randhir Kumar on 2/14/2017.
 */

public class VerifyAdhaarUpdateWithOtp extends BaseFragment implements View.OnClickListener {

    private final static String TAG = VerifyAdhaarUpdateWithOtp.class.getName();

    private Button mVerifyButton;
    private EditText mAdhaarNumberEt;
    private EditText mOtpVerifyEt;
    private TextView mTimerTextView, waitingTextView, secondsTextView;
    ;
    private TextView mResendOtpTextView;
    private TextView mUpdateAadhaarTextView;
    private String mAdhaarNumber;
    private String mOtp;
    private String mMobileNumber;
    private String mInsSeqNo;
    private String mPatientName;

    private static final String BUNDLE_ADHAAR_NUMBER_KEY = "adhaar_number";
    private static final String BUNDLE_MOBILE_NUMBER_KEY = "mobile_number";
    private static final String BUNDLE_INSSEQNO_KEY = "InsSeqNo";
    private static final String BUNDLE_PATIENT_NAME_KEY = "patient_name";

    private UpdateAdhaarNumberFragment.OnFragmentInteractionListener mListener;
    private ProgressDialog progress;
    private LinearLayout mOtpLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mAdhaarNumber = bundle.getString(BUNDLE_ADHAAR_NUMBER_KEY);
            mMobileNumber = bundle.getString(BUNDLE_MOBILE_NUMBER_KEY);
            mInsSeqNo = bundle.getString(BUNDLE_INSSEQNO_KEY);
            mPatientName = bundle.getString(BUNDLE_PATIENT_NAME_KEY);
        }
        if (context instanceof UpdateAdhaarNumberFragment.OnFragmentInteractionListener) {
            mListener = (UpdateAdhaarNumberFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "  must implement ChoosePatientDetials");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Bundle getBundle(String adhaarNumber, String mobileNumber, String insSeqNo, String patientName) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ADHAAR_NUMBER_KEY, adhaarNumber);
        bundle.putString(BUNDLE_MOBILE_NUMBER_KEY, mobileNumber);
        bundle.putString(BUNDLE_INSSEQNO_KEY, insSeqNo);
        bundle.putString(BUNDLE_PATIENT_NAME_KEY, patientName);
        return bundle;
    }

    public static Fragment getFragment(String adhaarNumber, String mobileNumber, String insSeqNo, String patientName) {
        VerifyAdhaarUpdateWithOtp verifyAdhaarUpdateWithOtpFragment = new VerifyAdhaarUpdateWithOtp();
        verifyAdhaarUpdateWithOtpFragment.setArguments(getBundle(adhaarNumber, mobileNumber, insSeqNo, patientName));
        return verifyAdhaarUpdateWithOtpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.verify_update_adhaar_layout, container, false);
        mAdhaarNumberEt = (EditText) view.findViewById(R.id.entered_adhaar_number);
        mOtpVerifyEt = (EditText) view.findViewById(R.id.enter_otp);
        mTimerTextView = (TextView) view.findViewById(R.id.manage_seconds);
        mResendOtpTextView = (TextView) view.findViewById(R.id.manage_resend_otp_txt);
        secondsTextView = (TextView) view.findViewById(R.id.textView3);
        waitingTextView = (TextView) view.findViewById(R.id.otp_activity_manage_seconds_txt);
        mVerifyButton = (Button) view.findViewById(R.id.verify_button);
        mOtpLayout = (LinearLayout) view.findViewById(R.id.otp_screen_body);
        mUpdateAadhaarTextView = (TextView) view.findViewById(R.id.update_aadhaar_for);

        mVerifyButton.setOnClickListener(this);
        mResendOtpTextView.setOnClickListener(this);

        timerMethod();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdhaarNumberEt.setText(mAdhaarNumber);
        mAdhaarNumberEt.setEnabled(false);
        mUpdateAadhaarTextView.setText(" " + mPatientName + " ");
        mAdhaarNumberEt.setTypeface(Typeface.DEFAULT_BOLD);
        mAdhaarNumberEt.setTextColor(Color.BLACK);
        progress = new ProgressDialog(mContext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("otp"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_button:
                mOtp = String.valueOf(mOtpVerifyEt.getText()).replace(" ", "");
                if (TextUtils.isEmpty(mOtp)) {
                    Toast.makeText(mContext, getString(R.string.otp_cannot_be_empty_message), Toast.LENGTH_LONG).show();
                } else {
                    IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(IPNUMBER_DETAILS);
                    IPDetails ipDetails = ipDetailsModel.getIpDetails();
                    if (ipDetails != null) {
                        doCallUpdateAadhaar(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext),
                                mAdhaarNumber, mMobileNumber, mInsSeqNo, mOtp);
                    }
                }
                break;

            case R.id.manage_resend_otp_txt:
                doCallAadhaarResendOtp(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext),
                        mMobileNumber, mAdhaarNumber);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.d(TAG, "onReceive: " + message);

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
                        mOtpVerifyEt.setText(mOtp);
                    }
                }
            }
        }
    };

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Api for updating aadhaar
     *
     * @param ipNumber
     * @param sessionId
     * @param aadhaarNumber
     * @param mobileNumber
     * @param insSeqNo
     * @param otpNumber
     */
    public void doCallUpdateAadhaar(String ipNumber, String sessionId, String aadhaarNumber, String mobileNumber, String insSeqNo, String otpNumber) {
        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("InsSeqNo", insSeqNo);
        jsonObject.addProperty("AadhaarNumber", aadhaarNumber);
        jsonObject.addProperty("MobileNumber", mobileNumber);
        jsonObject.addProperty("OTPNumber", otpNumber);
        Call<BaseModel> call = networkClient.getUpdateAadhaarCall(jsonObject);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, final Response<BaseModel> response) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (response != null) {
                            BaseModel baseModel = response.body();
                            if (baseModel != null && baseModel.getResponseCode() != null) {
                                if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1504), Toast.LENGTH_LONG).show();
                                    EsiPrefUtil.clearUserInfo(mContext);
                                    NoSqlDao.getInstance().deleteAll();
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1900)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1900), Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1901)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1901), Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1902)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1902), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1903)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1903), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1904)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1904), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1950)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1950), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1951)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1951), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1952)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1952), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mContext, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<BaseModel> call, final Throwable t) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(mContext, getString(R.string.unable_to_update_aadhaar_number), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }

    /**
     * Api for resending otp for updating aadhaar
     *
     * @param ipNumber
     * @param sessionId
     * @param mobileNumber
     * @param aadhaarNumber
     */
    public void doCallAadhaarResendOtp(String ipNumber, String sessionId, String mobileNumber, String aadhaarNumber) {
        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("AadhaarNumber", aadhaarNumber);
        jsonObject.addProperty("MobileNumber", mobileNumber);

        jsonArray.add(jsonObject);

        Call<List<BaseModel>> call = networkClient.getResendAadharCall(jsonArray);
        call.enqueue(new Callback<List<BaseModel>>() {
                         @Override
                         public void onResponse(final Call<List<BaseModel>> call, final Response<List<BaseModel>> response) {
                             if (getActivity() == null)
                                 return;

                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     progress.dismiss();
                                     if (response != null) {
                                         Log.d(TAG, "onResponse: ");

                                         List<BaseModel> baseModelResponse = response.body();
                                         if (baseModelResponse != null) {
                                             BaseModel baseModel = baseModelResponse.get(0);

                                             if (baseModel != null && baseModel.getResponseCode() != null) {
                                                 if (Constant.ERROR_CODE_2500.equalsIgnoreCase(baseModel.getResponseCode())) {
                                                     waitingTextView.setVisibility(View.VISIBLE);
                                                     mTimerTextView.setVisibility(View.VISIBLE);
                                                     secondsTextView.setVisibility(View.VISIBLE);
                                                     mResendOtpTextView.setEnabled(false);
                                                     timerMethod();
                                                     Toast.makeText(mContext, getString(R.string.error_code_2500), Toast.LENGTH_LONG).show();
                                                 } else if (Constant.ERROR_CODE_1503.equalsIgnoreCase(baseModel.getResponseCode())) {
                                                     Toast.makeText(mContext, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                                 } else if (Constant.ERROR_CODE_1504.equalsIgnoreCase(baseModel.getResponseCode())) {
                                                     Toast.makeText(mContext, R.string.session_id_check, Toast.LENGTH_LONG).show();
                                                     EsiPrefUtil.clearUserInfo(mContext);
                                                     NoSqlDao.getInstance().deleteAll();
                                                     Intent intent = new Intent(mContext, MainActivity.class);
                                                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                     startActivity(intent);
                                                     getActivity().finish();
                                                     return;
                                                 } else if (Constant.ERROR_CODE_2501.equalsIgnoreCase(baseModel.getResponseCode())) {
                                                     Toast.makeText(mContext, getString(R.string.error_code_2501), Toast.LENGTH_LONG).show();
                                                 } else {
                                                     Toast.makeText(mContext, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                                                 }
                                             }
//                                             if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2500)) {
//
//                                             }
                                         }
                                     }

                                 }
                             });
                         }

                         @Override
                         public void onFailure(Call<List<BaseModel>> call, final Throwable t) {
                             if (getActivity() == null)
                                 return;
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Log.v(TAG, "onFailure");
                                     Toast.makeText(mContext, getString(R.string.unable_to_send_otp), Toast.LENGTH_LONG).show();
                                     t.printStackTrace();
                                     progress.dismiss();
                                 }
                             });
                         }
                     }
        );
    }


    /**
     * Method for otp timer
     */
    private void timerMethod() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                mTimerTextView.setText("" + (l / 1000) + " ");
            }

            @Override
            public void onFinish() {
                mTimerTextView.setText(" 0 ");
                waitingTextView.setVisibility(View.GONE);
                mTimerTextView.setVisibility(View.GONE);
                secondsTextView.setVisibility(View.GONE);
                mResendOtpTextView.setEnabled(true);
                mOtpVerifyEt.setText("");
            }
        };
        countDownTimer.start();
    }
}
