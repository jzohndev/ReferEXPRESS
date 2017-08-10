package com.quickenloans.referexpress.misc.old;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;

public class TheApplication extends Application {

	private String emailAddress;
	private boolean registered = false;
    private static final String TAG         = "TheApplication";
    private final String NDK_LIBRARY        = "refer_express_library";

	public TheApplication() {
	}

    @Override
    public void onCreate() {
        super.onCreate();

        System.loadLibrary(NDK_LIBRARY);


        // get the signing key and set the Environment variables based on it
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        String packageSignature = "";
        if (pi != null && pi.signatures != null) {
            packageSignature = pi.signatures[0].toCharsString();
        }

        // switch based on the key store signature
        if (packageSignature.equalsIgnoreCase(KeystoreSignatures.DEV_BUILD_KEYSTORE)) {
            Environment.setDevBuildEnvironmentVariables();
            Log.i(TAG, "Dev Build Environment Variables set.");
        } else if (packageSignature.equalsIgnoreCase(KeystoreSignatures.PROD_BUILD_KEYSTORE)) {
            Environment.setProdBuildEnvironmentVariables();
            Log.i(TAG, "Prod Build Environment Variables set.");
        } else {
            // case where the build is unknown - usually from Debug build
            Environment.setDevBuildEnvironmentVariables();
            Log.i(TAG, "No Build matched - Dev Build Environment Variables set.");
        }

        FlurryAgent.init(this, Environment.getFlurryAppKey());
        Crittercism.initialize(getApplicationContext(), Environment.getCrittercismAppId());


    }

    public boolean isRegistered() {
		return registered;
	}
	
	public void register() {
		registered = true;
	}
	
	public void setEmailAddress(String email) {
		emailAddress = email;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
}
