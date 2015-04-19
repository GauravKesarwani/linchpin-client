package com.gayathri.enterpriselinchpin.gaurav.beans;

/**
 * Created by gaurav on 4/4/15.
 */
public class EmployerRowItem {

    private String employerName;
    private String designation;
    private String startDate;
    private String endDate;

    public String getEmployerName() {
        return this.employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public EmployerRowItem(String employerName, String designation, String startDate, String endDate) {
        this.employerName = employerName;
        this.designation = designation;
        this.startDate = startDate;
        this.endDate = endDate;
    }



}
