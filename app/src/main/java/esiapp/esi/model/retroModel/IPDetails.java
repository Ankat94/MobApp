package esiapp.esi.model.retroModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by soorianarayanan on 10/2/17.
 */

public class IPDetails implements Serializable {

    /**
     * "IPNumber",
     * "IPName",
     * "FirstDateofApp",
     * "Maritalstatus",
     * "Empeuhid",
     * "CreatedDateTime",
     * "IP_Gender",
     * "IPDateOfBirth",
     * "IP_AadharNo",
     * "HusbandName",
     * "DispCode",
     * "LocName",
     * "LocAddress",
     * "IPDependentDetail"
     */
    private String IPNumber;
    private String IPName;
    private String FirstDateofApp;
    private String Maritalstatus;
    private String Empeuhid;
    private String EnrollmentStatus;
    private String FatherName;
    private String CreatedDateTime;
    private String IP_Gender;
    private String IPDateOfBirth;
    private String IP_AadharNo;
    private String HusbandName;
    private String ResponseCode;
    private String DispCode;
    private String LocName;
    private String LocAddress;
    private List<IPDependentDetail> IPDependentDetail;


    public List<IPDependentDetail> getIPDependentDetail() {
        return IPDependentDetail;
    }

    public void setIPDependentDetail(List<IPDependentDetail> IPDependentDetail) {
        this.IPDependentDetail = IPDependentDetail;
    }

    public String getIPNumber() {
        return IPNumber;
    }

    public void setIPNumber(String IPNumber) {
        this.IPNumber = IPNumber;
    }

    public String getIPName() {
        return IPName;
    }

    public void setIPName(String IPName) {
        this.IPName = IPName;
    }

    public String getFirstDateofApp() {
        return FirstDateofApp;
    }

    public void setFirstDateofApp(String firstDateofApp) {
        FirstDateofApp = firstDateofApp;
    }

    public String getMaritalstatus() {
        return Maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        Maritalstatus = maritalstatus;
    }

    public String getEmpeuhid() {
        return Empeuhid;
    }

    public void setEmpeuhid(String empeuhid) {
        Empeuhid = empeuhid;
    }

    public String getEnrollmentStatus() {
        return EnrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        EnrollmentStatus = enrollmentStatus;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public String getIP_Gender() {
        return IP_Gender;
    }

    public void setIP_Gender(String IP_Gender) {
        this.IP_Gender = IP_Gender;
    }

    public String getIPDateOfBirth() {
        return IPDateOfBirth;
    }

    public void setIPDateOfBirth(String IPDateOfBirth) {
        this.IPDateOfBirth = IPDateOfBirth;
    }

    public String getIP_AadharNo() {
        return IP_AadharNo;
    }

    public void setIP_AadharNo(String IP_AadharNo) {
        this.IP_AadharNo = IP_AadharNo;
    }

    public String getHusbandName() {
        return HusbandName;
    }

    public void setHusbandName(String husbandName) {
        HusbandName = husbandName;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getDispCode() {
        return DispCode;
    }

    public void setDispCode(String dispCode) {
        DispCode = dispCode;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String locName) {
        LocName = locName;
    }

    public String getLocAddress() {
        return LocAddress;
    }

    public void setLocAddress(String locAddress) {
        LocAddress = locAddress;
    }
//
//    public DependantDetails getIPDependentDetail() {
//        return IPDependentDetail;
//    }
//
//    public void setIPDependentDetail(DependantDetails IPDependentDetail) {
//        this.IPDependentDetail = IPDependentDetail;
//    }
}
