package uk.co.chrisjenx.paralloid;

import android.view.View;

/**
 * OnScrolled changed Listener for {@link android.view.View} and this alike to implement
 */
public interface OnScrollChangedListener {
    void onScrollChanged(View who, int l, int t, int oldl, int oldt);
}