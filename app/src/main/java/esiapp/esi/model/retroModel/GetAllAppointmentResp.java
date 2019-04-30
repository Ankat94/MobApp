package esiapp.esi.model.retroModel;

import java.util.List;

/**
 * Created by Randhir Kumar on 2/20/2017.
 */

public class GetAllAppointmentResp {
    List<AppointMentModel> appointMentModelList;

    public List<AppointMentModel> getAppointMentModelList() {
        return appointMentModelList;
    }

    public void setAppointMentModelList(List<AppointMentModel> appointMentModelList) {
        this.appointMentModelList = appointMentModelList;
    }
}
