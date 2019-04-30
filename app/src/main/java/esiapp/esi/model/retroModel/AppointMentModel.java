package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/20/2017.
 */

public class AppointMentModel implements Serializable {
    /**
     * "AppointmentId":7,
     * "PatientName":"Mrs AMIT KUMAR ",
     * "IPNumber":"1114525366",
     * "AadharNumber":"985579000051",
     * "AppointmentDateString":"15 Mar, 2017 10:20 AM",
     * "ReferenceNumber":"1002170173",
     * "InsSeqNo":"0",
     * "IsCancelled":1,
     * "IsCheckedIn":0,
     * "ShowAppointmentIn":"History",
     * "StatusDescription":"ReScheduled then Cancelled",
     * "MobileNumber":"",
     * "ResponseCode":"1200"
     */
    private long AppointmentId;
    private String PatientName;
    private String IPNumber;
    private String AadharNumber;
    private String AppointmentDateString;
    private String AppointmentHospitalLocation;
    private String ReferenceNumber;
    private String InsSeqNo;
    private Integer IsCancelled;
    private Integer IsCheckedIn;
    private String ShowAppointmentIn;
    private String StatusDescription;
    private String MobileNumber;
    private String ResponseCode;

    public long getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        AppointmentId = appointmentId;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        this.PatientName = patientName;
    }

    public String getIPNumber() {
        return IPNumber;
    }

    public void setIPNumber(String IPNumber) {
        this.IPNumber = IPNumber;
    }

    public String getAadharNumber() {
        return AadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        AadharNumber = aadharNumber;
    }

    public String getAppointmentDateString() {
        return AppointmentDateString;
    }

    public void setAppointmentDateString(String appointmentDateString) {
        AppointmentDateString = appointmentDateString;
    }

    public String getAppointmentHospitalLocation() {
        return AppointmentHospitalLocation;
    }

    public void setAppointmentHospitalLocation(String appointmentHospitalLocation) {
        AppointmentHospitalLocation = appointmentHospitalLocation;
    }

    public String getReferenceNumber() {
        return ReferenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        ReferenceNumber = referenceNumber;
    }

    public String getInsSeqNo() {
        return InsSeqNo;
    }

    public void setInsSeqNo(String insSeqNo) {
        InsSeqNo = insSeqNo;
    }

    public Integer getIsCancelled() {
        return IsCancelled;
    }

    public void setIsCancelled(Integer isCancelled) {
        IsCancelled = isCancelled;
    }

    public Integer getIsCheckedIn() {
        return IsCheckedIn;
    }

    public void setIsCheckedIn(Integer isCheckedIn) {
        IsCheckedIn = isCheckedIn;
    }

    public String getShowAppointmentIn() {
        return ShowAppointmentIn;
    }

    public void setShowAppointmentIn(String showAppointmentIn) {
        ShowAppointmentIn = showAppointmentIn;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
