package com.quickenloans.referexpress.helpers;

import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quickenloans.referexpress.activities.ReferralViewPagerActivity;
import com.quickenloans.referexpress.data.ReferralTypes;
import com.quickenloans.referexpress.data.UserDAO;
import com.quickenloans.referexpress.misc.old.Environment;
import com.quickenloans.referexpress.misc.old.LeadSubmissionFields;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdeville on 7/17/17.
 */

public class ReferralInfoSender {
    private ReferralViewPagerActivity activity;
    private RelativeLayout progressLayout;
    private ReferralInfoManager dataManager;
    private RequestQueue queue;
    private StringRequest request;
    private Response.Listener<String> referralResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            progressLayout.setVisibility(View.GONE);

            if (response.contains("SUCCESS")) {
                activity.referralSuccessful(response);

            } else {
                activity.referralFailure(response);
            }
        }
    };
    private Response.ErrorListener referralErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            activity.referralNetworkFailure(error.toString());
        }
    };

    public ReferralInfoSender(ReferralViewPagerActivity activity, RelativeLayout progressLayout) {
        this.activity = activity;
        this.progressLayout = progressLayout;
        this.dataManager = ReferralInfoManager.getInstance();
        this.queue = Volley.newRequestQueue(activity);
        this.request = getStringRequest();
    }

    public void sendReferral() {
        request.setRetryPolicy(getRetryPolicy());
        queue.add(request);
    }

    private StringRequest getStringRequest() {
        return new StringRequest(
                Request.Method.POST,
                Environment.getSubmissionPostUrl(),
                referralResponseListener,
                referralErrorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getReferralParams();
            }
        };
    }

    private DefaultRetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(
                20000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    private Map<String, String> getReferralParams() {
        Map<String, String> params = new HashMap<>();


        params.put(LeadSubmissionFields.LEAD_TYPE, "DCXPANDROID");
        params.put(LeadSubmissionFields.LOAN, dataManager.getLoanType());
        params.put(LeadSubmissionFields.SESSION_ID, "123456");
        params.put(LeadSubmissionFields.LEAD_SYSTEM, "QLWEB");
        params.put(LeadSubmissionFields.FIRST_NAME, dataManager.getFirstName());
        params.put(LeadSubmissionFields.LAST_NAME, dataManager.getLastName());
        params.put(LeadSubmissionFields.STATE, dataManager.getLocation());

        if (dataManager.getReferralType().contains(ReferralTypes.REVERSE_MORTGAGE)) {
            params.put(LeadSubmissionFields.COMPANY_ID, "9");
        } else {
            // maybe todo if needed for forward mortgage
        }


        if (null != dataManager.getWorkPhoneArray()) {
            final String[] workPhoneArray = dataManager.getWorkPhoneArray();
            params.put(LeadSubmissionFields.DAY_AREA_PHONE, workPhoneArray[0]);
            params.put(LeadSubmissionFields.DAY_PREFIX_PHONE, workPhoneArray[1]);
            params.put(LeadSubmissionFields.DAY_LAST_4_PHONE, workPhoneArray[2]);
        }

        if (null != dataManager.getHomePhoneArray()) {
            final String[] homePhoneArray = dataManager.getHomePhoneArray();
            params.put(LeadSubmissionFields.EVE_AREA_PHONE, homePhoneArray[0]);
            params.put(LeadSubmissionFields.EVE_PREFIX_PHONE, homePhoneArray[1]);
            params.put(LeadSubmissionFields.EVE_LAST_4_PHONE, homePhoneArray[2]);
        }

        if (exists(dataManager.getCellPhoneNumber())) {
            params.put(LeadSubmissionFields.CELL_PHONE, dataManager.getCellPhoneNumber());
        }

        if (exists(dataManager.getNote())) {
            if (exists(dataManager.getBanker())) {
                final String noteWithBanker = dataManager.getNote()
                        + "\n\nI would like to be contacted by "
                        + dataManager.getBanker() + " (Banker) concerning my referral";
                params.put(LeadSubmissionFields.COMMENTS, noteWithBanker);
            } else {
                params.put(LeadSubmissionFields.COMMENTS, dataManager.getNote());
            }
        }


        if (exists(dataManager.getEmail())) {
            params.put(LeadSubmissionFields.EMAIL, dataManager.getEmail());
        }

        // Team Member
        params.put(LeadSubmissionFields.REFER_EMAIL_ADDRESS, new UserDAO(activity).getDefaultUser().getEmail());


        return params;
    }

    private boolean exists(String param) {
        return (null != param) && !(param.isEmpty());
    }
}
