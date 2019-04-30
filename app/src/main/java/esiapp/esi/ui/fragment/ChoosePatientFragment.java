package esiapp.esi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import esiapp.esi.R;
import esiapp.esi.adapter.PatientDependentAdapter;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.IPDependentDetail;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.ui.activity.BookAppointmentActivity;
import esiapp.esi.ui.activity.UpdateAdhaarActivity;
import esiapp.esi.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChoosePatientDetials} interface
 * to handle interaction events.
 * Use the {@link ChoosePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePatientFragment extends BaseFragment {

    private final static String TAG = ChoosePatientFragment.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    private TextView aadharName;
//    private TextView aadharNumber;
    private RecyclerView dependentsRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ChoosePatientDetials mListener;
    private String mIpNumber;
    private String mAdhaarNumber;
    private String mPatientName;
    private String mPatientUhid;
    private String mPatientDob;
    private String mGender;
    private String mHospitalName;
    private String mInsSeqNo;
    private String mHospitalAddress;
    private String mHospitalCode;
    PatientDependentAdapter mPatientDependentAdapter;
//    private LinearLayout mSelfLayout;
    private IPDetails mIpDetails;
    private List<IPDependentDetail> mIpDependentDetailList;
//    private Button mUpdateAdhaarButton;

    private boolean mIsDependantDetails;

    public ChoosePatientFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoosePatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoosePatientFragment newInstance(String param1, String param2) {
        ChoosePatientFragment fragment = new ChoosePatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_patient, container, false);

//        aadharName = (TextView) view.findViewById(R.id.choose_patient_fragemnt_aadhar_name);
//        aadharNumber = (TextView) view.findViewById(R.id.choose_patient_fragemnt_aadhar_no);
//        mSelfLayout = (LinearLayout) view.findViewById(R.id.choose_patient_fragemnt_patient_detail);
//        mUpdateAdhaarButton = (Button) view.findViewById(R.id.choose_patient_update_button);

        dependentsRecyclerView = (RecyclerView) view.findViewById(R.id.choose_patient_relative_recycler);

        // fetching the ip details ~ from the db
        final IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
        if (ipDetailsModel != null && ipDetailsModel != null) {
            mIpDetails = ipDetailsModel.getIpDetails();
            mIpDependentDetailList = mIpDetails.getIPDependentDetail();
            Log.d(TAG, "onCreateView: " + mIpDependentDetailList);
//            aadharName.setText(mIpDetails.getIPName());
//            aadharNumber.setText(" " + mIpDetails.getIP_AadharNo());
//
//            if (TextUtils.isEmpty(mIpDetails.getIP_AadharNo()) == true) {
//                mUpdateAdhaarButton.setVisibility(View.VISIBLE);
//            } else {
//                mUpdateAdhaarButton.setVisibility(View.GONE);
//            }
//            mUpdateAdhaarButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent updateAdhaarIntent = new Intent(mContext, UpdateAdhaarActivity.class);
//                    updateAdhaarIntent.putExtra("InsSeqNo", "0");
//                    updateAdhaarIntent.putExtra("patient_name", mIpDetails.getIPName());
//                    mContext.startActivity(updateAdhaarIntent);
//                    getActivity().finish();
//                }
//            });
        }


        //calling the listener to get the parameters on selecting the item from the recycler view
        mPatientDependentAdapter = new PatientDependentAdapter(mActivity, mIpDependentDetailList, mIpDetails, mIsDependantDetails, new PatientDependentAdapter.SelectedItemDetails() {

            @Override
            public void selectedPatientsDetails(String ipNumber, String adhaarNumber, String patientName, String patientUHID, String patientDOB, String gender, String insSeqNo, String hospitalCode, String hospitalAddress, String hospitalName) {
                mIpNumber = ipNumber;
                mAdhaarNumber = adhaarNumber;
                mPatientName = patientName;
                mPatientUhid = patientUHID;
                mPatientDob = patientDOB;
                mGender = gender;
                mInsSeqNo = insSeqNo;
                mHospitalAddress = hospitalAddress;
                mHospitalName = hospitalName;
                mHospitalCode = hospitalCode;
                Log.d(TAG, "selectedPatientsDetails: " + mIpNumber + mAdhaarNumber + mPatientName + mPatientUhid + mPatientDob + mGender);
                if (mIsDependantDetails == false) {
                    if (mListener != null && !mIsDependantDetails) {
                        mListener.choosePatientDetailsFragmentListener(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mInsSeqNo, mHospitalCode, mHospitalAddress, mHospitalName);
                    }
                    //mSelfLayout.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_color));
                }
            }
        });

        dependentsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        dependentsRecyclerView.setAdapter(mPatientDependentAdapter);

//        mSelfLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (TextUtils.isEmpty(mIpDetails.getIP_AadharNo()) == true) {
//                    Toast.makeText(getActivity(), getString(R.string.update_aadhaar_number_message), Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if (mPatientDependentAdapter != null) {
//                    mPatientDependentAdapter.clearSelectedItem();
//                }
//                mIpNumber = mIpDetails.getIPNumber();
//                mAdhaarNumber = mIpDetails.getIP_AadharNo();
//                mPatientName = mIpDetails.getIPName();
//                mPatientUhid = mIpDetails.getEmpeuhid();
//                mPatientDob = mIpDetails.getIPDateOfBirth();
//                mGender = mIpDetails.getIP_Gender();
//
//                // TODO: 2/23/2017
//                mInsSeqNo = "0";
//                mHospitalAddress = mIpDetails.getLocAddress();
//                mHospitalName = mIpDetails.getLocName();
//                if (mIsDependantDetails == false) {
//                    mSelfLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
//                    if (mListener != null) {
//                        mListener.choosePatientDetailsFragmentListener(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mInsSeqNo, mHospitalAddress, mHospitalName);
//                    }
//                }
//            }
//        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.choosePatientDetailsFragmentListener(mIpNumber, mAdhaarNumber, mPatientName, mPatientUhid, mPatientDob, mGender, mInsSeqNo, mHospitalCode, mHospitalAddress, mHospitalName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsDependantDetails = bundle.getBoolean("isDependant");
        }
        if (context instanceof BookAppointmentActivity) {
            mListener = (ChoosePatientDetials) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement ChoosePatientDetials");
//        }
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
    public interface ChoosePatientDetials {
        // TODO: Update argument type and name
        void choosePatientDetailsFragmentListener(String ipNumber, String adhaarNumber, String patientName, String patientUHID, String patientDOB, String gender, String insSeqNo, String hospitalCode, String hospitalAddress, String hospitalName);
    }
}
