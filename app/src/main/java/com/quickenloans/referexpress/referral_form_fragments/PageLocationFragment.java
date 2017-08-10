package com.quickenloans.referexpress.referral_form_fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.helpers.ReferralInfoManager;
import com.quickenloans.referexpress.interfaces.PageCompletionListener;
import com.quickenloans.referexpress.values.FragmentPositionValues;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

/**
 * Created by jdeville on 6/5/17.
 */

public class PageLocationFragment extends BaseCompletableFragment {
    private Activity mActivity;
    private ReferralInfoManager dataManager;
    private SearchableSpinner spnLocation;
    private RelativeLayout rlayout_addMessage, rlayout_editText;
    private View v;
    private EditText etMessage;
    private PageCompletionListener mCallback;
    private TextView tvPrimaryTextButton, tvSecondaryText;
    private AlertDialog mMessageDialog;


    // todo remove add message stuff


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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            checkAllFieldsComplete();
            //dataManager.displayDataLoggedToast(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_form_location, container, false);

        initFields();

        initControls();

        //initDialog();

        initLocationSpinner();

        return v;
    }

    private void initFields() {

        if (null == dataManager) {
            dataManager = ReferralInfoManager.getInstance();
        }

        mActivity = getActivity();
    }

    private void initControls() {
        spnLocation = (SearchableSpinner) v.findViewById(R.id.spn_stateSelector);
        /*rlayout_editText = (RelativeLayout) mActivity.getLayoutInflater()
                .inflate(R.layout.item_location_edittext, null);
        rlayout_addMessage = (RelativeLayout) v.findViewById(R.id.rlayout_addMessage);
        rlayout_addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMessageDialog();
            }
        });

        etMessage = (EditText) rlayout_editText.findViewById(R.id.et_message);
*/
        //tvPrimaryTextButton = (TextView) v.findViewById(R.id.tv_primaryText);
        //tvSecondaryText = (TextView) v.findViewById(R.id.tv_secondaryText);
    }

    /*private void initDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle("Include A Message")
                .setMessage("You can use this form to include a personalized message for your Personal Loan Expert!")
                .setView(rlayout_editText)
                .setPositiveButton("Accept", null)
                .setNegativeButton("Clear", null)
                .setNeutralButton("Cancel", null);

        mMessageDialog = builder.create();

        mMessageDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }*/

    private void initLocationSpinner() {
        final ArrayAdapter<String> locationSpinnerAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.item_location_spinner,
                getResources().getStringArray(R.array.states_full));

        spnLocation.setPositiveButton("Cancel");
        spnLocation.setTitle("");

        spnLocation.setAdapter(locationSpinnerAdapter);
        spnLocation.setSelection(0);

        spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                checkAllFieldsComplete();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean checkAllFieldsComplete() {
        final boolean validated = spnLocation.getSelectedItemPosition() > 0;
        mCallback.onPageCompletionChanged(FragmentPositionValues.LOCATION_FRAGMENT_4, validated);

        return validated;
    }

    @Override
    public void saveCompletedFieldsInfo() {
        if (spnLocation.getSelectedItemPosition() > 0) {
            dataManager.setLocation(spnLocation.getSelectedItem().toString());
        } else {
            dataManager.setLocation("");
        }
    }

    /*private void showAddMessageDialog() {

        mMessageDialog.show();

        if (!dataManager.getNote().isEmpty()) {
            final String note = dataManager.getNote();

            etMessage.setText(note);
            etMessage.setSelection(note.length());
        } else {
            etMessage.setText("");
        }

        // defining the onClickListeners outside of the setButton methods prevent closing onClick
        mMessageDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.setNote(etMessage.getText().toString());
                updateMessageView();
                hideKeyboard();
                mMessageDialog.dismiss();
            }
        });

        mMessageDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMessage.getText().clear();
            }
        });

        mMessageDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMessageView();
                hideKeyboard();
                mMessageDialog.dismiss();

            }
        });
    }*/

    /*private void hideKeyboard() {
        final EditText et = rlayout_editText.findViewById(R.id.et_message);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        *//*
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*//*
    }*/

    /*private void updateMessageView() {
        if (dataManager.getNote().isEmpty()) {
            setAddMessageView();
        } else {
            setEditMessageView();
        }
    }

    private void setAddMessageView() {
        tvPrimaryTextButton.setText("+ include a message");
        tvSecondaryText.setVisibility(View.VISIBLE);
    }*/

    private void setEditMessageView() {
        tvPrimaryTextButton.setText("Edit my message...");
        tvSecondaryText.setVisibility(View.GONE);

    }
}

