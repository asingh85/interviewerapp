package com.softvision.model;


public enum TechnologyCommunity {
    JAVA,
    UI,
    QA,
    MAINFRAME,
    DOTNET,
    COMMUNITYHEAD;

    @Override
    public String toString() {
        return JAVA + "," + UI + "," + QA + "," + MAINFRAME + "," + DOTNET + "," + COMMUNITYHEAD;
    }
}

