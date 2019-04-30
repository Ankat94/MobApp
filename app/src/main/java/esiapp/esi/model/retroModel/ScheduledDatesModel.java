package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/23/2017.
 */

public class ScheduledDatesModel implements Serializable {
    private String HospitalCode;
    private String SchedulingRangeFrom;
    private String SchedulingRangeTo;
    private String ResponseCode;

    public String getHospitalCode() {
        return HospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        HospitalCode = hospitalCode;
    }

    public String getSchedulingRangeFrom() {
        return SchedulingRangeFrom;
    }

    public void setSchedulingRangeFrom(String schedulingRangeFrom) {
        SchedulingRangeFrom = schedulingRangeFrom;
    }

    public String getSchedulingRangeTo() {
        return SchedulingRangeTo;
    }

    public void setSchedulingRangeTo(String schedulingRangeTo) {
        SchedulingRangeTo = schedulingRangeTo;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
