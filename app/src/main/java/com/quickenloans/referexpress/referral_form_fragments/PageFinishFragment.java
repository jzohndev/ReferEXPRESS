package com.quickenloans.referexpress.referral_form_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickenloans.referexpress.R;

/**
 * Created by jdeville on 6/5/17.
 */

public class PageFinishFragment extends BaseCompletableFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form_finish, container, false);

        return v;
    }

    @Override
    public void saveCompletedFieldsInfo() {
        // Nothing to save
    }
}
