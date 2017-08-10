package com.quickenloans.referexpress.activities;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by jdeville on 6/6/17.
 */

public class ReferralFormPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();


        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(1);

        } else if (position <= 1) { // [-1,1]

            //dummyImageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }


    }

}
