/*
    Copyright (c) 2014 Quicken Loans Inc. All rights reserved.
 */

package com.quickenloans.referexpress.views.old;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.quickenloans.referexpress.misc.old.TypeFaceProvider;
import com.quickenloans.referexpress.R;

/**
 * Created by jmitchell1 on 10/3/13.
 */
public class EditTextCustom extends EditText {

    private static final String TAG = EditTextCustom.class.getSimpleName();

    public EditTextCustom(Context context) {
        this(context, null);
    }

    public EditTextCustom(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("editTextStyle", "attr", "android"));
    }

    public EditTextCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextCustom);
        String typeFace = a.getString(R.styleable.EditTextCustom_etOverridenTypeface);
        a.recycle();

        init(context, typeFace);
    }

    private void init(Context context, String typeFace) {
        if (!isInEditMode()) {
            // set the typeface
            if (typeFace != null) {
                Typeface tfInput = TypeFaceProvider.getTypeFace(context, typeFace);
                setTypeface(tfInput);
            } else {
                // default typeface
                Typeface tfRobotoCondensed = TypeFaceProvider.getTypeFace(context, "MyriadPro-Regular.otf");
                setTypeface(tfRobotoCondensed);
            }
        }
    }



}
