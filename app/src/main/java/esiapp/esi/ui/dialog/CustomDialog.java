package esiapp.esi.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import esiapp.esi.R;
import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.ReScheduleAppointMentModel;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.activity.OtpActivity;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by soorianarayanan on 9/2/17.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    private final static String TAG = CustomDialog.class.getName();

    public Activity mActivity;
    public Button yes;
    public Button no;
    private ProgressDialog progress;
    private String mIpNumber;
    private String mReferenceNumber;
    private String mAppointmentTime;
    private String mAppointmentName;

    public CustomDialog(Activity activity, String ipNumber, String referenceNummber, String appointmentName, String appointmentTime) {
        super(activity);
        mActivity = activity;
        mIpNumber = ipNumber;
        mReferenceNumber = referenceNummber;
        mAppointmentName = appointmentName;
        mAppointmentTime = appointmentTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        yes = (Button) findViewById(R.id.custom_dialog_yes);
        no = (Button) findViewById(R.id.custom_dialog_no);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        progress = new ProgressDialog(mActivity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_dialog_yes:
                dismiss();
                doCallCancelAppointment(EsiPrefUtil.getIpNumber(mActivity), EsiPrefUtil.getKeyUserSession(mActivity), mReferenceNumber);
//                Intent intent = new Intent(mActivity, OtpActivity.class);
//                intent.putExtra(Constant.APPOINTMENT_NAME, mAppointmentName);
//                intent.putExtra(Constant.APPOINTMENT_SCHEDULE, mAppointmentTime);
//                intent.putExtra(Constant.APPOINTMENT_IS_FROM_CANCEL, true);
//                intent.putExtra(Constant.OTP_EXTRA, Constant.MANAGE_ACTIVITY);
//                mActivity.startActivity(intent);
//                mActivity.finish();
                break;

            case R.id.custom_dialog_no:
                dismiss();
                break;

        }
    }


    /**
     * Api for cancelling the appointment
     *
     * @param ipNumber
     * @param sessionId
     * @param referenceNumber
     */
    private void doCallCancelAppointment(String ipNumber, String sessionId, String referenceNumber) {
        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("ReferenceNumber", referenceNumber);
        jsonArray.add(jsonObject);

        Call<String> cancelAppointmentCall = networkClient.getAppointmentCancelCall(jsonArray);
        cancelAppointmentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (response != null) {
                            Log.d(TAG, "onResponse: ");
                            String responseCode = response.body();
                            Log.d(TAG, "run: " + responseCode);
                            if (TextUtils.isEmpty(responseCode)) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.error_code_1703), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (responseCode.equals(Constant.ERROR_CODE_1700) || responseCode.equals(Constant.ERROR_CODE_1701)) {
                                if (responseCode.equals(Constant.ERROR_CODE_1700)) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.error_code_1700), Toast.LENGTH_LONG).show();
//                                    mActivity.finish();

                                } else if (responseCode.equals(Constant.ERROR_CODE_1701)) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.error_code_1701), Toast.LENGTH_LONG).show();
                                }
                                mActivity.finish();
                            } else if (responseCode.equals(Constant.ERROR_CODE_1702) || responseCode.equals(Constant.ERROR_CODE_1703)) {
                                if (responseCode.equals(Constant.ERROR_CODE_1702)) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.error_code_1702), Toast.LENGTH_LONG).show();
                                } else if (responseCode.equals(Constant.ERROR_CODE_1703)) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.error_code_1703), Toast.LENGTH_LONG).show();
                                }
                            } else if (responseCode.equals(Constant.ERROR_CODE_1704)) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.error_code_1704), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, final Throwable t) {
                if (mActivity == null)
                    return;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(mActivity, mActivity.getString(R.string.unable_to_cancel), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
