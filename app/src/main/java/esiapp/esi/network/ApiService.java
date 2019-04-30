package esiapp.esi.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import esiapp.esi.model.BaseModel;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.AvailableDatesModel;
import esiapp.esi.model.retroModel.BookNewAppointMentModel;
import esiapp.esi.model.retroModel.CreateSessionIdResp;
import esiapp.esi.model.retroModel.GetAllAppointmentResp;
import esiapp.esi.model.retroModel.HospitalTimeSlotModel;
import esiapp.esi.model.retroModel.HospitalTimeSlotResp;
import esiapp.esi.model.retroModel.IPEligibility;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.model.retroModel.IpNumberEligibilityCheckResp;
import esiapp.esi.model.retroModel.OtpValidationResp;
import esiapp.esi.model.retroModel.ReScheduleAppointMentModel;
import esiapp.esi.model.retroModel.SaveAdharDetailResp;
import esiapp.esi.model.retroModel.ScheduledDatesModel;
import esiapp.esi.model.retroModel.UpdateAdhaarResponse;
import esiapp.esi.model.retroModel.ValidateSessionIdResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by soorianarayanan on 13/2/17.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("http://164.164.87.43:91/IPD/Details/IPNumber")
    //@POST("http://172.16.80.89:621/IPD/Details/IPNumber")
    Call<IPNumberDetails> getIpNumberDetailsCall(@Field("IPNumber") String iPnumber);

    @FormUrlEncoded
    @POST("http://164.164.87.43:91/MAIE/Details/IPNumber")
    //@POST("http://172.16.80.89:623/MAIE/Details/IPNumber")
    Call<IPEligibility> getEligibilityCall(@Field("IPNumber") String iPnumber);

    @FormUrlEncoded
    @POST("/MAIE/Details/IPNumber")
    Call<SaveAdharDetailResp> getSaveAdharDetailRespCall(@Field("IPNumber") String iPnumber, @Field("AdhaarNo") String aadharNo, @Field("MobileNo") String mobileNo);

    @FormUrlEncoded
    @POST("http://164.164.87.43:91/MIAD/SaveDetails/AadharDetails")
    //@POST("http://172.16.80.89:622/MIAD/SaveDetails/AadharDetails")
    Call<UpdateAdhaarResponse> getUpdateAdhaarResponseCall(@Field("IPNumber") String iPNumber, @Field("AdhaarNo") String adhaarNo, @Field("MobileNo") String mobileNo);

    @FormUrlEncoded
    @POST("/API/Appointments/BookNew")
    Call<BookNewAppointMentModel> getBookNewAppointMentModelCall(@Field("IPNumber") String ipNumber, @Field("AadharNumber") String aadharNumber, @Field("PatientName") String patientName,
                                                                 @Field("PatientUHID") String patientUHID, @Field("PatientDOB") String patientDOB, @Field("Gender") String gender,
                                                                 @Field("AppointmentDate") String appointmentDate, @Field("HospitalCode") String hospitalCode, @Field("InsSeqNo") String insSeqNo,
                                                                 @Field("MobileNumber") String mobileNumber);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API10/Appointments/BookNew")
    //@POST("/API2/Appointments/BookNew")
    Call<String> getBookNewAppointmentCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API2/API/OTPValidate")
//    @POST("http://164.164.87.43:91/OTP/API/OTPValidate")
    //@POST("http://164.164.87.42:90/OTP/API/OTPValidate")
    Call<List<OtpValidationResp>> getOtpValidationRespCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/OTP/API/OTPValidate")
    //@POST("http://164.164.87.42:90/OTP/API/OTPValidate")
    Call<List<OtpValidationResp>> getOtpValidationRespCall(@Field("IPNumber") String ipNumber, @Field("MobileNumber") String mobileNumber,
                                                           @Field("SessionId") String sessionId, @Field("OTPNumber") String otpNumber);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/Validate/API/Create")
    //@POST("http://164.164.87.42:90/Validate/API/Create")
    Call<List<CreateSessionIdResp>> getCreateSessionIdRespCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/Validate/API/Validate")
    //@POST("http://164.164.87.42:90/Validate/API/Validate")
    Call<List<ValidateSessionIdResp>> getValidateSessionIdRespCall(@Body JsonArray request);

    @FormUrlEncoded
    @POST("http://164.164.87.43:91/API1/INS/IPEligibility")
    //@POST("http://164.164.87.42:90/API1/INS/IPEligibility")
    Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Field("IPNumber") String ipNumber,
                                                                           @Field("MobileNumber") String mobileNumber,
                                                                           @Field("Lang") String language);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API1/INS/IPEligibility")
    //@POST("http://164.164.87.42:90/API1/INS/IPEligibility")
    Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Body JsonObject request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API1/INS/IPEligibility")
        //@POST("http://164.164.87.42:90/API1/INS/IPEligibility")
    Call<IpNumberEligibilityCheckResp> getIpNumberEligibilityCheckRespCall(@Body JsonArray request);

    @FormUrlEncoded
    @POST("/API/Appointments/ReSchedule")
    Call<ReScheduleAppointMentModel> getReScheduleAppointMentModelCall(@Field("AppointmentID") String appointmentID,
                                                                       @Field("ReferenceNumber") String referenceNumber,
                                                                       @Field("NewAppointmentDate") String newAppointmentDate);

    @FormUrlEncoded
    @POST("/API/Appointments/Cancel")
    Call<BaseModel> getCancelAppointmentCall(@Field("ReferenceNumber") String referenceNumber);

    @FormUrlEncoded
    @POST("/API/Hospital/AvalilableDates")
    Call<AvailableDatesModel> getAvailableDatesModelCall(@Field("HospitalCode") String hospitalCode,
                                                         @Field("SelectedDateForAppointment") String selectedDateForAppointment,
                                                         @Field("SlotTypeAppointment") String slotTypeAppointment);

    @FormUrlEncoded
    @POST("/API1/Appointments/FetchByIP")
    Call<GetAllAppointmentResp> getAllAppointmentRespCall(@Field("IPNumber") String ipNumber, @Field("SessionId") String sessionId);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API6/Appointments/FetchByIP")
    //@POST("/API1/Appointments/FetchByIP")
    Call<List<AppointMentModel>> getAllAppointmentRespCall(@Body JsonArray request);

    @FormUrlEncoded
    @POST("/API3/Appointments/Reschedule")
    Call<ReScheduleAppointMentModel> getReScheduleAppointMentModelCall(@Field("IPNumber") String ipNumber,
                                                                       @Field("SessionId") String sessionId,
                                                                       @Field("AppointmentId") String appointmentId,
                                                                       @Field("ReferenceNumber") String referenceNumber,
                                                                       @Field("NewAppointmentDate") String newAppointmentDate);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API11/Appointments/ReSchedule")
    //@POST("/API3/Appointments/Reschedule")
    Call<String> getReScheduleAppointMentModelCall(@Body JsonArray request);

    @FormUrlEncoded
    @POST("/API6/Hospitals/TimeSlots")
    Call<HospitalTimeSlotResp> getHospitalTimeSlotRespCall(@Field("IPNumber") String ipNumber,
                                                           @Field("SessionId") String sessionId,
                                                           @Field("HospitalCode") String hospitalCode,
                                                           @Field("SelectedDateForAppointment") String selectedDateForAppointment);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API8/Hospitals/TimeSlots")
    //@POST("/API6/Hospitals/TimeSlots")
    Call<List<HospitalTimeSlotModel>> getHospitalTimeSlotRespCall(@Body JsonArray request);

    @FormUrlEncoded
    @POST("/API4/Appointments/Cancel")
    Call<BaseModel> getCancelAppointmentCall(@Field("IPNumber") String ipNumber, @Field("SessionId") String sessionId,
                                             @Field("ReferenceNumber") String referenceNumber);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API12/Appointments/Cancel")
    //@POST("/API4/Appointments/Cancel")
    Call<String> getAppointmentCancelCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API5/INS/IPDetails")
//    @POST("http://164.164.87.43:91/API2/INS/IPDetails")
    //@POST("http://164.164.87.42:90/API2/INS/IPDetails")
    Call<IpDetailsModel> getIpDetailsCall(@Body JsonObject request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API5/INS/IPDetails")
    Call<IpDetailsModel> getIpDetailsCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API3/INS/UpdateAadhaar")
    //@POST("http://164.164.87.42:90/API3/INS/UpdateAadhaar")
    Call<BaseModel> getUpdateAadhaarCall(@Body JsonObject request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API7/Hospitals/ScheduledDates")
    //@POST("/API5/Hospitals/ScheduledDates")
    Call<List<ScheduledDatesModel>> getScheduledDatesCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API4/INS/OTPAadhaar")
//    @POST("http://164.164.87.42:90/API4/INS/OTPAadhaar")
    Call<List<BaseModel>> getAadhaarOTPCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API5/INS/ResendEligibility")
//    @POST("http://164.164.87.42:90/API5/INS/ResendEligibility")
    Call<List<BaseModel>> getResendEligibilityCall(@Body JsonArray request);

    @Headers("Content-Type: application/json")
    @POST("http://164.164.87.43:91/API5/INS/ResendAadhaar")
//    @POST("http://164.164.87.42:90/API5/INS/ResendAadhaar")
    Call<List<BaseModel>> getResendAadharCall(@Body JsonArray request);
}