package com.quickenloans.referexpress.activities;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickenloans.referexpress.anims.ResizeAnimation;
import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.helpers.ReferralInfoManager;
import com.quickenloans.referexpress.values.LoanTypes;
import com.quickenloans.referexpress.data.ReferralTypes;
import com.quickenloans.referexpress.data.UserDAO;
import com.quickenloans.referexpress.dialog.UserCreationDialog;
import com.quickenloans.referexpress.dialog.UserSelectionActivity;
import com.quickenloans.referexpress.interfaces.DefaultUserChangedListener;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.quickenloans.referexpress.values.ClassNames.MAIN_ACTIVITY;
import static com.quickenloans.referexpress.values.ClassNames.RETURN_ADDRESS;

/**
 * Created by jdeville on 6/5/17.
 */

public class MainActivity extends AppCompatActivity implements DefaultUserChangedListener {
    private static final String NEW_USER_TEXT = "Setup new user...";
    private static final int TOP_VIEW_DP_MODIFIER = 96;
    private static final int BOTTOM_VIEW_DP_MODIFIER = 40;
    private static final int EXPAND_CONTRACT_ANIM_DURATION = 300;

    @BindView(R.id.tv_referText)
    TextView tvReferField;
    @BindView(R.id.tv_defaultText)
    TextView tvDefaultField;
    @BindView(R.id.tv_defaultUser)
    TextView tvDefaultUser;
    @BindView(R.id.rlayout_user)
    RelativeLayout rlayoutUserSelection;
    @BindView(R.id.include)
    RelativeLayout rlayoutOptions;
    @BindView(R.id.btn_forward)
    Button btnForward;
    @BindView(R.id.btn_reverse)
    Button btnReverse;
    @BindView(R.id.btn_refinance)
    Button btnRefinance;
    @BindView(R.id.btn_purchase)
    Button btnPurchase;
    @BindView(R.id.view_topCenterTriangle)
    View dividerTriangleTop;
    @BindView(R.id.view_bottomCenterTriangle)
    View dividerTriangleBottom;
    @BindView(R.id.rlayout_bottomButtons)
    RelativeLayout rlayoutBottom;

    private ReferralInfoManager dataManager;
    private UserDAO mUserDAO;
    private int topViewHeight, bottomViewHeight;

    @BindString(R.string.main_text_field_default)
    String mainDefaultText;


    @OnClick(R.id.btn_forward)
    public void onForwardClick() {
        if (btnForward.isSelected()) {

            btnForward.setSelected(false);
            btnPurchase.setEnabled(false);
            btnRefinance.setEnabled(false);
            dividerTriangleTop.setVisibility(View.INVISIBLE);
            dividerTriangleBottom.setVisibility(View.VISIBLE);
            expandOptionButtonsLayout(false);

        } else {
            if (!btnReverse.isSelected()) {
                expandOptionButtonsLayout(true);
            }

            btnForward.setSelected(true);
            btnReverse.setSelected(false);
            btnPurchase.setEnabled(true);
            btnRefinance.setEnabled(true);
            dividerTriangleTop.setVisibility(View.VISIBLE);
            dividerTriangleBottom.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_reverse)
    public void onReverseClick() {
        if (btnReverse.isSelected()) {
            btnReverse.setSelected(false);
            btnPurchase.setEnabled(false);
            btnRefinance.setEnabled(false);
            dividerTriangleTop.setVisibility(View.INVISIBLE);
            dividerTriangleBottom.setVisibility(View.VISIBLE);
            expandOptionButtonsLayout(false);
        } else {
            if (!btnForward.isSelected()) {
                expandOptionButtonsLayout(true);
            }
            btnForward.setSelected(false);
            btnReverse.setSelected(true);
            btnPurchase.setEnabled(true);
            btnRefinance.setEnabled(true);
            dividerTriangleTop.setVisibility(View.VISIBLE);
            dividerTriangleBottom.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.btn_purchase, R.id.btn_refinance})
    public void onOptionsClick(Button b) {
        if (btnForward.isSelected()) {
            dataManager.setReferralType(ReferralTypes.FORWARD_MORTGAGE);
        } else if (btnReverse.isSelected()) {
            dataManager.setReferralType(ReferralTypes.REVERSE_MORTGAGE);
        } else {
            dataManager.setReferralType(ReferralTypes.UNSPECIFIED);
        }

        switch (b.getId()) {
            case R.id.btn_purchase:
                dataManager.setLoanType(LoanTypes.PURCHASE);
                break;
            case R.id.btn_refinance:
                dataManager.setLoanType(LoanTypes.REFINANCE);
                break;
            default:
                dataManager.setLoanType(LoanTypes.UNSPECIFIED); //todo validation fail
        }

        startActivity(new Intent(getApplicationContext(), ReferralViewPagerActivity.class));
    }

    @OnClick(R.id.rlayout_user)
    public void onOptionButtonClick(View v) {
        if (userExists()) {
            startUserSelectionActivity();
        } else {
            showUserCreationDialog();
        }
    }

    @Override
    public void onResume() {
        initActivityView();
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        ButterKnife.bind(this);

        initApp();
    }

    private void initApp() {
        initFields();
        initActivityView();
    }

    private void initFields() {
        if (null == dataManager) {
            dataManager = ReferralInfoManager.getInstance();
        }
        mUserDAO = new UserDAO(this);
        final int screenDensity = (int) Resources.getSystem().getDisplayMetrics().density;
        topViewHeight = TOP_VIEW_DP_MODIFIER * screenDensity;
        bottomViewHeight = BOTTOM_VIEW_DP_MODIFIER * screenDensity;
    }

    private void initActivityView() {
        if (userExists()) {
            setDefaultActivityView();
        } else {
            setNewUserActivityView();
        }
    }

    private void expandOptionButtonsLayout(boolean doExpand) {
        Animation anim;
        int targetHeight = topViewHeight;
        int startHeight = bottomViewHeight;

        if (!doExpand) {
            targetHeight = bottomViewHeight;
            startHeight = topViewHeight;
        }

        anim = new ResizeAnimation(rlayoutBottom, targetHeight, startHeight);
        anim.setDuration(EXPAND_CONTRACT_ANIM_DURATION);
        rlayoutBottom.startAnimation(anim);
    }

    private boolean userExists() {
        return (mUserDAO.getDefaultUserId() != -1);
    }

    private void setDefaultActivityView() {
        tvDefaultUser.setText(mUserDAO.getDefaultUser().getFullName());
        rlayoutOptions.setVisibility(View.VISIBLE);
        tvReferField.setVisibility(View.VISIBLE);
        tvDefaultField.setVisibility(View.GONE);
    }

    private void setNewUserActivityView() {
        setNewUserControls();
    }

    private void setNewUserControls() {
        tvDefaultUser.setText(NEW_USER_TEXT);
        rlayoutOptions.setVisibility(View.INVISIBLE);
        tvReferField.setVisibility(View.GONE);
        tvDefaultField.setVisibility(View.VISIBLE);
    }

    private void showUserCreationDialog() {
        final DialogFragment dialog = new UserCreationDialog();
        dialog.setArguments(bundleReturnAddress());
        dialog.show(getSupportFragmentManager(), "UserCreationDialog");
    }

    private void startUserSelectionActivity() {
        Intent i = new Intent(getApplicationContext(), UserSelectionActivity.class);
        i.putExtras(bundleReturnAddress());
        startActivity(i);
    }

    private Bundle bundleReturnAddress() {
        final Bundle args = new Bundle();
        args.putString(RETURN_ADDRESS, MAIN_ACTIVITY);
        return args;
    }

    @Override
    public void onUserChanged() {
        initActivityView();
    }
}

