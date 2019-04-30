package esiapp.esi.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.activity.MainActivity;
import esiapp.esi.ui.activity.RescheduleAppointMentActivity;
import esiapp.esi.ui.activity.UpdateAdhaarActivity;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Randhir Kumar on 2/14/2017.
 */

public class UpdateAdhaarNumberFragment extends BaseFragment implements View.OnClickListener {

    private final static String TAG = UpdateAdhaarNumberFragment.class.getName();
    private Button mNextButton;
    private EditText mAdhaarNumberEt;
    private EditText mMobileNumberEt;
    private String mAdhaarNumber;
    private String mMobileNumber;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progress;
    private String mInsSeqNo;
    private String mPatientName;
    private boolean mIsPrefixAdded = false;
    private TextView mUpdateAadhaarTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mInsSeqNo = bundle.getString("InsSeqNo");
            mPatientName = bundle.getString("patient_name");
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "  must implement ChoosePatientDetials");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog(mContext);

        IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
        IPDetails ipDetails = ipDetailsModel.getIpDetails();
        if (ipDetails != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.update_adhar_layout, container, false);
        mAdhaarNumberEt = (EditText) view.findViewById(R.id.adhaar_no);
        mMobileNumberEt = (EditText) view.findViewById(R.id.mobile_no);
        mNextButton = (Button) view.findViewById(R.id.next_button);
        mUpdateAadhaarTextView = (TextView) view.findViewById(R.id.update_aadhaar_for);
        mNextButton.setOnClickListener(this);
//        if (Constant.DEBUG) {
//            mAdhaarNumberEt.setText("100001234011");
//            mMobileNumberEt.setText("7892378851");
//        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mMobileNumberEt.addTextChangedListener(m_MobileWatcher);
        mUpdateAadhaarTextView.setText(" " + mPatientName + " ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                mAdhaarNumber = String.valueOf(mAdhaarNumberEt.getText());
                mMobileNumber = String.valueOf(mMobileNumberEt.getText());

                if (TextUtils.isEmpty(mAdhaarNumber) || TextUtils.isEmpty(mMobileNumber)) {
                    Toast.makeText(mContext, getString(R.string.aadhar_and_mobile_number_message), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mAdhaarNumber)) {
                    Toast.makeText(mContext, getString(R.string.aadhaar_number_message), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mMobileNumber)) {
                    Toast.makeText(mContext, getString(R.string.mobile_number_empty_message), Toast.LENGTH_LONG).show();
                } else if (mMobileNumber.length() < 10 || mMobileNumber.length() > 10) {
                    Toast.makeText(mContext, getString(R.string.mobile_number_message), Toast.LENGTH_LONG).show();
                } else if (mAdhaarNumber.length() < 12 || mAdhaarNumber.length() > 12) {
                    Toast.makeText(mContext, getString(R.string.aadhaar_number_message_s), Toast.LENGTH_LONG).show();
                } else {
                    doCallAadhaarOTP(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext), mAdhaarNumber, mMobileNumber);
                }
                break;
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Api for getting otp for updating aadhaar number
     *
     * @param ipNumber
     * @param sessionId
     * @param aadhaarNumber
     * @param mobileNumber
     */
    public void doCallAadhaarOTP(String ipNumber, String sessionId, String aadhaarNumber, String mobileNumber) {
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
        Call<List<BaseModel>> call = networkClient.getAadhaarOTPCall(jsonArray);
        call.enqueue(new Callback<List<BaseModel>>() {
            @Override
            public void onResponse(Call<List<BaseModel>> call, final Response<List<BaseModel>> response) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        List<BaseModel> baseModelResponse = response.body();
                        if (baseModelResponse != null && baseModelResponse.size() > 0) {
                            BaseModel baseModel = baseModelResponse.get(0);
                            if (baseModel != null && baseModel.getResponseCode() != null) {
                                if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(mContext, getString(R.string.session_id_check), Toast.LENGTH_LONG).show();
                                    EsiPrefUtil.clearUserInfo(mContext);
                                    NoSqlDao.getInstance().deleteAll();
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2300)) {
                                    if (getActivity() instanceof UpdateAdhaarActivity) {
                                        ((UpdateAdhaarActivity) getActivity()).setCustomTitle(R.string.verify_adhaar);
                                        Log.d("MM", "onClick: this is instance of UpdateAdhaarActivity");
                                    } else {
                                        Log.d("MM", "onClick: this is not an instance of UpdateAdhaarActivity");
                                    }
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    VerifyAdhaarUpdateWithOtp verifyAdhaarUpdateWithOtp = (VerifyAdhaarUpdateWithOtp) VerifyAdhaarUpdateWithOtp.getFragment(mAdhaarNumber, mMobileNumber, mInsSeqNo, mPatientName);
                                    fragmentTransaction.replace(R.id.update_adhaar_fragment, verifyAdhaarUpdateWithOtp, "fragment_screen");
                                    fragmentTransaction.commit();

                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2301)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_2301), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2302)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_2302), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2303)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_2303), Toast.LENGTH_LONG).show();
                                } else if (baseModel.getResponseCode().equals(Constant.ERROR_CODE_2304)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_2304), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mContext, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext, getString(R.string.unable_to_update_aadhaar_number), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }

    TextWatcher m_MobileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (TextUtils.isEmpty(s.toString())) {
                mIsPrefixAdded = false;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().contains("+91") && !mIsPrefixAdded) {
                mMobileNumberEt.setText("+91" + s.toString());
                Selection.setSelection(mMobileNumberEt.getText(), mMobileNumberEt.getText().length());
                mIsPrefixAdded = true;
            }
        }
    };
}
