package com.softvision.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EmployeeType {
    INTERVIEWER("Interviewer"),
    RECRUITER("Recruiter");

    // Internal state
    private String employeeType;

    private EmployeeType(final String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    // Lookup table
    private static final Map lookup = new HashMap();

    // Populate the lookup table on loading time
    static {

        for (EmployeeType e : EnumSet.allOf(EmployeeType.class))
            lookup.put(e.getEmployeeType(), e);
    }

    // This method can be used for reverse lookup purpose
    public static EmployeeType get(String employeeType) {

        return (EmployeeType) lookup.get(employeeType);
    }

    public static void main(String[] args) {

        System.out.println(EmployeeType.INTERVIEWER.getEmployeeType());
        System.out.println(EmployeeType.RECRUITER.getEmployeeType());

        String dbEmployeeType = null;
        String sql = null;
        // Reverse lookup by Status when UI sending status to Rest service
        String employeeTypeFromUI = "Interviewer";
        System.out.println(EmployeeType.get(employeeTypeFromUI)); //INTERVIWER
        dbEmployeeType = EmployeeType.get(employeeTypeFromUI).toString();
        //Send this status to Database query
        sql = "select * from emp_type_table where employeeType = '" + dbEmployeeType + "'";
        System.out.println(sql);

        employeeTypeFromUI = "Recruiter";
        System.out.println(EmployeeType.get(employeeTypeFromUI));//RECRUITER
        dbEmployeeType = EmployeeType.get(employeeTypeFromUI).toString();
        sql = "select * from emp_type_table where employeeType = '" + dbEmployeeType + "'";
        System.out.println(sql);
    }
}
