package com.quickenloans.referexpress.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quickenloans.referexpress.adapters.ReferralFormAdapter;
import com.quickenloans.referexpress.helpers.ReferralInfoManager;
import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.helpers.ReferralInfoSender;
import com.quickenloans.referexpress.data.User;
import com.quickenloans.referexpress.data.UserDAO;
import com.quickenloans.referexpress.dialog.ListSelectionStateManager;
import com.quickenloans.referexpress.dialog.UserEditDialog;
import com.quickenloans.referexpress.interfaces.PageCompletionListener;
import com.quickenloans.referexpress.interfaces.DefaultUserChangedListener;
import com.quickenloans.referexpress.values.FragmentPositionValues;

import static com.quickenloans.referexpress.values.ClassNames.REFERRAL_PAGER_HOLDER_ACTIVITY;
import static com.quickenloans.referexpress.values.ClassNames.RETURN_ADDRESS;
import static com.quickenloans.referexpress.values.FragmentPositionValues.ADDITIONAL_FRAGMENT_5;
import static com.quickenloans.referexpress.values.FragmentPositionValues.FINISH_FRAGMENT_6;
import static com.quickenloans.referexpress.values.FragmentPositionValues.LOCATION_FRAGMENT_4;

/**
 * Created by jdeville on 6/5/17.
 */

public class ReferralViewPagerActivity extends AppCompatActivity implements PageCompletionListener, DefaultUserChangedListener {
    private ViewPager.PageTransformer transformer;
    private ReferralInfoManager dataManager;
    private NoSwipeViewPager mViewPager;
    private RelativeLayout rlayoutDots, rlayoutLoading;
    private ReferralFormAdapter mViewPagerAdapter;
    private Button btnBack, btnNext;
    private ImageView dot1, dot2, dot3, dot4, ivClose;
    private int dotSelectedId, dotUnselectedId;

    private DialogInterface.OnClickListener discardDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dataManager.clearData();
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        discardForm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_referral_pager_holder);

        initFields();

        initControls();

        setupControls();
    }

    private void initFields() {

        if (null == dataManager) {
            dataManager = ReferralInfoManager.getInstance();
        }
        transformer = new ReferralFormPageTransformer();
        dotSelectedId = getResources().getIdentifier("dot_selected", "drawable", getPackageName());
        dotUnselectedId = getResources().getIdentifier("dot_unselected", "drawable", getPackageName());
    }

    private void initControls() {

        dot1 = (ImageView) findViewById(R.id.iv_dot1);
        dot2 = (ImageView) findViewById(R.id.iv_dot2);
        dot3 = (ImageView) findViewById(R.id.iv_dot3);
        dot4 = (ImageView) findViewById(R.id.iv_dot4);

        rlayoutDots = (RelativeLayout) findViewById(R.id.rlayout_pagerDots);
        rlayoutLoading = (RelativeLayout) findViewById(R.id.rlayout_loading);

        mViewPager = (NoSwipeViewPager) findViewById(R.id.viewPager);
        mViewPager.setPagingEnabled(false);

        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);
        ivClose = (ImageView) findViewById(R.id.iv_close);

        //progressBar = (ProgressBar) findViewById(R.id.progress);
        //loadingBackground = (View) findViewById(R.id.background);
    }

    private void setupControls() {

        mViewPagerAdapter = new ReferralFormAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // Check if no view has focus:
                View view = ReferralViewPagerActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                updateDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //mViewPager.setPageTransformer(false, transformer);


        // Setup 'back' button listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChildFragmentData();
                updateButtonFunctionBack();
                updateNavigationButtons();
            }
        });

        // Setup 'forward' button listener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChildFragmentData();
                updateButtonFunctionNext();
                updateNavigationButtons();
            }
        });

        final Activity activity = this;

        // Setup 'exit' button listener
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardForm();
            }
        });

        updateNavButtonsForStart();
    }

    private void updateDots(int position) {

        switch (position) {
            case (1):
                if (rlayoutDots.getVisibility() == View.INVISIBLE) {
                    rlayoutDots.setVisibility(View.VISIBLE);
                } else {
                    rlayoutDots.setVisibility(View.VISIBLE);

                    dot1.setImageResource(dotSelectedId);
                    dot2.setImageResource(dotUnselectedId);
                    dot3.setImageResource(dotUnselectedId);
                    dot4.setImageResource(dotUnselectedId);
                }
                break;

            case (2):
                rlayoutDots.setVisibility(View.VISIBLE);
                dot1.setImageResource(dotUnselectedId);
                dot2.setImageResource(dotSelectedId);
                dot3.setImageResource(dotUnselectedId);
                dot4.setImageResource(dotUnselectedId);
                break;

            case (3):
                rlayoutDots.setVisibility(View.VISIBLE);
                dot1.setImageResource(dotUnselectedId);
                dot2.setImageResource(dotUnselectedId);
                dot3.setImageResource(dotSelectedId);
                dot4.setImageResource(dotUnselectedId);
                break;
            case (4):
                rlayoutDots.setVisibility(View.VISIBLE);
                dot1.setImageResource(dotUnselectedId);
                dot2.setImageResource(dotUnselectedId);
                dot3.setImageResource(dotUnselectedId);
                dot4.setImageResource(dotSelectedId);
                break;
            default:
                rlayoutDots.setVisibility(View.INVISIBLE);
        }
    }

    private void discardForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReferralViewPagerActivity.this);
        builder.setTitle("Discard referral?")
                .setMessage("Changes will be lost and you will return to the main screen.")
                .setPositiveButton("Discard", discardDialogListener)
                .setNegativeButton("Cancel", discardDialogListener)
                .show();
    }

    private void updateButtonFunctionBack() {
        navigateViewPager(-1);
    }

    private void updateButtonFunctionNext() {
        if (mViewPager.getCurrentItem() == FINISH_FRAGMENT_6) {
            dataManager.clearData();
            finish();
        } else if (mViewPager.getCurrentItem() == ADDITIONAL_FRAGMENT_5) {
            sendReferral();

        } else {
            navigateViewPager(1);
        }
    }

    private void navigateViewPager(int navigationDirection) {
        mViewPager.setCurrentItem
                (mViewPager.getCurrentItem() + navigationDirection);
    }

    private void updateNavigationButtons() {
        final int currentFragment = mViewPager.getCurrentItem();

        switch (currentFragment) {
            case FragmentPositionValues.START_FRAGMENT_1:
                updateNavButtonsForStart();
                break;
            case FragmentPositionValues.NAME_FRAGMENT_2:
                updateNavButtonsForName();
                break;
            case FragmentPositionValues.CONTACT_FRAGMENT_3:
                updateNavButtonsForContact();
                break;
            case LOCATION_FRAGMENT_4:
                updateNavButtonsForLocation();
                break;
            case ADDITIONAL_FRAGMENT_5:
                updateNavButtonsForAdditional();
                break;
            case FINISH_FRAGMENT_6:
                updateNavButtonsForFinish();
                break;
            default:
                break;
        }
    }

    private void updateNavButtonsForStart() {
        // 'back' button logic
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setEnabled(false);

        // 'next' button logic
        btnNext.setText(R.string.start_fragment_next);
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setEnabled(true);

        // 'exit' button logic
        ivClose.setVisibility(View.VISIBLE);

    }

    private void updateNavButtonsForName() {
        // 'back' button logic
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setEnabled(false);

        // 'next' button logic
        btnNext.setText(R.string.normal_next);
        btnNext.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.VISIBLE);

    }

    private void updateNavButtonsForContact() {
        // 'back' button logic
        btnBack.setText(R.string.normal_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setEnabled(true);

        // 'next' button logic
        btnNext.setText(R.string.normal_next);
        btnNext.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.VISIBLE);

    }

    private void updateNavButtonsForLocation() {
        // 'back' button logic
        btnBack.setText(R.string.normal_back);
        btnBack.setVisibility(View.VISIBLE);

        // 'next' button logic
        btnNext.setText(R.string.normal_next);
        btnNext.setVisibility(View.VISIBLE);

        ivClose.setVisibility(View.VISIBLE);
    }

    private void updateNavButtonsForAdditional() {
        // 'back' button logic
        btnBack.setText(R.string.normal_back);
        btnBack.setVisibility(View.VISIBLE);

        // 'next' button logic
        btnNext.setText(R.string.finish_next);
        btnNext.setVisibility(View.VISIBLE);

        ivClose.setVisibility(View.VISIBLE);
    }

    private void updateNavButtonsForFinish() {
        // 'back' button logic
        btnBack.setText(R.string.normal_back);
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setEnabled(false);

        // 'next' button logic
        btnNext.setText(R.string.exit_next);
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setEnabled(true);

        // 'exit' button logic
        ivClose.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onPageCompletionChanged(int requestingFragment, boolean validated) {

        // Updating the nextButton can only be done by the visible fragment
        if (mViewPager.getCurrentItem() == requestingFragment) {
            btnNext.setEnabled(validated);
        }
    }

    private void saveChildFragmentData() {
        mViewPagerAdapter.saveCurrentFragmentData();
    }

    private void sendReferral() {
        enableNavigationButtons(false);
        startLoadingSequence();
        final ReferralInfoSender sender = new ReferralInfoSender(this, rlayoutLoading);
        sender.sendReferral();
    }

    private void enableNavigationButtons(boolean enable) {
        btnNext.setEnabled(enable);
        btnBack.setEnabled(enable);
    }

    public void referralSuccessful(String response) {
        rlayoutLoading.clearAnimation();
        rlayoutLoading.setVisibility(View.GONE);
        updateNavButtonsForFinish();
        navigateViewPager(1);
    }

    public void referralFailure(String response) {
        final UserDAO userDAO = new UserDAO(this);
        final User user = userDAO.getDefaultUser();
        final AlertDialog dialog = new AlertDialog.Builder(ReferralViewPagerActivity.this)
                .setTitle("Team member not found")
                .setMessage("We couldn't verify the team member's email \"" + user.getEmail() + "\""
                        + "\nWould you like to edit user information?")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final ListSelectionStateManager mStateManager = ListSelectionStateManager.getInstance();
                        mStateManager.setSelectedItemId(user.getId());

                        DialogFragment frag = new UserEditDialog();
                        frag.setArguments(bundleReturnAddress());
                        frag.show(getSupportFragmentManager(), "UserEditDialog");
                    }
                })
                .setNegativeButton("Back", null)
                .show();
        enableNavigationButtons(true);
        //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        endLoadingSequence();
        // dialog popup failure
    }

    public void referralNetworkFailure(String error) {
        final UserDAO userDAO = new UserDAO(this);
        final User user = userDAO.getDefaultUser();
        final AlertDialog dialog = new AlertDialog.Builder(ReferralViewPagerActivity.this)
                .setTitle("Network error")
                .setMessage("Unable to connect to the server. Verify internet connectivity is enabled and try again.")
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sendReferral();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        enableNavigationButtons(true);
        endLoadingSequence();
        //Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void startLoadingSequence() {
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.your_fade_in_anim);
        anim.setFillAfter(true);
        rlayoutLoading.setVisibility(View.VISIBLE);
        rlayoutLoading.bringToFront();
        rlayoutLoading.startAnimation(anim);
    }

    private void endLoadingSequence() {
        rlayoutLoading.clearAnimation();
        rlayoutLoading.setVisibility(View.GONE);
    }

    private Bundle bundleReturnAddress() {
        final Bundle args = new Bundle();
        args.putString(RETURN_ADDRESS, REFERRAL_PAGER_HOLDER_ACTIVITY);
        return args;
    }

    @Override
    public void onUserChanged() {
        // nothing dialog interface
    }
}
