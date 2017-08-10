package com.quickenloans.referexpress.referral_form_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.helpers.ReferralInfoManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jdeville on 8/7/17.
 */

public class PageAdditionalFragment extends BaseCompletableFragment {
    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_message) EditText etMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_form_additional, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
    @Override
    public void saveCompletedFieldsInfo() {
        final ReferralInfoManager dataManager = ReferralInfoManager.getInstance();
        if (!etMessage.getText().toString().isEmpty()){
            dataManager.setNote(etMessage.getText().toString());
        }

        if (!etName.getText().toString().isEmpty()){
            dataManager.setBanker(etName.getText().toString());
        }
    }

   /* private void init() {
        if (null == dataManager){
            dataManager = ReferralInfoManager.getInstance();
        }

        rlayoutMessage = (RelativeLayout) inflater.inflate(R.layout.item_location_edittext, null);
        etMessage = rlayoutMessage.findViewById(R.id.et_message);
        final AlertDialog.Builder messageBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Include a message")
                .setMessage("Have more to say? Leave a message for your banker here!")
                .setView(rlayoutMessage)
                .setPositiveButton("Accept", null)
                .setNegativeButton("Clear", null)
                .setNeutralButton("Cancel", null);
        messageDialog = messageBuilder.create();
        messageDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        rlayoutBanker = (RelativeLayout) inflater.inflate(R.layout.item_location_edittext, null);
        etBanker = rlayoutBanker.findViewById(R.id.et_message);
        final AlertDialog.Builder bankerBuilder = new AlertDialog.Builder(getActivity())
                //.setTitle("Include a message")
                .setMessage("Include a banker's name")
                .setView(rlayoutBanker)
                .setPositiveButton("Accept", null)
                .setNegativeButton("Clear", null)
                .setNeutralButton("Cancel", null);
        bankerDialog = messageBuilder.create();
        bankerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void showMessageDialog() {
        messageDialog.show();

        if (!dataManager.getNote().isEmpty()) {
            final String note = dataManager.getNote();

            etMessage.setText(note);
            etMessage.setSelection(note.length());
        } else {
            etMessage.setText("");
        }

        // defining the onClickListeners outside of the setButton methods prevent closing onClick
        messageDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.setNote(etMessage.getText().toString());
                updateMessageView();
                hideKeyboard();
                messageDialog.dismiss();
            }
        });

        messageDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMessage.getText().clear();
            }
        });

        messageDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMessageView();
                hideKeyboard();
                messageDialog.dismiss();

            }
        });
    }

    private void showBankerDialog() {
        bankerDialog.show();

        if (!dataManager.getBanker().isEmpty()) {
            final String banker = dataManager.getBanker();

            etBanker.setText(banker);
            etBanker.setSelection(banker.length());
        } else {
            etBanker.setText("");
        }

        // defining the onClickListeners outside of the setButton methods prevent closing onClick
        bankerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.setBanker(etBanker.getText().toString());
                updateBankerView();
                hideKeyboard();
                bankerDialog.dismiss();
            }
        });

        bankerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBanker.getText().clear();
            }
        });

        bankerDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBankerView();
                hideKeyboard();
                bankerDialog.dismiss();

            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etBanker.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(etMessage.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        *//*
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*//*
    }

    private void updateMessageView(){
        if (dataManager.getNote().isEmpty()){
            vfMessage.setDisplayedChild(0);
        } else {
            vfMessage.setDisplayedChild(1);
        }
    }

    private void updateBankerView(){
        if (dataManager.getBanker().isEmpty()){
            vfMessage.setDisplayedChild(0);
        } else {
            vfMessage.setDisplayedChild(1);
        }
    }*/


}
