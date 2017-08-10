package com.quickenloans.referexpress.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.interfaces.DefaultUserChangedListener;
import com.quickenloans.referexpress.data.User;
import com.quickenloans.referexpress.data.UserDAO;

import java.util.List;

import static com.quickenloans.referexpress.dialog.ListSelectionStateManager.NO_USER_SELECTED;
import static com.quickenloans.referexpress.dialog.ListSelectionStateManager.USER_SELECTED;
import static com.quickenloans.referexpress.values.ClassNames.RETURN_ADDRESS;
import static com.quickenloans.referexpress.values.ClassNames.USER_SELECTION_DIALOG;

/**
 * Created by jdeville on 6/30/17.
 */

public class UserSelectionActivity extends AppCompatActivity implements DefaultUserChangedListener {
    private UserDAO mUserDAO;

    private ListView listViewUsers;
    private ImageView btnExit;
    private Button btnSet, btnOk, btnNeutral, btnNegative;

    private DialogListAdapter mListAdapter;
    private ListSelectionStateManager mStateManager;
    private List<User> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        initActivity();

    }

    private void initActivity() {
        initFields();
        initControls();
        setupControls();
        initListeners();
    }

    private void initFields() {

        if (null == mStateManager) {
            mStateManager = ListSelectionStateManager.getInstance();
        }

        mUserDAO = new UserDAO(this);
        userList = mUserDAO.selectAll();
        mListAdapter = new DialogListAdapter(this, userList);

    }

    private void initControls() {

        listViewUsers = (ListView) findViewById(R.id.lv_users);


        btnExit = (ImageView) findViewById(R.id.btn_cancel);
        btnSet = (Button) findViewById(R.id.btn_set);


        btnNeutral = (Button) findViewById(R.id.btn_neutral);
        btnNegative = (Button) findViewById(R.id.btn_negative);
        btnOk = (Button) findViewById(R.id.btn_ok);

        updateButtons();
    }

    private void setupControls() {

        listViewUsers.setAdapter(mListAdapter);
    }

    private void initListeners() {

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                /*final Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtras(bundleReturnAddress());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));*/
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUserDAO.updateDefaultUser((int) mStateManager.getSelectedItemId());
                mListAdapter.dataSetChanged();
                updateButtons();

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnNeutral.setOnClickListener(neutralButtonListener);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserDAO.selectAll().size() == 1) {
                    AlertDialog cantdothat = new AlertDialog.Builder(UserSelectionActivity.this).setMessage("At least one user must exist")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    TextView textView = (TextView) cantdothat.findViewById(android.R.id.message);
                    textView.setTextColor(getResources().getColor(R.color.black_secondary));


                } else {

                    mUserDAO.delete(mStateManager.getSelectedItemId());

                    if (mStateManager.getSelectedItemId() == mUserDAO.getDefaultUserId()) {
                        mUserDAO.lottoNewDefaultUser();
                    }
                    clearSelection();
                    mListAdapter.dataSetChanged();
                    updateButtons();

                }
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mStateManager.getSelectedItemId() == id) {
                    clearSelection();

                } else {
                    mStateManager.setSelectedItemId(id);
                    mStateManager.updateDialogState(USER_SELECTED);
                }

                updateButtons();
            }
        });
    }

    private void updateButtons() {

        switch (mStateManager.getDialogState()) {
            case (NO_USER_SELECTED):
                btnSet.setEnabled(false);

                btnNeutral.setText("Add User...");

                btnNegative.setVisibility(View.INVISIBLE);
                break;

            case (USER_SELECTED):

                btnNeutral.setText("Edit");

                btnNegative.setVisibility(View.VISIBLE);

                if (mStateManager.getSelectedItemId() == mUserDAO.getDefaultUserId()) {
                    btnSet.setEnabled(false);
                } else {
                    btnSet.setEnabled(true);
                }
                break;
        }
    }

    private View.OnClickListener neutralButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mStateManager.getDialogState()) {
                case (NO_USER_SELECTED):
                    DialogFragment createDialog = new UserCreationDialog();
                    createDialog.setArguments(bundleReturnAddress());
                    createDialog.show(getSupportFragmentManager(), "UserEditDialog");
                    break;

                case (USER_SELECTED): // EDIT
                    DialogFragment dialog = new UserEditDialog();
                    dialog.setArguments(bundleReturnAddress());
                    dialog.show(getSupportFragmentManager(), "UserEditDialog");
                    break;
            }
            onUserChanged();
        }
    };

    private Bundle bundleReturnAddress() {
        final Bundle args = new Bundle();
        args.putString(RETURN_ADDRESS, USER_SELECTION_DIALOG);
        return args;
    }

    private void clearSelection() {
        mStateManager.setSelectedItemId(-1);
        mStateManager.updateDialogState(NO_USER_SELECTED);
        // Removes selection. Only solution that seems to work.
        listViewUsers.setAdapter(mListAdapter);
    }

    @Override
    public void onUserChanged() {

        mListAdapter.dataSetChanged();
        updateButtons();
    }

    @Override
    public void finish() {
        mStateManager.setSelectedItemId(-1);
        mStateManager.updateDialogState(NO_USER_SELECTED);
        super.finish();
    }
}
