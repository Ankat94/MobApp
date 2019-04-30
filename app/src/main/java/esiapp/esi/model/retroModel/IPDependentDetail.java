package esiapp.esi.model.retroModel;

import java.io.Serializable;

/**
 * Created by Randhir Kumar on 2/22/2017.
 */

public class IPDependentDetail implements Serializable {
    private String DependatName;
    private String Dep_DateofBirth;
    private String Relationship;
    private String Town;
    private String State;
    private String Dependent_Gender;
    private String DependentUHID;
    private String DEpendent_AadharNo;
    private String InsSeqNo;

    public String getDependatName() {
        return DependatName;
    }

    public void setDependatName(String dependatName) {
        DependatName = dependatName;
    }

    public String getDep_DateofBirth() {
        return Dep_DateofBirth;
    }

    public void setDep_DateofBirth(String dep_DateofBirth) {
        Dep_DateofBirth = dep_DateofBirth;
    }

    public String getRelationship() {
        return Relationship;
    }

    public void setRelationship(String relationship) {
        Relationship = relationship;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDependent_Gender() {
        return Dependent_Gender;
    }

    public void setDependent_Gender(String dependent_Gender) {
        Dependent_Gender = dependent_Gender;
    }

    public String getDEpendent_AadharNo() {
        return DEpendent_AadharNo;
    }

    public void setDEpendent_AadharNo(String DEpendent_AadharNo) {
        this.DEpendent_AadharNo = DEpendent_AadharNo;
    }

    public String getInsSeqNo() {
        return InsSeqNo;
    }

    public void setInsSeqNo(String insSeqNo) {
        InsSeqNo = insSeqNo;
    }

    public String getDependentUHID() {
        return DependentUHID;
    }

    public void setDependentUHID(String dependentUHID) {
        DependentUHID = dependentUHID;
    }
}
