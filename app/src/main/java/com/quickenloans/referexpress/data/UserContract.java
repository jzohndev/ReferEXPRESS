package com.quickenloans.referexpress.data;

import android.provider.BaseColumns;

/**
 * Created by jdeville on 6/21/17.
 */

public class UserContract {
    private UserContract() {}

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "USER";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_EMAIL = "email";
    }
}
