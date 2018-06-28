package com.softvision.model;


public enum InterviewerType {
    MANAGER,
    INTERVIEWER,
    ADMIN;

    @Override
    public String toString() {
        return MANAGER+","+ INTERVIEWER + "," + ADMIN ;
    }
}

