package org.onap.ccsdk.sli.plugins.sshapicall.model;

public enum AuthType {
    NONE, BASIC, KEY, UNSPECIFIED;

    public static AuthType fromString(String s) {
        if ("basic".equalsIgnoreCase(s))
            return BASIC;
        if ("key".equalsIgnoreCase(s))
            return KEY;
        if ("unspecified".equalsIgnoreCase(s))
            return UNSPECIFIED;
        if ("none".equalsIgnoreCase(s))
            return NONE;
        throw new IllegalArgumentException("Invalid value for format: " + s);
    }
}
