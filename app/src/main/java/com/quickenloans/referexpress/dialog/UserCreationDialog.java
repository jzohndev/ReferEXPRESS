package com.quickenloans.referexpress.dialog;

;import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.interfaces.DefaultUserChangedListener;
import com.quickenloans.referexpress.data.User;
import com.quickenloans.referexpress.data.UserDAO;
import com.quickenloans.referexpress.misc.old.OnTextChangedWatcher;
import com.quickenloans.referexpress.misc.old.Validation;
import com.quickenloans.referexpress.views.old.ValidationImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.quickenloans.referexpress.values.ClassNames.MAIN_ACTIVITY;
import static com.quickenloans.referexpress.values.ClassNames.RETURN_ADDRESS;
import static com.quickenloans.referexpress.values.ClassNames.USER_CREATION_DIALOG;
import static com.quickenloans.referexpress.values.ClassNames.USER_SELECTION_DIALOG;

/**
 * Created by jdeville on 7/3/17.
 */

public class UserCreationDialog extends DialogFragment {
    private Activity mActivity;
    private UserDAO mUserDAO;
    private DefaultUserChangedListener updateUI;

    protected AlertDialog.Builder mBuilder;
    protected AlertDialog mDialog;
    protected View dialogView;

    protected MaterialEditText etFirstName, etLastName, etEmail;
    protected Button btnExit, btnFinish, btnClear;
    protected ValidationImageView ivFirstNameValidation, ivLastNameValidation, ivEmailValidation;


    // Attach listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            updateUI = (DefaultUserChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DefaultUserChangedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initDialog();

        return mDialog;
    }

    private void initDialog() {
        initFields();
        initControls();
        initView();
        initListeners();
    }

    protected void initFields() {
        mActivity = getActivity();
        mUserDAO = new UserDAO(mActivity);
        mBuilder = new AlertDialog.Builder(mActivity);

        final LayoutInflater inflater = mActivity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_dialog_user_creation, null);
    }

    protected void initControls() {

        btnExit = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnFinish = (Button) dialogView.findViewById(R.id.btn_finish);
        btnClear = (Button) dialogView.findViewById(R.id.btn_clear);

        etFirstName = (MaterialEditText) dialogView.findViewById(R.id.et_firstName);
        etLastName = (MaterialEditText) dialogView.findViewById(R.id.et_lastName);
        etEmail = (MaterialEditText) dialogView.findViewById(R.id.et_email);

        ivFirstNameValidation = (ValidationImageView) dialogView.findViewById(R.id.iv_firstNameValidation);
        ivLastNameValidation = (ValidationImageView) dialogView.findViewById(R.id.iv_lastNameValidation);
        ivEmailValidation = (ValidationImageView) dialogView.findViewById(R.id.iv_emailValidation);
    }

    private void initView() {
        mBuilder.setView(dialogView);
        mDialog = mBuilder.create();
    }

    protected void initListeners() {

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstName.setText("");
                etLastName.setText("");
                etEmail.setText("");
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInputs()) {
                    completeDialog();
                }
            }
        });


        // First Name EditText ***************************************************
        etFirstName.addTextChangedListener(new OnTextChangedWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onFirstNameInputChanged();
            }
        });

        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ivFirstNameValidation.setVisibility(View.INVISIBLE);
                    ivLastNameValidation.setVisibility(View.INVISIBLE);
                    ivEmailValidation.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Last Name EditText ***************************************************
        etLastName.addTextChangedListener(new OnTextChangedWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                onLastNameInputChanged();

            }
        });

        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ivFirstNameValidation.setVisibility(View.INVISIBLE);
                    ivLastNameValidation.setVisibility(View.INVISIBLE);
                    ivEmailValidation.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Email EditText ***************************************************
        etEmail.addTextChangedListener(new OnTextChangedWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                onEmailInputChanged();

            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ivFirstNameValidation.setVisibility(View.INVISIBLE);
                    ivLastNameValidation.setVisibility(View.INVISIBLE);
                    ivEmailValidation.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    protected boolean verifyInputs() {
        return true; // todo setup verification
    }

    protected void completeDialog() {
        int id = mUserDAO.insert(new User(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etEmail.getText().toString()
        ));

        mUserDAO.updateDefaultUser(id); // todo id of row may be different from user id
        exitDialog();
    }

    protected void exitDialog() {
        updateUI.onUserChanged();
        mDialog.dismiss();
    }

    protected String getReturnAddress() {
        return getArguments().getString(RETURN_ADDRESS);
    }

    protected Bundle bundleReturnAddress() {
        final Bundle args = new Bundle();
        args.putString(RETURN_ADDRESS, USER_CREATION_DIALOG);
        return args;
    }

    private void onFirstNameInputChanged() {
        if (checkIfFirstNameIsValid()) {
            checkIfAllFieldsComplete();
        } else {
            btnFinish.setEnabled(false);
        }
    }

    private void onLastNameInputChanged() {
        if (checkIfLastNameIsValid()) {
            checkIfAllFieldsComplete();
        } else {
            btnFinish.setEnabled(false);
        }
    }

    private void onEmailInputChanged() {
        if (checkIfEmailIsValid()) {
            checkIfAllFieldsComplete();
        } else {
            btnFinish.setEnabled(false);
        }
    }

    private void checkIfAllFieldsComplete() {
        if (!TextUtils.isEmpty(etFirstName.getText())
                && !TextUtils.isEmpty(etLastName.getText())
                && !TextUtils.isEmpty(etEmail.getText())) {

            if (checkIfFirstNameIsValid()
                    && checkIfLastNameIsValid()
                    && checkIfEmailIsValid()) {

                btnFinish.setEnabled(true);
            }
        }
    }

    private boolean checkIfFirstNameIsValid() {
        final boolean validated = Validation.isNameValid(etFirstName.getText().toString());

        updateItemValidationIcon(ivFirstNameValidation, validated);

        return validated;
    }

    private boolean checkIfLastNameIsValid() {
        final boolean validated = Validation.isLastNameValid(etLastName.getText().toString());

        updateItemValidationIcon(ivLastNameValidation, validated);

        return validated;

    }

    private boolean checkIfEmailIsValid() {
        final boolean validated = Validation.isEmailValid(etEmail.getText().toString());

        updateItemValidationIcon(ivEmailValidation, validated);

        return validated;
    }

    private void updateItemValidationIcon(ValidationImageView ivIcon, boolean validated) {
        if (validated) {
            ivIcon.setValid();
        } else {
            ivIcon.setInvalid();
        }
    }
}
