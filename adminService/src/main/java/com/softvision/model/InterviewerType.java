package com.softvision.model;


public enum InterviewerType {
    M("m"),
    I("i"),
    A("a"),
    R("r");

    private String s;

    InterviewerType(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }


}

