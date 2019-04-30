package esiapp.esi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.model.retroModel.ReScheduleAppointMentModel;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.fragment.BookingConfirmedFragment;
import esiapp.esi.ui.fragment.ChoosePatientFragment;
import esiapp.esi.ui.fragment.ReviewBookingFragment;
import esiapp.esi.ui.fragment.SelectDateFragment;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Randhir Kumar on 2/14/2017.
 */

public class RescheduleAppointMentActivity extends BaseActivity implements SelectDateFragment.OnSelectDateAndTimeSlot,
        ReviewBookingFragment.OnMobileNumberEntered,
        BookingConfirmedFragment.OnFragmentInteractionListener, View.OnClickListener {

    private final static String TAG = RescheduleAppointMentActivity.class.getName();

    private Button bookAppointmentButton;
    private SelectDateFragment selectDateFragment;
    private BookingConfirmedFragment bookingConfirmedFragment;

    private String mIpNumber;
    private String mAdhaarNumber;
    private String mPatientName;
    private String mPatientUhid;
    private String mPatientDob;
    private String mGender;
    private String mAppointmentDate;
    private String mHospitalCode;
    private String mInsSeqNo;
    private String mMobileNumber;
    private String mTimeSlot;
    private String mappointmentId;
    private ProgressDialog progress;
    private String mReferenceNumber;
    private String mHospitalName;
    private String mHospitalAddress;
    private Toolbar mToolbar;
    private String mNewMobileNumber;

    private CurrentState mCurrentState = CurrentState.SelecteDate;

    enum CurrentState {
        SelectPatient,
        SelecteDate,
        ReviewBooking,
        ConfirmBooking
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reschedule_appointment_layout);
        bookAppointmentButton = (Button) findViewById(R.id.content_landing_book_appointment_button);
        bookAppointmentButton.setOnClickListener(this);

        selectDateFragment = new SelectDateFragment();
        bookingConfirmedFragment = new BookingConfirmedFragment();

        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progress = new ProgressDialog(this);
        IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
        IPDetails ipDetails = ipDetailsModel.getIpDetails();
        mIpNumber = ipDetails.getIPNumber();
        if (getIntent() != null) {
            mIpNumber = getIntent().getStringExtra("ipNumber");
            mappointmentId = String.valueOf(getIntent().getLongExtra("appointmentId", 0));
            mReferenceNumber = getIntent().getStringExtra("referenceNumber");
            mPatientName = getIntent().getStringExtra("patientName");
            mAdhaarNumber = getIntent().getStringExtra("aadhaarNumber");
            mNewMobileNumber = getIntent().getStringExtra("mobileNumber");
        }
        mHospitalAddress = ipDetails.getLocAddress();
        mHospitalName = ipDetails.getLocName();
        setTitle(getString(R.string.select_date_fragment_title));
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_book_appointment_fragment_continer, selectDateFragment).commit();
        bookAppointmentButton.setText(getText(R.string.book_appointment_select));
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_landing_book_appointment_button:
                String buttonTxt = bookAppointmentButton.getText().toString();

                if (mCurrentState == CurrentState.SelecteDate) {
                    if (TextUtils.isEmpty(mIpNumber) == false && TextUtils.isEmpty(mTimeSlot) == false) {
                        Fragment reviewBookingFragment = ReviewBookingFragment.getFragment(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mAppointmentDate, mTimeSlot, (mHospitalName + " " + mHospitalAddress), mInsSeqNo, mNewMobileNumber);
                        setTitle(getString(R.string.review_booking_fragment_title));
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_book_appointment_fragment_continer, reviewBookingFragment).commit();
                        bookAppointmentButton.setText(getText(R.string.book_appointment_proceed));
                        mCurrentState = CurrentState.ReviewBooking;
                    } else if (TextUtils.isEmpty(mTimeSlot) == true) {
                        Toast.makeText(this, R.string.choose_time_slot_message, Toast.LENGTH_LONG).show();
                    }
                } else if (mCurrentState == CurrentState.ReviewBooking) {
                    if (isValidMobileNumber(mMobileNumber)) {
                        doCallRescheduleAppointment(mIpNumber, EsiPrefUtil.getKeyUserSession(mContext), mappointmentId, mReferenceNumber, (mAppointmentDate + " " + mTimeSlot));
                    } else if (mMobileNumber.length() < 10 || mMobileNumber.length() > 10) {
                        Toast.makeText(mContext, getString(R.string.mobile_number_message), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, getString(R.string.enter_mobile_number_message), Toast.LENGTH_LONG).show();
                    }
                } else if (mCurrentState == CurrentState.ConfirmBooking) {
                    finish();
                }
                break;
        }
    }

    private static boolean isValidMobileNumber(CharSequence phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
        }
    }

    @Override
    public void onMobileNumberDetails(String mobileNumber) {
        mMobileNumber = mobileNumber;
        Log.d(TAG, "onMobileNumberDetails: " + mMobileNumber);
    }

    @Override
    public void onDateAndTimeSlotSelected(String appointmentDate, String timeSlot, String hospitalCode, String insSeqNo) {
        mAppointmentDate = appointmentDate;
        mTimeSlot = timeSlot;
//        mHospitalCode = hospitalCode;
//        mInsSeqNo = insSeqNo;
        Log.d(TAG, "onDateAndTimeSlotSelected: " + mAppointmentDate + mTimeSlot + mHospitalCode + mInsSeqNo);
    }

    /**
     * Api for rescheduling appointment
     *
     * @param ipNumbointmentDateer
     *      * @param sessionId
     *      * @param appointmentId
     *      * @param referenceNumber
     *      * @param newApp
     */
    private void doCallRescheduleAppointment(String ipNumber, String sessionId, String appointmentId, String referenceNumber,
                                             String newAppointmentDate) {
        progress.show();
        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("AppointmentId", appointmentId);
        jsonObject.addProperty("ReferenceNumber", referenceNumber);
        jsonObject.addProperty("NewAppointmentDate", newAppointmentDate);
        jsonArray.add(jsonObject);

        Call<String> getReScheduleAppointMentModelCall =
                networkClient.getReScheduleAppointMentModelCall(jsonArray);
        getReScheduleAppointMentModelCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (response != null) {
                            Log.d(TAG, "run: ");
                            String responseString = response.body();
                            if (responseString != null) {
                                String[] responseCode = responseString.split(",");
                                if (responseCode != null) {
                                    if (responseCode[0].equals(Constant.ERROR_CODE_1503)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1504)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.session_id_check), Toast.LENGTH_LONG).show();
                                        EsiPrefUtil.clearUserInfo(mContext);
                                        NoSqlDao.getInstance().deleteAll();
                                        Intent intent = new Intent(RescheduleAppointMentActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1600)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1600), Toast.LENGTH_LONG).show();
                                        mReferenceNumber = responseCode[1];
                                        Fragment bookingConfirmedFragment = BookingConfirmedFragment.getFragment(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mAppointmentDate, mTimeSlot, (mHospitalName + " " + mHospitalAddress), mInsSeqNo, mReferenceNumber);
                                        setTitle(getString(R.string.confirm_booking_fragment_title));
                                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_book_appointment_fragment_continer, bookingConfirmedFragment).commit();
                                        mCurrentState = CurrentState.ConfirmBooking;
                                        bookAppointmentButton.setText(getText(R.string.book_appointment_close));
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1601)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1601), Toast.LENGTH_LONG).show();
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1602)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1602), Toast.LENGTH_LONG).show();
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1603)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1603), Toast.LENGTH_LONG).show();
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1604)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1604), Toast.LENGTH_LONG).show();
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1605)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1605), Toast.LENGTH_LONG).show();
                                    } else if (responseCode[0].equals(Constant.ERROR_CODE_1607)) {
                                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.error_code_1607), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(RescheduleAppointMentActivity.this, getString(R.string.unable_to_reschedule), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
            }
        });
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
