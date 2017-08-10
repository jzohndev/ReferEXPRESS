package com.quickenloans.referexpress.values;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jdeville on 7/18/17.
 */

public abstract class LoanTypes {
    public static final String UNSPECIFIED = "Unspecified";
    public static final String PURCHASE = "Purchase";
    public static final String REFINANCE = "Refinance";
    public static final String REVERSE_MORTGAGE = "Reverse Mortgage";

    @StringDef({UNSPECIFIED, PURCHASE, REFINANCE, REVERSE_MORTGAGE})
    @Retention(RetentionPolicy.SOURCE)

    public @interface NavigationMode {

    }
}
