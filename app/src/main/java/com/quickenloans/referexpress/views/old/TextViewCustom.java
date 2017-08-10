package com.quickenloans.referexpress.views.old;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.quickenloans.referexpress.misc.old.TypeFaceProvider;
import com.quickenloans.referexpress.R;

public class TextViewCustom extends TextView {

    public TextViewCustom(Context context) {
        this(context, null);
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewCustom);
        String typeFace = a.getString(R.styleable.TextViewCustom_tvOverriddenTypeface);
        a.recycle();

        init(context, typeFace);
    }

    private void init(Context context, String typeFace) {
        // isInEditMode is used to determine what code not to execute if this
        // view is been viewed in the layout editor
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