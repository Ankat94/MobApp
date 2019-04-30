package esiapp.esi.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormatSymbols;

import esiapp.esi.R;
import esiapp.esi.util.EsiPrefUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewBookingFragment.OnMobileNumberEntered} interface
 * to handle interaction events.
 * Use the {@link ReviewBookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewBookingFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText mMobileNumberEt;
    private OnMobileNumberEntered mListener;
    private String mMobileNumer;

    private static final String IP_NUMBER_KEY = "ip_number";
    private static final String AADHAAR_NUMBER_KEY = "aadhaar_number";
    private static final String PATIENT_NAME_KEY = "patient_name";
    private static final String PATIENTUHID_KEY = "patient_uhid";
    private static final String PATIENT_DOB_KEY = "patient_dob";
    private static final String GENDER_KEY = "gender";
    private static final String APPOINTMENT_DATE_KEY = "appointment_date";
    private static final String HOSPITAL_CODE_KEY = "hospital_code";
    private static final String INS_SEQ_NO_KEY = "ins_seq_no";
    private static final String MOBILE_NUMBER_KEY = "mobile_number";
    private static final String TIME_SLOT_KEY = "time_slot";
    private static final String NEW_MOBILE_NUMBER = "mobile_number";

    private static ReviewBookingFragment mrReviewBookingFragment;

    private TextView mPatientName;
    private TextView mIpNumber;
    //private TextView mAadhaarNumber;
    private TextView mDateTime;
    private TextView mDispensary;
    private CheckBox mSaveMobileCheckBox;

    private String mIpNumberText;
    private String mAdhaarNumber;
    private String mPatientNameText;
    private String mPatientUhid;
    private String mPatientDob;
    private String mGender;
    private String mAppointmentDate;
    private String mHospitalCode;
    private String mInsSeqNo;
    private String mMobileNumber;
    private String mTimeSlot;
    private String mHospitalName;
    private String mNewMobileNumber;

    public ReviewBookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewBookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewBookingFragment newInstance(String param1, String param2) {
        ReviewBookingFragment fragment = new ReviewBookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle getBundle(String ipNumber, String adhaarNumber, String patientName, String patientUHID, String patientDOB,
                                   String gender, String appointmentDate, String timeSlot, String hospitalCode, String insSeqNo, String mobileNumber) {
        Bundle bundle = new Bundle();
        bundle.putString(IP_NUMBER_KEY, ipNumber);
        bundle.putString(AADHAAR_NUMBER_KEY, adhaarNumber);
        bundle.putString(PATIENT_NAME_KEY, patientName);
        bundle.putString(PATIENTUHID_KEY, patientUHID);
        bundle.putString(PATIENT_DOB_KEY, patientDOB);
        bundle.putString(GENDER_KEY, gender);
        bundle.putString(APPOINTMENT_DATE_KEY, appointmentDate);
        bundle.putString(TIME_SLOT_KEY, timeSlot);
        bundle.putString(HOSPITAL_CODE_KEY, hospitalCode);
        bundle.putString(INS_SEQ_NO_KEY, insSeqNo);
        bundle.putString(NEW_MOBILE_NUMBER, mobileNumber);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static Fragment getFragment(String ipNumber, String adhaarNumber, String patientName, String patientUHID, String patientDOB,
                                       String gender, String appointmentDate, String timeSlot, String hospitalCode, String insSeqNo, String mobileNumber) {
        mrReviewBookingFragment = new ReviewBookingFragment();
        mrReviewBookingFragment.setArguments(getBundle(ipNumber, adhaarNumber, patientName, patientUHID, patientDOB, gender, appointmentDate, timeSlot, hospitalCode, insSeqNo, mobileNumber));
        return mrReviewBookingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_booking, container, false);
        mMobileNumberEt = (EditText) view.findViewById(R.id.mobile_number_edit_text);
        mPatientName = (TextView) view.findViewById(R.id.review_booking_fragment_name);
        mIpNumber = (TextView) view.findViewById(R.id.review_booking_ip_no);
        //mAadhaarNumber = (TextView) view.findViewById(R.id.review_booking_aadhar_no);
        mDispensary = (TextView) view.findViewById(R.id.textView2);
        mDateTime = (TextView) view.findViewById(R.id.textView4);
        mSaveMobileCheckBox = (CheckBox) view.findViewById(R.id.save_mobile_check);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMobileNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (isValidMobileNumber(s)) {
//
//                }
                mMobileNumer = String.valueOf(mMobileNumberEt.getText());
                if (mListener != null) {
//                    if (mMobileNumber.contains("+91"))
//                    mMobileNumer = mMobileNumber.replace("+91", "");
                    mListener.onMobileNumberDetails(mMobileNumer);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPatientName.setText(String.valueOf(mPatientNameText));
        mIpNumber.setText(" " + mIpNumberText);
//        mAadhaarNumber.setText(" " + mAdhaarNumber);
        mDispensary.setText(mHospitalCode);
        String[] dateString = mAppointmentDate.split("-");
        String newDate = getDate(Integer.parseInt(dateString[2])) + " " + new DateFormatSymbols().getMonths()[Integer.parseInt(dateString[1]) - 1] + " " + dateString[0];
        mDateTime.setText(newDate + ", " + mTimeSlot);
        mDateTime.setTypeface(Typeface.DEFAULT_BOLD);
        if (mNewMobileNumber != null) {
            String mobileNumber = mNewMobileNumber;
            mMobileNumberEt.setText(mobileNumber);
        } else {
            String mobileNumber = EsiPrefUtil.getKeyMobileNumber(mContext);
            mMobileNumberEt.setText(mobileNumber);

        }
    }

    private String getDate(int day) {
        if (day <= 9) {
            return "0" + day;
        } else {
            return "" + day;
        }
    }

    private static boolean isValidMobileNumber(CharSequence phoneNumber) {
        if (phoneNumber.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String mobileNumber) {
        if (mListener != null) {
            mListener.onMobileNumberDetails(mobileNumber);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIpNumberText = bundle.getString(IP_NUMBER_KEY);
            mAdhaarNumber = bundle.getString(AADHAAR_NUMBER_KEY);
            mPatientNameText = bundle.getString(PATIENT_NAME_KEY);
            mPatientUhid = bundle.getString(PATIENTUHID_KEY);
            mPatientDob = bundle.getString(PATIENT_DOB_KEY);
            mGender = bundle.getString(GENDER_KEY);
            mAppointmentDate = bundle.getString(APPOINTMENT_DATE_KEY);
            mHospitalCode = bundle.getString(HOSPITAL_CODE_KEY);
            mInsSeqNo = bundle.getString(INS_SEQ_NO_KEY);
            mTimeSlot = bundle.getString(TIME_SLOT_KEY);
            mNewMobileNumber = bundle.getString(NEW_MOBILE_NUMBER);
        }
        if (context instanceof OnMobileNumberEntered) {
            mListener = (OnMobileNumberEntered) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChoosePatientDetials");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnMobileNumberEntered {
        // TODO: Update argument type and name
        void onMobileNumberDetails(String mobileNumber);
    }
}
