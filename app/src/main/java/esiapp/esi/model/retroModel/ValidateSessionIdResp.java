package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/17/2017.
 */

public class ValidateSessionIdResp implements Serializable {
    String ResponseCode;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
