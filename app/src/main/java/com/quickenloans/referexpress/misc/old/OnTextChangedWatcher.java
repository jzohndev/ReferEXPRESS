package com.quickenloans.referexpress.misc.old;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by chenige on 8/18/15.
 *
 * This class is here because I do not want the beforeTextChanged and the afterTextchanged methods that you are forced to implement in TextWatcher
 */
public abstract class OnTextChangedWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
