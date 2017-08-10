package com.quickenloans.referexpress.interfaces;

/**
 * Surrounding fragments, in the ViewPager, are making callbacks AFTER
 * the viewing fragment makes its callbacks. This is causing the 'Next button'
 * to enable/disable incorrectly. Adding a 'requesting fragment' condition
 * to the callback has rectified this issue.
 */

public interface PageCompletionListener {
    void onPageCompletionChanged(int requestingFragment, boolean validated);
}
