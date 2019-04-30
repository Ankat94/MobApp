package esiapp.esi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.AvailableDatesModel;
import esiapp.esi.model.retroModel.BookNewAppointMentModel;
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

public class BookAppointmentActivity extends BaseActivity implements SelectDateFragment.OnSelectDateAndTimeSlot,
        ChoosePatientFragment.ChoosePatientDetials, ReviewBookingFragment.OnMobileNumberEntered,
        BookingConfirmedFragment.OnFragmentInteractionListener, View.OnClickListener {

    private final static String TAG = BookAppointmentActivity.class.getName();

    private Button bookAppointmentButton;
    private ChoosePatientFragment choosePatientFragment;
    private SelectDateFragment selectDateFragment;
    //    private ReviewBookingFragment reviewBookingFragment;
//    private BookingConfirmedFragment bookingConfirmedFragment;
    private ProgressDialog progress;
    private Toolbar mToolbar;
    private Button mAddNewDependencyButton;

    private CurrentState mCurrentState = CurrentState.SelectPatient;

    enum CurrentState {
        SelectPatient,
        SelecteDate,
        ReviewBooking,
        ConfirmBooking
    }

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
    private String mHospitalName;
    private String mHospitalAddress;
    private String mReferenceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progress = new ProgressDialog(this);

        bookAppointmentButton = (Button) findViewById(R.id.content_landing_book_appointment_button);
        bookAppointmentButton.setOnClickListener(this);

        choosePatientFragment = ChoosePatientFragment.newInstance("Abhimanyu Panda", "ABCD1234AABB3344");

        // fetching the ip details ~ from the db
        IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
        IPDetails ipDetails = ipDetailsModel.getIpDetails();
        mHospitalCode = ipDetails.getDispCode();

        getSupportFragmentManager().beginTransaction().add(R.id.activity_book_appointment_fragment_continer, choosePatientFragment).commit();
        setTitle(getString(R.string.choose_patient_fragment_title));
        selectDateFragment = new SelectDateFragment();
//        reviewBookingFragment = new ReviewBookingFragment();
//        bookingConfirmedFragment = new BookingConfirmedFragment();

        mAddNewDependencyButton = (Button) findViewById(R.id.add_new_dependency);
        mAddNewDependencyButton.setVisibility(View.GONE);
        mAddNewDependencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(BookAppointmentActivity.this, AddNewDependencyActivity.class));

//                Toast.makeText(BookAppointmentActivity.this, R.string.dependency_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDateAndTimeSlotSelected(String appointmentDate, String timeSlot, String hospitalCode, String insSeqNo) {
        mAppointmentDate = appointmentDate;
        mTimeSlot = timeSlot;
//        mHospitalCode = hospitalCode;
//        mInsSeqNo = insSeqNo;
        Log.d(TAG, "choosePatientDetailsFragmentListener: " + mAppointmentDate + mTimeSlot);
    }

    @Override
    public void choosePatientDetailsFragmentListener(String ipNumber, String adhaarNumber, String patientName, String patientUHID, String patientDOB, String gender, String insSeqNo,String hospitalCode, String hospitalAddress, String hospitalName) {
        mIpNumber = ipNumber;
        mAdhaarNumber = adhaarNumber;
        mPatientName = patientName;
        mPatientUhid = patientUHID;
        mPatientDob = patientDOB;
        mGender = gender;
        mHospitalAddress = hospitalAddress;
        mHospitalName = hospitalName;
        mInsSeqNo = insSeqNo;
        mHospitalCode = hospitalCode;
        Log.d(TAG, "choosePatientDetailsFragmentListener: " + mIpNumber + mAdhaarNumber + mPatientName + mPatientUhid + mPatientDob + mGender + mInsSeqNo + mHospitalAddress + mHospitalName);

    }

    @Override
    public void onMobileNumberDetails(String mobileNumber) {
        mMobileNumber = mobileNumber;
        Log.d(TAG, "onMobileNumberDetails: " + mMobileNumber);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_landing_book_appointment_button:
                String buttonTxt = bookAppointmentButton.getText().toString();
                Log.d(TAG, "onClick: abc" + getText(R.string.book_appointment_proceed).toString());
                Log.d(TAG, "onClick: xyz" + buttonTxt);

                if (mCurrentState == CurrentState.SelectPatient) {
                    Log.d(TAG, "onClick: " + " 1");
                    if (TextUtils.isEmpty(mIpNumber) == false &&
//                            TextUtils.isEmpty(mAdhaarNumber) == false &&
                            TextUtils.isEmpty(mPatientName) == false &&
                            TextUtils.isEmpty(mPatientUhid) == false &&
                            TextUtils.isEmpty(mPatientDob) == false &&
                            TextUtils.isEmpty(mGender) == false &&
                            TextUtils.isEmpty(mHospitalAddress) == false &&
                            TextUtils.isEmpty(mHospitalCode) == false) {
                        setTitle(getString(R.string.select_date_fragment_title));
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_book_appointment_fragment_continer, selectDateFragment).commit();
                        mAddNewDependencyButton.setVisibility(View.GONE);
                        bookAppointmentButton.setText(getText(R.string.book_appointment_select));

                        mCurrentState = CurrentState.SelecteDate;
                    } else if (TextUtils.isEmpty(mIpNumber) == true) {
                        Toast.makeText(this, getString(R.string.select_patient), Toast.LENGTH_LONG).show();
                    }
//                    else if (TextUtils.isEmpty(mAdhaarNumber) == true) {
//                        Toast.makeText(this, getString(R.string.add_aadhaar_number_message), Toast.LENGTH_LONG).show();
//                    }
                    else if (TextUtils.isEmpty(mHospitalCode) == true) {
                        Toast.makeText(this, getString(R.string.hospital_code_error), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(mHospitalName) == true && TextUtils.isEmpty(mHospitalCode) == true) {
                        Toast.makeText(this, getString(R.string.hospital_address_error), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(mPatientUhid) == true) {
                        Toast.makeText(this, getString(R.string.uhid_not_available_message), Toast.LENGTH_LONG).show();
                    }

                } else if (mCurrentState == CurrentState.SelecteDate) {
                    Log.d(TAG, "onClick: " + " 2");
                    if (TextUtils.isEmpty(mTimeSlot) == false) {
                        setTitle(getString(R.string.review_booking_fragment_title));
                        bookAppointmentButton.setText(getText(R.string.book_appointment_proceed));
                        Fragment reviewBookingFragment = ReviewBookingFragment.getFragment(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mAppointmentDate, mTimeSlot, (mHospitalName + " " + mHospitalAddress), mInsSeqNo, null);
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_book_appointment_fragment_continer, reviewBookingFragment).commit();
                        mAddNewDependencyButton.setVisibility(View.GONE);

                        mCurrentState = CurrentState.ReviewBooking;

                    } else if (TextUtils.isEmpty(mTimeSlot) == true) {
                        Toast.makeText(this, R.string.choose_time_slot_message, Toast.LENGTH_LONG).show();
                    }


                } else if (mCurrentState == CurrentState.ReviewBooking) {
                    Log.d(TAG, "onClick: " + " 3");
                    Log.d(TAG, "onClick: " + "Inside Proceed" + "mobile Number" + mMobileNumber);

                    if (isValidMobileNumber(mMobileNumber)) {
                        doCallBookNewAppointMent(mIpNumber, EsiPrefUtil.getKeyUserSession(mContext), mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, (mAppointmentDate + " " + mTimeSlot), mHospitalCode, mInsSeqNo, mMobileNumber);
                        mAddNewDependencyButton.setVisibility(View.GONE);
                    } else if (mMobileNumber.length() < 10 || mMobileNumber.length() > 10) {
                        Toast.makeText(mContext, getString(R.string.mobile_number_message), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, getString(R.string.enter_mobile_number_message), Toast.LENGTH_LONG).show();
                    }

                } else if (mCurrentState == CurrentState.ConfirmBooking) {
                    Log.d(TAG, "onClick: " + " 4");
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

    /**
     * Api for booking new appointment
     *
     * @param ipNumber
     * @param sessionId
     * @param adhaarNumber
     * @param patientName
     * @param patientUHID
     * @param patientDOB
     * @param gender
     * @param appointmentDate
     * @param hospitalCode
     * @param insSeqNo
     * @param mobileNumber
     */
    public void doCallBookNewAppointMent(String ipNumber, String sessionId, String adhaarNumber, String patientName, String patientUHID,
                                         String patientDOB, String gender, String appointmentDate, String hospitalCode,
                                         String insSeqNo, String mobileNumber) {

        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        //jsonObject.addProperty("AadharNumber", adhaarNumber);
        jsonObject.addProperty("PatientName", patientName);
        jsonObject.addProperty("PatientUHID", patientUHID);
        jsonObject.addProperty("PatientDOB", patientDOB);
        jsonObject.addProperty("Gender", gender);
        jsonObject.addProperty("AppointmentDate", appointmentDate);
        jsonObject.addProperty("HospitalCode", hospitalCode);
        jsonObject.addProperty("InsSeqNo", insSeqNo);
        jsonObject.addProperty("MobileNumber", mobileNumber);
        jsonArray.add(jsonObject);

        Call<String> bookNewAppointMentModelCall = networkClient.getBookNewAppointmentCall(jsonArray);

        bookNewAppointMentModelCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progress.dismiss();

                        Log.d(TAG, "onResponse: ");
                        String responseString = response.body();
                        if (responseString != null) {
                            String[] responseCode = responseString.split(",");
                            if (responseCode != null) {
                                if (responseCode[0].equals(Constant.ERROR_CODE_1503)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(BookAppointmentActivity.this, R.string.session_id_check, Toast.LENGTH_LONG).show();
                                    EsiPrefUtil.clearUserInfo(mContext);
                                    NoSqlDao.getInstance().deleteAll();
                                    Intent intent = new Intent(BookAppointmentActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1300)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1300), Toast.LENGTH_LONG).show();
                                    mReferenceNumber = responseCode[1];
                                    Fragment bookingConfirmedFragment = BookingConfirmedFragment.getFragment(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mAppointmentDate, mTimeSlot, (mHospitalName + " " + mHospitalAddress), mInsSeqNo, mReferenceNumber);
                                    setTitle(getString(R.string.confirm_booking_fragment_title));
                                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_book_appointment_fragment_continer, bookingConfirmedFragment).commit();
                                    bookAppointmentButton.setText(getText(R.string.book_appointment_close));
                                    mCurrentState = CurrentState.ConfirmBooking;
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1301)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1301), Toast.LENGTH_LONG).show();
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1302)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1302), Toast.LENGTH_LONG).show();
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1303)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1303), Toast.LENGTH_LONG).show();
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1304)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1304), Toast.LENGTH_LONG).show();
                                } else if (responseCode[0].equals(Constant.ERROR_CODE_1305)) {
                                    Toast.makeText(BookAppointmentActivity.this, getString(R.string.error_code_1305), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(BookAppointmentActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(BookAppointmentActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(BookAppointmentActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
