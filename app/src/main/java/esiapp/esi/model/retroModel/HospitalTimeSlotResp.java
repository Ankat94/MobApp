package esiapp.esi.model.retroModel;

import java.util.List;

/**
 * Created by Randhir Kumar on 2/20/2017.
 */

public class HospitalTimeSlotResp {
    List<HospitalTimeSlotModel> hospitalTimeSlotResps;

    public List<HospitalTimeSlotModel> getHospitalTimeSlotResps() {
        return hospitalTimeSlotResps;
    }

    public void setHospitalTimeSlotResps(List<HospitalTimeSlotModel> hospitalTimeSlotResps) {
        this.hospitalTimeSlotResps = hospitalTimeSlotResps;
    }
}
