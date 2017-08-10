package com.quickenloans.referexpress.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.quickenloans.referexpress.referral_form_fragments.BaseCompletableFragment;
import com.quickenloans.referexpress.referral_form_fragments.PageAdditionalFragment;
import com.quickenloans.referexpress.referral_form_fragments.PageContactFragment;
import com.quickenloans.referexpress.referral_form_fragments.PageFinishFragment;
import com.quickenloans.referexpress.referral_form_fragments.PageLocationFragment;
import com.quickenloans.referexpress.referral_form_fragments.PageNameFragment;
import com.quickenloans.referexpress.referral_form_fragments.PageStartFragment;
import com.quickenloans.referexpress.values.FragmentPositionValues;

/**
 * Created by jdeville on 6/16/17.
 */
public class ReferralFormAdapter extends FragmentPagerAdapter {
    private BaseCompletableFragment mCurrentFragment;

    public ReferralFormAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return FragmentPositionValues.TOTAL_FRAGMENTS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PageStartFragment();
            case 1:
                return new PageNameFragment();
            case 2:
                return new PageContactFragment();
            case 3:
                return new PageLocationFragment();
            case 4:
                return new PageAdditionalFragment();
            case 5:
                return new PageFinishFragment();
            default:
                return null;
        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    //...
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((BaseCompletableFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }


    public void saveCurrentFragmentData() {
        mCurrentFragment.saveCompletedFieldsInfo();
    }


}
