package com.gayathri.enterpriselinchpin.gaurav.beans;

/**
 * Created by gaurav on 3/29/15.
 */
public class EmployeeRowItem {
    private String imageId;
    private String empName;
    private String empDesg;
    private String empDept;

    public EmployeeRowItem(String imageId,String empName, String empDesg, String empDept) {
        this.imageId = imageId;
        this.empName = empName;
        this.empDesg = empDesg;
        this.empDept = empDept;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDesg() {
        return empDesg;
    }

    public void setEmpDesg(String empDesg) {
        this.empDesg = empDesg;
    }

    public String getEmpDept() {
        return empDept;
    }

    public void setEmpDept(String empDept) {
        this.empDept = empDept;
    }

}