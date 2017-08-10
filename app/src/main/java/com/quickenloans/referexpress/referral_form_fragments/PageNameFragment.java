package com.quickenloans.referexpress.referral_form_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quickenloans.referexpress.helpers.ReferralInfoManager;
import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.interfaces.PageCompletionListener;
import com.quickenloans.referexpress.misc.old.OnTextChangedWatcher;
import com.quickenloans.referexpress.misc.old.Validation;
import com.quickenloans.referexpress.values.FragmentPositionValues;

/**
 * Created by jdeville on 6/5/17.
 */


public class PageNameFragment extends BaseCompletableFragment {

    private View v;
    private ReferralInfoManager dataManager;
    private EditText etFirstName, etLastName;
    private PageCompletionListener mCallback;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            checkIfAllFieldsComplete();
            //dataManager.displayDataLoggedToast(getContext());
        }
    }

    // Attach interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PageCompletionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PageCompletionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_form_name, container, false);

        initFields();

        initControls();

        initTextChangeListeners();

        return v;
    }

    private void initFields() {
        if (dataManager == null) {
            dataManager = ReferralInfoManager.getInstance();
        }
    }

    private void initControls() {
        etFirstName = (EditText) v.findViewById(R.id.et_firstName);
        etLastName = (EditText) v.findViewById(R.id.et_lastName);
    }

    private void initTextChangeListeners() {

        etFirstName.addTextChangedListener(new OnTextChangedWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

                checkIfAllFieldsComplete();
            }
        });

        etLastName.addTextChangedListener(new OnTextChangedWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

                checkIfAllFieldsComplete();
            }
        });
    }

    private void checkIfAllFieldsComplete() {
        if (checkIfFirstNameIsValid() && checkIfLastNameIsValid()) {
            mCallback.onPageCompletionChanged(FragmentPositionValues.NAME_FRAGMENT_2, true);
        } else {
            mCallback.onPageCompletionChanged(FragmentPositionValues.NAME_FRAGMENT_2, false);
        }
    }

    private boolean checkIfFirstNameIsValid() {
        return Validation.isNameValid(etFirstName.getText().toString());
    }

    private boolean checkIfLastNameIsValid() {
        return Validation.isLastNameValid(etLastName.getText().toString());
    }

    @Override
    public void saveCompletedFieldsInfo() {
        if (checkIfFirstNameIsValid() && checkIfLastNameIsValid()) {
            dataManager.setFirstName(etFirstName.getText().toString());
            dataManager.setLastName(etLastName.getText().toString());
        } else {
            dataManager.setFirstName("");
            dataManager.setLastName("");
        }
    }

}

