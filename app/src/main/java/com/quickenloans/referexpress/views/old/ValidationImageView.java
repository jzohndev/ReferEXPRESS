package com.quickenloans.referexpress.views.old;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.quickenloans.referexpress.R;

/**
 * Created by chenige on 8/19/15.
 * This class is a custom ImageView that shows a checkmark when valid and a red X when invalid.
 */
public class ValidationImageView extends ImageView {

    public ValidationImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public void setValid() {
        setImageDrawable(getResources().getDrawable(R.drawable.ic_validation_yes));
        setTag("valid");
        setVisibility(View.VISIBLE);
    }

    public void setInvalid() {
        setImageDrawable(getResources().getDrawable(R.drawable.ic_validation_no));
        setTag("invalid");
        setVisibility(View.VISIBLE);
    }

    public boolean getValidation(){
        if (getTag().equals("valid")){
            return true;
        } else if (getTag().equals("invalid")){
            return false;
        } else {
            return false;
        }
    }


}
