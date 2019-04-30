package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/20/2017.
 */

public class HospitalTimeSlotModel implements Serializable {

    private String TimeSlotMinutes;
    private String SlotAppointmentStatus;
    private String SlotPartofDay;
    private String ResponseCode;

    public String getTimeSlotMinutes() {
        return TimeSlotMinutes;
    }

    public void setTimeSlotMinutes(String timeSlotMinutes) {
        TimeSlotMinutes = timeSlotMinutes;
    }

    public String getSlotAppointmentStatus() {
        return SlotAppointmentStatus;
    }

    public void setSlotAppointmentStatus(String slotAppointmentStatus) {
        SlotAppointmentStatus = slotAppointmentStatus;
    }

    public String getSlotPartofDay() {
        return SlotPartofDay;
    }

    public void setSlotPartofDay(String slotPartofDay) {
        SlotPartofDay = slotPartofDay;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
