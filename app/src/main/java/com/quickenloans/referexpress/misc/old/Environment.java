package com.quickenloans.referexpress.misc.old;

/**
 * Created by mchannapatna on 8/7/14.
 */
public class Environment {

    private static String CRITTERCISM_APP_ID;

    private static String FLURRY_APP_KEY;

    private static String AUTH_KEY;

    private static String SUBMISSION_POST_URL;

    public static final String DOWNLOAD_URL = "http://mobiledev.rockfin.com/qlmobile/";

    // private constructor so class cannot be instantiated
    private Environment() {
        // do nothing
    }

    /*
     * Set the test build environment variables
     */
    public static void setDevBuildEnvironmentVariables() {

        CRITTERCISM_APP_ID = "53e3966a83fb7944df000002";
        FLURRY_APP_KEY = "QJWMZXFF8G99PQ9FG33N";
        AUTH_KEY = "9q0m5Gt2UT0ZM70q";
        SUBMISSION_POST_URL = "https://servicestest.quickenloans.com/leadsubmit/Submit.aspx?factory=0";
    }

    /*
     * Set the prod build environment variables
     */
    public static void setProdBuildEnvironmentVariables() {
        CRITTERCISM_APP_ID = "55ce00b2985ec40d0002c587";
        FLURRY_APP_KEY = "72M5FKJWGX3MDPC5WHKD";
        AUTH_KEY = "9q0m5Gt2UT0ZM70q";
        SUBMISSION_POST_URL = "https://servicestest.quickenloans.com/leadsubmit/Submit.aspx?factory=0";
        // SUBMISSION_POST_URL = "https://services.quickenloans.com/leadsubmit/Submit.aspx?factory=0"; todo real submission
    }


    public static String getAuthKeyId(){
        return AUTH_KEY;
    }

    public static String getCrittercismAppId() {
        return CRITTERCISM_APP_ID;
    }

    public static String getDownloadUrl() {
        return DOWNLOAD_URL;
    }

    public static String getFlurryAppKey() {
        return FLURRY_APP_KEY;
    }

    public static String getSubmissionPostUrl() {
        return SUBMISSION_POST_URL;
    }
}
