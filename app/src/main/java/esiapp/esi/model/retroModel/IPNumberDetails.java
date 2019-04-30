package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by soorianarayanan on 10/2/17.
 */

public class IPNumberDetails implements Serializable {

    private String ResponseCode;
    private IPDetails IpDetails;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public IPDetails getIpDetails() {
        return IpDetails;
    }

    public void setIpDetails(IPDetails ipDetails) {
        IpDetails = ipDetails;
    }
}
