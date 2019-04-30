package esiapp.esi.model;

import java.io.Serializable;

/**
 * Created by soorianarayanan on 9/2/17.
 */

public class ChoosePatientDepdentListItemModel implements Serializable {

    public String patientName;
    public String patientAadharNumber;
    public boolean isAadhar;

    public ChoosePatientDepdentListItemModel(String patientName, String patientAadharNumber, boolean isAadhar) {
        this.patientName = patientName;
        this.patientAadharNumber = patientAadharNumber;
        this.isAadhar = isAadhar;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAadharNumber() {
        return patientAadharNumber;
    }

    public void setPatientAadharNumber(String patientAadharNumber) {
        this.patientAadharNumber = patientAadharNumber;
    }

    public boolean isAadhar() {
        return isAadhar;
    }

    public void setAadhar(boolean aadhar) {
        isAadhar = aadhar;
    }

}
