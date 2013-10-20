package uk.co.chrisjenx.paralloid;

import android.view.View;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public final class ParallaxHelper {

    public static final void scrollViewBy(final View view, final int x, final int y, final float factor) {
        if(view == null) return;
        view.scrollTo((int)(x * factor),(int)(y * factor));
    }

}
