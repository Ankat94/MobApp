package esiapp.esi.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.HospitalTimeSlotModel;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.model.retroModel.IpNumberEligibilityCheckResp;
import esiapp.esi.model.retroModel.OtpValidationResp;
import esiapp.esi.model.retroModel.ScheduledDatesModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Randhir Kumar on 3/9/2017.
 */

public interface ApiServiceProduction {
    @FormUrlEncoded
    @POST("http://164.164.87.43:91/IPD/Details/IPNumber")
//    @POST("http://172.16.80.89:621/IPD/Details/IPNumber")
    Call<IPNumberDetails> getIpNumberDetailsCall(@Field("IPNumber") String iPnumber);


    @Headers("Content-Type: application/json")
    @POST("/API10/Appointments/BookNew")
    Call<String> getBookNewAppointmentCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API2/API/OTPValidate")
    Call<List<OtpValidationResp>> getOtpValidationRespCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API1/INS/IPEligibility")
    Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Body JsonObject request);

    @Headers("Content-Type: application/json")
    @POST("/API1/INS/IPEligibility")
    Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Body JsonArray request);


    @Headers("Content-Type: application/json")
    @POST("/API6/Appointments/FetchByIP")
    Call<List<AppointMentModel>> getAllAppointmentRespCall(@Body JsonArray request);


    @Headers("Content-Type: application/json")
    @POST("/API11/Appointments/ReSchedule")
    Call<String> getReScheduleAppointMentModelCall(@Body JsonArray request);


    @Headers("Content-Type: application/json")
    @POST("/API8/Hospitals/TimeSlots")
    Call<List<HospitalTimeSlotModel>> getHospitalTimeSlotRespCall(@Body JsonArray request);


    @Headers("Content-Type: application/json")
    @POST("/API12/Appointments/Cancel")
    Call<String> getAppointmentCancelCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API5/INS/IPDetails")
    Call<IpDetailsModel> getIpDetailsCall(@Body JsonObject request);

    @Headers("Content-Type: application/json")
    @POST("/API5/INS/IPDetails")
    Call<IpDetailsModel> getIpDetailsCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API4/INS/UpdateAadhaar")
    Call<BaseModel> getUpdateAadhaarCall(@Body JsonObject request);

    @Headers("Content-Type: application/json")
    @POST("/API7/Hospitals/ScheduledDates")
    Call<List<ScheduledDatesModel>> getScheduledDatesCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API3/INS/OTPAadhaar")
    Call<List<BaseModel>> getAadhaarOTPCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API14/INS/ResendEligibility")
    Call<List<BaseModel>> getResendEligibilityCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("/API14/INS/ResendAadhaar")
    Call<List<BaseModel>> getResendAadharCall(@Body JsonArray request);
}
