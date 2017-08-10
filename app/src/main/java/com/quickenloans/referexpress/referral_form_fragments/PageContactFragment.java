package com.quickenloans.referexpress.referral_form_fragments;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.quickenloans.referexpress.helpers.ReferralInfoManager;
import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.interfaces.PageCompletionListener;
import com.quickenloans.referexpress.values.FragmentPositionValues;
import com.quickenloans.referexpress.views.old.ValidationImageView;
import com.quickenloans.referexpress.misc.old.LeadSubmissionFields;
import com.quickenloans.referexpress.misc.old.OnTextChangedWatcher;
import com.quickenloans.referexpress.misc.old.Utility;
import com.quickenloans.referexpress.misc.old.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdeville on 6/5/17.
 */

public class PageContactFragment extends BaseCompletableFragment {
    private static final int FRAGMENT_NUMBER = 2;
    private static final int MAX_NUMBER_OF_PHONE_NUMBERS
            = LeadSubmissionFields.PHONE_NUMBER_TYPES.length;

    private View v;
    private ReferralInfoManager dataManager;
    private Button btnAddPhone, btnAddEmail;
    private LinearLayout layoutPhoneItems, layoutEmailItems;
    private TextView tvInvalidPhoneNumberTypes, tvNonUniquePhoneNumbers;

    PageCompletionListener mCallback;

    // Attach listener
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_form_contact, container, false);

        initFields();

        initControls();

        initListeners();


        return v;
    }

    private void initControls() {
        btnAddPhone = (Button) v.findViewById(R.id.btn_addPhone);
        btnAddEmail = (Button) v.findViewById(R.id.btn_addEmail);

        layoutPhoneItems = (LinearLayout) v.findViewById(R.id.llayout_phoneItems);
        layoutEmailItems = (LinearLayout) v.findViewById(R.id.llayout_emailItems);

        tvInvalidPhoneNumberTypes = (TextView) v.findViewById(R.id.tv_invalidPhoneNumberTypesError);
        tvNonUniquePhoneNumbers = (TextView) v.findViewById(R.id.tv_nonUniquePhoneNumbersError);

        addPhoneItem();
    }

    private void initFields() {
        if (dataManager == null) {
            dataManager = ReferralInfoManager.getInstance();
        }
    }

    private void initListeners() {
        btnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoneItem();
            }
        });

        btnAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmailItem();
            }
        });
    }

    private void addPhoneItem() {
        final View contactItem = LayoutInflater.from(getContext())
                .inflate(R.layout.item_phone_number_input, null);
        setupPhoneItem(contactItem);
        layoutPhoneItems.addView(contactItem);
        checkNumberOfPhoneItems();
        onPhoneTypeChanged();
        checkAllFieldsComplete();
    }

    private void addEmailItem() {
        final View contactItem = LayoutInflater.from(getContext())
                .inflate(R.layout.item_email_input, null);
        setupEmailItem(contactItem);
        layoutEmailItems.addView(contactItem);
        checkNumberOfEmailItems();
        onEmailInputChanged();
        checkAllFieldsComplete();
    }

    private void setupPhoneItem(final View contactItem) {
        final ValidationImageView ivItemValidation
                = (ValidationImageView) contactItem.findViewById(R.id.iv_checkmarkValidation);
        final Spinner spnPhoneType = (Spinner) contactItem.findViewById(R.id.spn_phoneNumberType);
        final EditText etPhoneNumber = (EditText) contactItem.findViewById(R.id.et_phoneNumber);
        final ImageView btnRemove = (ImageView) contactItem.findViewById(R.id.iv_deletePhone);

        // Setup Spinner
        final ArrayAdapter<String> phoneNumberTypeAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item,
                LeadSubmissionFields.PHONE_NUMBER_TYPES);

        phoneNumberTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPhoneType.setAdapter(phoneNumberTypeAdapter);
        spnPhoneType.setFocusable(true);
        spnPhoneType.setSelection(0, true);

        // Setup Spinner listeners
        spnPhoneType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //stupid android http://stackoverflow.com/questions/2636098/android-spinner-selection/2649198#2649198
            //int lastPosition = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if (lastPosition != position) {
                //  lastPosition = position;
                onPhoneTypeChanged();
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onPhoneTypeChanged();
            }
        });

        spnPhoneType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideKeyboard(getActivity());
                return false;
            }
        });

        spnPhoneType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onPhoneTypeChanged();
                }
            }
        });

        // Setup EditText listeners
        etPhoneNumber.addTextChangedListener(new OnTextChangedWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onPhoneInputChanged();
            }
        });

        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    updateItemValidationIcon(
                            ivItemValidation,
                            checkPhoneNumberValid(etPhoneNumber.getText().toString())
                    );

                    onPhoneInputChanged();
                }
            }
        });

        etPhoneNumber.setKeyListener(DigitsKeyListener.getInstance(" ()-0123456789"));

        etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Setup 'remove' Button
        if (layoutPhoneItems.getChildCount() == 0) {
            btnRemove.setVisibility(View.INVISIBLE);
        } else {
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutPhoneItems.removeView(contactItem);
                    checkNumberOfPhoneItems();
                    onPhoneInputChanged();
                    onPhoneTypeChanged();
                }
            });
        }

        // Start animation for new item added
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);

        animation.setDuration(100);
        animation.reset();
        contactItem.clearAnimation();
        contactItem.startAnimation(animation);

        //Request focus
        etPhoneNumber.requestFocus();
    }

    private void setupEmailItem(final View contactItem) {
        final ValidationImageView ivItemValidation
                = (ValidationImageView) contactItem.findViewById(R.id.iv_checkmarkValidation);
        final EditText etEmail = (EditText) contactItem.findViewById(R.id.et_email);
        final ImageView btnRemove = (ImageView) contactItem.findViewById(R.id.iv_deleteEmail);

        // Setup EditText listeners
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

                    updateItemValidationIcon(
                            ivItemValidation,
                            checkPhoneNumberValid(etEmail.getText().toString())
                    );

                    onEmailInputChanged();
                }
            }
        });


        // Setup 'Remove' button
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEmailItems.removeView(contactItem);
                checkNumberOfEmailItems();
                onEmailInputChanged();
            }
        });

        // Start animation for new item added
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);

        animation.setDuration(100);
        animation.reset();
        contactItem.clearAnimation();
        contactItem.startAnimation(animation);

        etEmail.requestFocus();
    }

    //
    //
    //
    //

    private void onPhoneTypeChanged() {

        if (checkAllPhoneTypesUnique()) {
            tvInvalidPhoneNumberTypes.setVisibility(View.GONE);
            checkAllFieldsComplete();
        } else {
            tvInvalidPhoneNumberTypes.setVisibility(View.VISIBLE);
            mCallback.onPageCompletionChanged(FragmentPositionValues.CONTACT_FRAGMENT_3, false);
        }
    }

    private void onPhoneInputChanged() {

        if (checkAllPhoneNumbersUnique()) {
            if (checkAllPhoneNumbersValid()) {
                checkAllFieldsComplete();
            } else {
                mCallback.onPageCompletionChanged(FragmentPositionValues.CONTACT_FRAGMENT_3, false);
            }
        } else {
            mCallback.onPageCompletionChanged(FragmentPositionValues.CONTACT_FRAGMENT_3, false);
        }
    }

    private void onEmailInputChanged() {

        if (checkAllEmailsValid()) {
            if (checkAllEmailsUnique()) {
                checkAllFieldsComplete();
            } else {
                mCallback.onPageCompletionChanged(FragmentPositionValues.CONTACT_FRAGMENT_3, false);
            }
        } else {
            mCallback.onPageCompletionChanged(FragmentPositionValues.CONTACT_FRAGMENT_3, false);
        }
    }

    //
    //
    //
    //

    private boolean checkAllFieldsComplete() {

        final boolean validated = checkAllPhoneTypesUnique()
                && checkAllPhoneNumbersValid()
                && checkAllPhoneNumbersUnique()
                && checkAllEmailsValid()
                && checkAllEmailsUnique();

        mCallback.onPageCompletionChanged(FragmentPositionValues.CONTACT_FRAGMENT_3, validated);
        return validated;
    }

    // Phone TYPE Validation method
    private boolean checkAllPhoneTypesUnique() {
        ArrayList<String> phoneNumberTypesUsed = new ArrayList<>();
        final int phoneNumbersInputed = layoutPhoneItems.getChildCount();
        View currentItem;
        Spinner spnCurrentItem;

        for (int i = 0; i < phoneNumbersInputed; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            spnCurrentItem = (Spinner) currentItem.findViewById(R.id.spn_phoneNumberType);
            phoneNumberTypesUsed.add(spnCurrentItem.getSelectedItem().toString());
        }

        return !Utility.hasDuplicate(phoneNumberTypesUsed);
    }

    // Phone NUMBER Validation methods
    private boolean checkAllPhoneNumbersValid() {
        final int phoneItems = layoutPhoneItems.getChildCount();
        View currentItem;
        EditText etPhoneNumber;
        ValidationImageView ivItemValidation;

        for (int i = 0; i < phoneItems; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            etPhoneNumber = (EditText) currentItem.findViewById(R.id.et_phoneNumber);
            ivItemValidation = (ValidationImageView) currentItem.findViewById(R.id.iv_checkmarkValidation);

            final boolean validated = checkPhoneNumberValid(etPhoneNumber.getText().toString());
            updateItemValidationIcon(ivItemValidation, validated);

            if (!validated) {
                return false;
            }
        }

        return true;
    }

    private boolean checkPhoneNumberValid(String phoneNumber) {
        return Validation.isPhoneValid(phoneNumber);
    }

    private boolean checkAllPhoneNumbersUnique() {
        ArrayList<String> phoneNumbers = new ArrayList<>();
        final int phoneNumbersInputed = layoutPhoneItems.getChildCount();
        View currentItem;
        EditText etCurrentItem;

        for (int i = 0; i < phoneNumbersInputed; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            etCurrentItem = (EditText) currentItem.findViewById(R.id.et_phoneNumber);
            phoneNumbers.add(etCurrentItem.getText().toString());
        }

        if (!Utility.hasDuplicate(phoneNumbers)) {
            tvNonUniquePhoneNumbers.setVisibility(View.GONE);
            return true;
        } else {
            tvNonUniquePhoneNumbers.setVisibility(View.VISIBLE);
            return false;
        }
    }

    // Email Validation methods
    private boolean checkAllEmailsValid() {
        final int emailItems = layoutEmailItems.getChildCount();
        View currentItem;
        EditText etEmail;
        ValidationImageView ivItemValidation;

        for (int i = 0; i < emailItems; i++) {
            currentItem = layoutEmailItems.getChildAt(i);
            etEmail = (EditText) currentItem.findViewById(R.id.et_email);
            ivItemValidation = (ValidationImageView) currentItem.findViewById(R.id.iv_checkmarkValidation);

            final boolean validated = checkEmailValid(etEmail.getText().toString());
            updateItemValidationIcon(ivItemValidation, validated);

            if (!validated) {
                return false;
            }
        }

        return true;
    }

    private boolean checkEmailValid(String email) {
        return Validation.isEmailValid(email);
    }

    private boolean checkAllEmailsUnique() {
        ArrayList<String> emails = new ArrayList<>();
        final int emailsInputed = layoutEmailItems.getChildCount();
        View currentItem;
        EditText etCurrentItem;

        for (int i = 0; i < emailsInputed; i++) {
            currentItem = layoutEmailItems.getChildAt(i);
            etCurrentItem = (EditText) currentItem.findViewById(R.id.et_email);
            emails.add(etCurrentItem.getText().toString());
        }

        return !Utility.hasDuplicate(emails);
    }

    //
    //
    //
    //

    private void updateItemValidationIcon(ValidationImageView ivIcon, boolean validated) {
        if (validated) {
            ivIcon.setValid();
        } else {
            ivIcon.setInvalid();
        }
    }

    private void checkNumberOfPhoneItems() {
        final int phoneItemsInLayout = layoutPhoneItems.getChildCount();

        if (phoneItemsInLayout < MAX_NUMBER_OF_PHONE_NUMBERS) {
            updateAddPhoneButton(true);
        } else {
            updateAddPhoneButton(false);
        }
    }

    private void checkNumberOfEmailItems() {
        final int emailItemsInLayout = layoutEmailItems.getChildCount();

        if (emailItemsInLayout < 1){
            updateAddEmailButton(true);
        } else {
            updateAddEmailButton(false);
        }
    }

    private void updateAddPhoneButton(boolean doEnable) {
     btnAddPhone.setEnabled(doEnable);
    }

    private void updateAddEmailButton(boolean doEnable){
        btnAddEmail.setEnabled(doEnable);
    }

    //
    //
    //
    //

   /* private void onPhoneNumberInputChanged() {
        validateAllPhoneNumberInputs();
        validateAllPhoneNumberTypesAreUnique();
        checkIfAllFieldsAreComplete();
    }*/

   /* private void onEmailInputChanged() {
        validateAllEmailInputs();
        checkIfAllEmailFieldsAreValid();
        checkIfAllFieldsAreComplete();
    }*/

   /* private void checkIfAllFieldsAreComplete() {
        if (checkIfAllFieldsAreValid()) {
            mCallback.onPageCompletionChanged(FRAGMENT_NUMBER, true);
        } else {
            mCallback.onPageCompletionChanged(FRAGMENT_NUMBER, false);
        }
    }*/

    /* private void validateAllPhoneNumberInputs() {
        final int phoneItemsTotal = layoutPhoneItems.getChildCount();

        View currentItem = null;
        EditText etPhoneNumber = null;
        ValidationImageView ivItemValidation = null;

        for (int i = 0; i < phoneItemsTotal; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            etPhoneNumber = (EditText) currentItem.findViewById(R.id.et_phoneNumber);
            ivItemValidation = (ValidationImageView) currentItem.findViewById(R.id.iv_checkmarkValidation);

            if (Validation.isPhoneValid(etPhoneNumber.getText().toString())) {
                ivItemValidation.setValid();
            } else {
                ivItemValidation.setInvalid();
            }
        }
    } */

    /*private void validateAllPhoneNumberTypesAreUnique() {
        if (checkForDuplicatePhoneNumberTypes()) {
            tvInvalidPhoneNumberTypes.setVisibility(View.GONE);
        } else {
            updateDuplicatePhoneTypeValidationImageView();
            tvInvalidPhoneNumberTypes.setVisibility(View.VISIBLE);
            clearPhoneMap();
        }
    }*/

   /* private void validateAllEmailInputs() {
        final int emailItemsTotal = layoutEmailItems.getChildCount();

        View currentItem;
        EditText etEmail;
        ValidationImageView ivItemValidation;

        for (int i = 0; i < emailItemsTotal; i++) {
            currentItem = layoutEmailItems.getChildAt(i);
            etEmail = (EditText) currentItem.findViewById(R.id.et_email);
            ivItemValidation = (ValidationImageView) currentItem.findViewById(R.id.iv_checkmarkValidation);

            if (Validation.isEmailValid((etEmail.getText().toString()))) {
                ivItemValidation.setValid();
            } else {
                ivItemValidation.setInvalid();
            }
        }
    }

    private void updateDuplicatePhoneTypeValidationImageView() {
        ArrayList<Integer> phoneNumberTypesUsed = new ArrayList<>();
        final int phoneNumbersInputed = layoutPhoneItems.getChildCount();
        View currentItem = null;
        Spinner spnCurrentItem = null;

        for (int i = 0; i < phoneNumbersInputed; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            spnCurrentItem = (Spinner) currentItem.findViewById(R.id.spn_phoneNumberType);


            if (phoneNumberTypesUsed.contains(spnCurrentItem.getSelectedItemPosition())) {
                final ValidationImageView phoneCheck = (ValidationImageView)
                        currentItem.findViewById(R.id.iv_checkmarkValidation);
                phoneCheck.setInvalid();
            } else {
                phoneNumberTypesUsed.add(spnCurrentItem.getSelectedItemPosition());
            }
        }
    }*/

   /* private boolean checkForDuplicatePhoneNumberTypes() {
        ArrayList<String> phoneNumberTypesUsed = new ArrayList<>();
        final int phoneNumbersInputed = layoutPhoneItems.getChildCount();
        View currentItem = null;
        Spinner spnCurrentItem = null;

        for (int i = 0; i < phoneNumbersInputed; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            spnCurrentItem = (Spinner) currentItem.findViewById(R.id.spn_phoneNumberType);
            phoneNumberTypesUsed.add(spnCurrentItem.getSelectedItem().toString());
        }

        return !Utility.hasDuplicate(phoneNumberTypesUsed);
    }*/

   /* public boolean checkIfAllFieldsAreValid() {
        return (checkIfAllPhoneItemFieldsAreValid()
                && checkForDuplicatePhoneNumberTypes()
                && checkIfAllEmailFieldsAreValid());
    }
*/
   /* public boolean checkIfAllPhoneItemFieldsAreValid() {
        boolean isValid = true;
        final int phoneNumbersInputed = layoutPhoneItems.getChildCount();

        View currentItem;
        EditText etPhoneNumber;
        Spinner spnPhoneType;
        String phoneNumber;

        for (int i = 0; i < phoneNumbersInputed; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            etPhoneNumber = (EditText) currentItem.findViewById(R.id.et_phoneNumber);
            spnPhoneType = (Spinner) currentItem.findViewById(R.id.spn_phoneNumberType);
            phoneNumber = etPhoneNumber.getText().toString();

            if (!Validation.isPhoneValid(phoneNumber)) {
                isValid = false;

                // Scenario: User completes a valid number, reverts to an invalid number, and leaves
                // fragment. Returning to fragment will not display enabled 'NEXT' button.
                clearPhoneMap();

            } else if (!phoneNumbers.containsKey(phoneNumber)) {

                if (!checkForDuplicatePhoneNumberTypes()) {
                    phoneNumbers.put(phoneNumber, spnPhoneType.getSelectedItem().toString());
                } else {
                    clearPhoneMap();
                }
            }
        }

        return isValid;
    }*/
/*
    public boolean checkIfAllEmailFieldsAreValid() {
        boolean isValid = true;
        final int emailsInputed = layoutEmailItems.getChildCount();
        View currentItem;
        EditText etEmail;
        String email;

        for (int i = 0; i < emailsInputed; i++) {
            currentItem = layoutEmailItems.getChildAt(i);
            etEmail = (EditText) currentItem.findViewById(R.id.et_email);
            email = etEmail.getText().toString();

            if (!Validation.isEmailValid(email)) {
                isValid = false;

                if (emails.size() == emailsInputed) {
                    emails.remove(i);
                }

            } else {

                if (!emails.contains(email)) {
                    emails.add(email);
                }
            }
        }

        return isValid;
    }*/

    @Override
    public void saveCompletedFieldsInfo() {
        dataManager.setPhoneNumbers(getPhoneNumbersAndTypes());
        dataManager.setEmail(getEmail());
    }

    private Map<String, String> getPhoneNumbersAndTypes() {
        Map<String, String> phoneNumbersAndTypes = new HashMap<>();
        final int totalItems = layoutPhoneItems.getChildCount();
        View currentItem;
        ValidationImageView ivValidationIcon;
        EditText etPhoneNumber;
        Spinner spnPhoneNumber;

        for (int i = 0; i < totalItems; i++) {
            currentItem = layoutPhoneItems.getChildAt(i);
            ivValidationIcon = (ValidationImageView) currentItem.findViewById(R.id.iv_checkmarkValidation);

            if (ivValidationIcon.getValidation()) {
                etPhoneNumber = (EditText) currentItem.findViewById(R.id.et_phoneNumber);
                spnPhoneNumber = (Spinner) currentItem.findViewById(R.id.spn_phoneNumberType);

                phoneNumbersAndTypes.put(
                        spnPhoneNumber.getSelectedItem().toString(),
                        etPhoneNumber.getText().toString()
                );
            }
        }

        return phoneNumbersAndTypes;
    }

    private String getEmail() {
        if (layoutEmailItems.getChildCount() > 0){
            final View emailItem = layoutEmailItems.getChildAt(0);
            final EditText etEmail = (EditText) emailItem.findViewById(R.id.et_email);
            return etEmail.getText().toString();
        } else {
            return "";
        }




        /*String emails = new ArrayList<>();
        final int totalItems = layoutEmailItems.getChildCount();
        View currentItem;
        EditText etEmail;

        for (int i = 0; i < totalItems; i++) {
            currentItem = layoutEmailItems.getChildAt(i);
            etEmail = (EditText) currentItem.findViewById(R.id.et_email);

            if (checkEmailValid(etEmail.getText().toString())) {
                emails.add(etEmail.getText().toString());
            }

        }

        return emails;*/
    }

}

