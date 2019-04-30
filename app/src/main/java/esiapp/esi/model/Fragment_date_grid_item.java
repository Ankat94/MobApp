package esiapp.esi.model;

import java.io.Serializable;

/**
 * Created by soorianarayanan on 8/2/17.
 */

public class Fragment_date_grid_item implements Serializable{

    private String time_no;
    private String am_pm;

    public Fragment_date_grid_item(String time_no, String am_pm) {
        this.time_no = time_no;
        this.am_pm = am_pm;
    }

    public String getTime_no() {
        return time_no;
    }

    public void setTime_no(String time_no) {
        this.time_no = time_no;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }
}
