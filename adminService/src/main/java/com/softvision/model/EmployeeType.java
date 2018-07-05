package com.softvision.model;

public enum EmployeeType {
    I("i"),
    A("a"),
    R("r");

    private String s;
    EmployeeType(String s) {
        this.s = s;
    }

    @Override
    public String toString(){
        return s;
    }
}

