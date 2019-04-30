package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/22/2017.
 */

public class IpDetailsModel implements Serializable {

    private IPDetails IpDetails;
    private String ResponseCode;

    public IPDetails getIpDetails() {
        return IpDetails;
    }

    public void setIpDetails(IPDetails ipDetails) {
        IpDetails = ipDetails;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
