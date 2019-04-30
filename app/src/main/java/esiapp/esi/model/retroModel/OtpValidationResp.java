package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/17/2017.
 */

public class OtpValidationResp implements Serializable {
    String IPNumber;
    String SessionId;
    String ResponseCode;
    String CreatedDate;

    public String getIPNumber() {
        return IPNumber;
    }

    public void setIPNumber(String IPNumber) {
        this.IPNumber = IPNumber;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
