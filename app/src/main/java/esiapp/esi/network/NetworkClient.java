package esiapp.esi.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import esiapp.esi.R;
import esiapp.esi.application.BaseApplication;
import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.HospitalTimeSlotModel;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.model.retroModel.IpNumberEligibilityCheckResp;
import esiapp.esi.model.retroModel.OtpValidationResp;
import esiapp.esi.model.retroModel.ScheduledDatesModel;
import esiapp.esi.util.EsiPrefUtil;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

/**
 * Created by soorianarayanan on 13/2/17.
 */

public class NetworkClient {

    private static final String BASE_URL = BaseApplication.mApplication.getString(R.string.base_url);
    private static final String PRODUCTION_URL = "http://ehealth.esic.in";
    private ApiService apiService;
    private ApiServiceProduction mApiServiceProduction;
    private boolean mIsStageBuild = false;

    public NetworkClient() {


        String buildVersion = EsiPrefUtil.getKeyBuildVersion();
        if (buildVersion != null) {
            if (buildVersion.equals("stage")) {
                mIsStageBuild = true;
            } else {
                mIsStageBuild = false;
            }
        }
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

//        Retrofit retrofit = new Retrofit.Builder()
//Temprory change base url
//                .baseUrl(BASE_URL)
//                .baseUrl(NetworkChanges.BaseURL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        apiService = retrofit.create(ApiService.class);
//        String buildVersion = EsiPrefUtil.getKeyBuildVersion();
//        if (buildVersion != null) {
//            if (buildVersion.equals("stage")) {
        Retrofit retrofit = new Retrofit.Builder()
//Temprory change base url
//                .baseUrl(BASE_URL)
                .baseUrl(NetworkChanges.BaseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
//            } else {
        Retrofit retrofit2 = new Retrofit.Builder()
//Temprory change base url
//                .baseUrl(BASE_URL)
                .baseUrl(PRODUCTION_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiServiceProduction = retrofit2.create(ApiServiceProduction.class);
//            }
//        }
    }


    public ApiService getApiService() {
        return apiService;
    }

    public Call<IPNumberDetails> getIpNumberDetailsCall(String IPNumber) {
        if (EsiPrefUtil.getKeyBuildVersion().equals("stage")) {
            return apiService.getIpNumberDetailsCall(IPNumber);
        } else {
            return mApiServiceProduction.getIpNumberDetailsCall(IPNumber);
        }
    }

    public Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Body JsonObject request) {
        if (mIsStageBuild) {
            return apiService.getIpNumberEligibilityCheckRespCall(request);
        } else {
            return mApiServiceProduction.getIpNumberEligibilityCheckRespCall(request);
        }
    }

    public Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getIpNumberEligibilityCheckRespCall(request);
        } else {
            return mApiServiceProduction.getIpNumberEligibilityCheckRespCall(request);
        }
    }

    public Call<List<OtpValidationResp>> getOtpValidationRespCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getOtpValidationRespCall(request);
        } else {
            return mApiServiceProduction.getOtpValidationRespCall(request);
        }
    }

    public Call<List<BaseModel>> getResendEligibilityCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getResendEligibilityCall(request);
        } else {
            return mApiServiceProduction.getResendEligibilityCall(request);
        }
    }

    public Call<IpDetailsModel> getIpDetailsCall(@Body JsonObject request) {
        if (mIsStageBuild) {
            return apiService.getIpDetailsCall(request);
        } else {
            return mApiServiceProduction.getIpDetailsCall(request);
        }
    }

    public Call<IpDetailsModel> getIpDetailsCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getIpDetailsCall(request);
        } else {
            return mApiServiceProduction.getIpDetailsCall(request);
        }
    }

    public Call<List<AppointMentModel>> getAllAppointmentRespCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getAllAppointmentRespCall(request);
        } else {
            return mApiServiceProduction.getAllAppointmentRespCall(request);
        }
    }

    public Call<List<BaseModel>> getAadhaarOTPCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getAadhaarOTPCall(request);
        } else {
            return mApiServiceProduction.getAadhaarOTPCall(request);
        }
    }

    public Call<BaseModel> getUpdateAadhaarCall(@Body JsonObject request) {
        if (mIsStageBuild) {
            return apiService.getUpdateAadhaarCall(request);
        } else {
            return mApiServiceProduction.getUpdateAadhaarCall(request);
        }
    }

    public Call<List<BaseModel>> getResendAadharCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getResendAadharCall(request);
        } else {
            return mApiServiceProduction.getResendAadharCall(request);
        }
    }

    public Call<List<HospitalTimeSlotModel>> getHospitalTimeSlotRespCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getHospitalTimeSlotRespCall(request);
        } else {
            return mApiServiceProduction.getHospitalTimeSlotRespCall(request);
        }
    }

    public Call<List<ScheduledDatesModel>> getScheduledDatesCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getScheduledDatesCall(request);
        } else {
            return mApiServiceProduction.getScheduledDatesCall(request);
        }
    }

    public Call<String> getBookNewAppointmentCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getBookNewAppointmentCall(request);
        } else {
            return mApiServiceProduction.getBookNewAppointmentCall(request);
        }
    }

    public Call<String> getReScheduleAppointMentModelCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getReScheduleAppointMentModelCall(request);
        } else {
            return mApiServiceProduction.getReScheduleAppointMentModelCall(request);
        }
    }

    public Call<String> getAppointmentCancelCall(@Body JsonArray request) {
        if (mIsStageBuild) {
            return apiService.getAppointmentCancelCall(request);
        } else {
            return mApiServiceProduction.getAppointmentCancelCall(request);
        }
    }
}
