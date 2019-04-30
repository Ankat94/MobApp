package esiapp.esi.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import esiapp.esi.R;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.CreateSessionIdResp;
import esiapp.esi.model.retroModel.GetAllAppointmentResp;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.model.retroModel.IpNumberEligibilityCheckResp;
import esiapp.esi.model.retroModel.ValidateSessionIdResp;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import esiapp.esi.util.PermissionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = LoginActivity.class.getName();

    private Button signin;
    private EditText ipNum;
    private EditText mobileNum;
    private Toolbar toolbar;
    private TextView title;

    private ProgressDialog progress;

    private String ipNumber;
    private String mobileNumber;

    private boolean mIsPrefixAdded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.activity_main_title));
        progress = new ProgressDialog(this);
        signin = (Button) findViewById(R.id.activity_login_button);
        ipNum = (EditText) findViewById(R.id.activity_login_ip_no);
        mobileNum = (EditText) findViewById(R.id.activity_login_mobile_no);

//        if (Constant.DEBUG) {
//            ipNum.setText("2013532364");
//            mobileNum.setText("+918587806755");
//        }

//        mobileNum.addTextChangedListener(m_MobileWatcher);
        signin.setOnClickListener(this);

        PermissionUtil.hasReadSMSPermission(this, 100);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_title:
                break;
            case R.id.activity_login_button:
                ipNumber = ipNum.getText().toString();
                if (mobileNum != null && mobileNum.length() > 0) {
                    mobileNumber = mobileNum.getText().toString();
                }
                if (TextUtils.isEmpty(ipNumber) || TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(getBaseContext(), getString(R.string.ip_and_aadhaar_number_message), Toast.LENGTH_LONG).show();
                } else if (ipNum.getText().length() < 10 || ipNum.getText().length() > 10) {
                    Toast.makeText(this, getString(R.string.ip_number_message), Toast.LENGTH_LONG).show();
                } else if (mobileNum.getText().length() < 10 || mobileNum.getText().length() > 10) {
                    Toast.makeText(this, getString(R.string.mobile_number_message1), Toast.LENGTH_LONG).show();
                } else {
                    String selectedLanguage;
                    if ("hi".equals(EsiPrefUtil.getKeyLocaleLang(mContext))) {
                        selectedLanguage = "1";
                    } else {
                        selectedLanguage = "0";
                    }
                    validateUserDetails(ipNumber, mobileNumber, selectedLanguage);
                }

                break;
            case R.id.activity_login_ip_no:
                break;
            case R.id.activity_login_mobile_no:
                break;
        }
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
                mobileNum.setText("+91" + s.toString());
                Selection.setSelection(mobileNum.getText(), mobileNum.getText().length());
                mIsPrefixAdded = true;
            }
        }
    };

    public void executeApiCall(String IpNumber, String MobileNumber) {

        progress.show();
        NetworkClient networkClient = new NetworkClient();
        Call<IPNumberDetails> call = networkClient.getIpNumberDetailsCall(IpNumber);
        call.enqueue(new Callback<IPNumberDetails>() {
            @Override
            public void onResponse(Call<IPNumberDetails> call, final Response<IPNumberDetails> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Log.v(TAG, "onResponse");
                        NoSqlDao.getInstance().insertValues(Constant.IPNUMBER_DETAILS, (IPNumberDetails) response.body());
                        startActivity(new Intent(LoginActivity.this, OtpActivity.class));
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(Call<IPNumberDetails> call, Throwable t) {
                Log.v(TAG, "onFailure");
                Toast.makeText(getBaseContext(), getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                progress.dismiss();
            }
        });
    }

    public void validateUserDetails(final String ipNumber, final String mobileNumber, final String lang) {

        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("MobileNumber", mobileNumber);
        jsonObject.addProperty("Language", lang);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);

        Call<IpNumberEligibilityCheckResp> call = networkClient.getIpNumberEligibilityCheckRespCall(jsonArray);

        call.enqueue(new Callback<IpNumberEligibilityCheckResp>() {
            @Override
            public void onResponse(Call<IpNumberEligibilityCheckResp> call, final Response<IpNumberEligibilityCheckResp> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();

                        if (response != null) {
                            Log.d(TAG, "onResponse: ");
                            IpNumberEligibilityCheckResp ipNumberEligibilityCheckResp = response.body();
                            if (ipNumberEligibilityCheckResp != null && ipNumberEligibilityCheckResp.getResponseCode() != null) {
                                if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1000)) {
                                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                                    intent.putExtra("ipNumber", ipNumber);
                                    intent.putExtra("mobileNumber", mobileNumber);
                                    intent.putExtra("sessionId", ipNumberEligibilityCheckResp.getSessionid());
                                    if (ipNumberEligibilityCheckResp.getSessionid() != null) {
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.session_id_error_message), Toast.LENGTH_LONG).show();
                                    }
                                } else if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1001)) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.error_code_1001), Toast.LENGTH_LONG).show();
                                } else if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1002)) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.error_code_1002), Toast.LENGTH_LONG).show();
                                } else if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1003)) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.error_code_1003), Toast.LENGTH_LONG).show();
                                } else if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1004)) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.error_code_1004), Toast.LENGTH_LONG).show();
                                } else if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                } else if (ipNumberEligibilityCheckResp.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.error_code_1504), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<IpNumberEligibilityCheckResp> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(getBaseContext(), getString(R.string.unable_to_connnect_message), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
