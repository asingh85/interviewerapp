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
        switch(this) {
            case JAVA: return "JAVA";
            case UI: return "UI";
            case QA: return "QA";
            case MAINFRAME: return "MAINFRAME";
            case DOTNET: return "DOTNET";
            case COMMUNITYHEAD: return "COMMUNITYHEAD";
            default: throw new IllegalArgumentException();
        }
    }
}

