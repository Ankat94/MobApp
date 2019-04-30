package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/17/2017.
 */

public class IpNumberEligibilityCheckResp implements Serializable {
    private String IPnumber;
    private String IsIPEligible;
    private String ResponseCode;
    //    private String OtpCode;
    private String Sessionid;

    public String getIPnumber() {
        return IPnumber;
    }

    public void setIPnumber(String IPnumber) {
        this.IPnumber = IPnumber;
    }

    public String getIsIPEligible() {
        return IsIPEligible;
    }

    public void setIsIPEligible(String isIPEligible) {
        IsIPEligible = isIPEligible;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

//    public String getOtpCode() {
//        return OtpCode;
//    }
//
//    public void setOtpCode(String otpCode) {
//        OtpCode = otpCode;
//    }


    public String getSessionid() {
        return Sessionid;
    }

    public void setSessionid(String sessionid) {
        Sessionid = sessionid;
    }
}
