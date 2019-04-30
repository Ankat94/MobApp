package esiapp.esi.model;

import java.io.Serializable;

/**
 * Created by soorianarayanan on 9/2/17.
 */

public class UpcomingModel implements Serializable{
    private String name;
    private String appointment;
    private String dateAndtime;

    public UpcomingModel(String name, String appointment, String dateAndtime) {
        this.name = name;
        this.appointment = appointment;
        this.dateAndtime = dateAndtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getDateAndtime() {
        return dateAndtime;
    }

    public void setDateAndtime(String dateAndtime) {
        this.dateAndtime = dateAndtime;
    }

}
