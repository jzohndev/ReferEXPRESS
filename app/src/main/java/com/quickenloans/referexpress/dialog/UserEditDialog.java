package com.quickenloans.referexpress.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.data.User;
import com.quickenloans.referexpress.data.UserDAO;

import static com.quickenloans.referexpress.values.ClassNames.RETURN_ADDRESS;
import static com.quickenloans.referexpress.values.ClassNames.USER_EDIT_DIALOG;

/**
 * Created by jdeville on 7/3/17.
 */

public class UserEditDialog extends UserCreationDialog {
    private ListSelectionStateManager mStateManager;
    private UserDAO userDAO;
    private User user;
    private int userId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    protected void initFields() {
        super.initFields();
        if (null == mStateManager){
            mStateManager = ListSelectionStateManager.getInstance();
        }

        userDAO = new UserDAO(getActivity());
        userId = (int) mStateManager.getSelectedItemId();

        user = userDAO.select(userId);
    }

    @Override
    protected void initControls() {
        super.initControls();

        btnFinish.setEnabled(true);

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        final TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_content);
        tvTitle.setText(R.string.edit_user);
    }

    @Override
    protected void completeDialog() {
        userDAO.update(userId, new User(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etEmail.getText().toString()
        ));
        exitDialog();
    }

    @Override
    protected void exitDialog() {
        // todo return to previous dialog
        super.exitDialog();
    }

    @Override
    protected String getReturnAddress() {
        return super.getReturnAddress();
    }

    @Override
    protected Bundle bundleReturnAddress() {
        final Bundle args = new Bundle();
        args.putString(RETURN_ADDRESS, USER_EDIT_DIALOG);
        return args;
    }
}
