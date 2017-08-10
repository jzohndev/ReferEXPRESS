package com.quickenloans.referexpress.data;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jdeville on 6/21/17.
 */

public abstract class ReferralTypes {
    public static final String UNSPECIFIED = "Unspecified";
    public static final String FORWARD_MORTGAGE = "Forward";
    public static final String REVERSE_MORTGAGE = "Reverse";

    @StringDef({UNSPECIFIED, FORWARD_MORTGAGE, REVERSE_MORTGAGE})
    @Retention(RetentionPolicy.SOURCE)

    public @interface NavigationMode {

    }
}