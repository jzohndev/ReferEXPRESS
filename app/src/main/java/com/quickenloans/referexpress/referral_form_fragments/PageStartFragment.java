package com.quickenloans.referexpress.referral_form_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.helpers.ReferralInfoManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jdeville on 6/5/17.
 */

public class PageStartFragment extends BaseCompletableFragment {
    @BindView(R.id.tv_loanType)
    TextView tvLoanTypeTitle;
    private ReferralInfoManager referralInfoManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_form_start, container, false);
        ButterKnife.bind(this, contentView);

        setLoanTypeTitle();

        return contentView;
    }

    private void setLoanTypeTitle() {
        if (null == referralInfoManager) {
            referralInfoManager = ReferralInfoManager.getInstance();}

        tvLoanTypeTitle.setText(
                referralInfoManager.getReferralType()
                        + " Mortgage\n"
                        + referralInfoManager.getLoanType()
                        + "?");
    }

    @Override
    public void saveCompletedFieldsInfo() {
        // No fields to save
    }
}
