package esiapp.esi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.ui.dialog.CustomDialog;
import esiapp.esi.util.Constant;

public class ManageAppointmentActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = ManageAppointmentActivity.class.getName();
    private Button cancel;
    private Button reSchedule;
    private TextView mPatientName;
    private TextView mIpNumber;
    //private TextView mAadhaarNumber;
    private TextView mDispensaryName;
    private TextView mAppointmentTimeAndDate;
    private TextView mReferenceNumber;
    private TextView mAppointmentStatus;
    private AppointMentModel maAppointMentModel;
    private boolean mIsFrom;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointment);
        setTitle(getString(R.string.manage_appointment_activity_title));

        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPatientName = (TextView) findViewById(R.id.manage_appointment_name);
        mIpNumber = (TextView) findViewById(R.id.manage_appointment_ip_no);
        //mAadhaarNumber = (TextView) findViewById(R.id.manage_appointment_aadhar_no);
        mDispensaryName = (TextView) findViewById(R.id.manage_appointment_dispensary_name);
        mAppointmentTimeAndDate = (TextView) findViewById(R.id.manage_appointment_date_time);
        mReferenceNumber = (TextView) findViewById(R.id.manage_appointment_reference_no);
        mAppointmentStatus = (TextView) findViewById(R.id.manage_appointment_status_no);

        // fetching the ip details ~ from the db
        IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
        IPDetails ipDetails = ipDetailsModel.getIpDetails();
        if (ipDetails != null) {
            mDispensaryName.setText(ipDetails.getLocName() + "\n" + ipDetails.getLocAddress());
        } else {
            mDispensaryName.setText(" " + getString(R.string.not_available));
        }

        // getting the values from intent
        if (getIntent() != null) {
            maAppointMentModel = (AppointMentModel) getIntent().getSerializableExtra(Constant.APPOINTMENT_MODEL_ITEM);
            mIsFrom = getIntent().getBooleanExtra(Constant.ISFROMUPCOMING, false);
            mPatientName.setText(maAppointMentModel.getPatientName());
            mIpNumber.setText(" " + maAppointMentModel.getIPNumber());
            //mAadhaarNumber.setText(" " + maAppointMentModel.getAadharNumber());
            mAppointmentTimeAndDate.setText(maAppointMentModel.getAppointmentDateString());
            mAppointmentTimeAndDate.setTypeface(Typeface.DEFAULT_BOLD);
            mReferenceNumber.setText(" " + maAppointMentModel.getReferenceNumber());
            mAppointmentStatus.setText(" "+ maAppointMentModel.getStatusDescription());
//            mDispensaryName.setText(maAppointMentModel.getAppointmentHospitalLocation());
            Log.d(TAG, "onCreate: " + maAppointMentModel.getAppointmentHospitalLocation());
        }
        cancel = (Button) findViewById(R.id.manage_appointment_cancel_button);
        reSchedule = (Button) findViewById(R.id.manage_appointment_reschedule_button);

        // hiding the buttons if from history
        if (mIsFrom == false) {
            cancel.setVisibility(View.GONE);
            reSchedule.setVisibility(View.GONE);
        }
        cancel.setOnClickListener(this);
        reSchedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.manage_appointment_cancel_button:
                CustomDialog customDialog = new CustomDialog(this, maAppointMentModel.getIPNumber(), maAppointMentModel.getReferenceNumber(), maAppointMentModel.getPatientName(), maAppointMentModel.getAppointmentDateString());
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
                break;
            case R.id.manage_appointment_reschedule_button:
                Intent reScheduleAppointMent = new Intent(this, RescheduleAppointMentActivity.class);
                reScheduleAppointMent.putExtra("ipNumber", maAppointMentModel.getIPNumber());
                reScheduleAppointMent.putExtra("appointmentId", maAppointMentModel.getAppointmentId());
                reScheduleAppointMent.putExtra("referenceNumber", maAppointMentModel.getReferenceNumber());
                reScheduleAppointMent.putExtra("patientName", maAppointMentModel.getPatientName());
                reScheduleAppointMent.putExtra("aadhaarNumber", maAppointMentModel.getAadharNumber());
                reScheduleAppointMent.putExtra("mobileNumber", maAppointMentModel.getMobileNumber());
                startActivity(reScheduleAppointMent);
                finish();
                break;
        }

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
