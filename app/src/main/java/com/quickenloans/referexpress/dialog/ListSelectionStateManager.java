package com.quickenloans.referexpress.dialog;

import android.util.Log;

import static com.quickenloans.referexpress.values.ClassNames.DIALOG_STATE_MANAGER;

/**
 * Created by jdeville on 6/30/17.
 */

public class ListSelectionStateManager {
    public static final ListSelectionStateManager ourInstance = new ListSelectionStateManager();

    public static final int NO_USER_SELECTED = 0;
    public static final int USER_SELECTED = 1;
    //
    private static int sDialogState;
    private static long sSelectedItemId;

    //




    public static ListSelectionStateManager getInstance() {
        return ourInstance;
    }

    private ListSelectionStateManager() {
        initialize();

    }

    ///////
    public void updateDialogState(int value){
        ListSelectionStateManager.sDialogState = value;
    }

    public int getDialogState(){
        return sDialogState;
    }

    //...
    public long getSelectedItemId(){
        Log.e(DIALOG_STATE_MANAGER, Integer.toString((int) sSelectedItemId));
        return ListSelectionStateManager.sSelectedItemId;
    }

    public void setSelectedItemId(long value) {
        ListSelectionStateManager.sSelectedItemId = value;
    }


    //
    private void initialize(){
        sDialogState = NO_USER_SELECTED;
        sSelectedItemId = -1;
    }
}
