package uk.co.chrisjenx.paralloid;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import uk.co.chrisjenx.paralloid.graphics.ParallaxDrawable;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public final class ParallaxHelper {

    public static void scrollViewBy(final View view, final int x, final int y, final float factor) {
        if (view == null) return;
        view.scrollTo((int) (x * factor), (int) (y * factor));
    }

    public static void scrollBackgroundBy(final ParallaxDrawable drawable, final int scrollX, final int scrollY, final float factor) {
        if (drawable == null) return;
        drawable.setScrollTo(scrollX * factor, scrollY * factor);
    }


    public static ParallaxDrawable setParallaxBackground(final View view, final Drawable drawableBackground, float multiplier) {
        if (view == null || drawableBackground == null) return null;
        final ParallaxDrawable parallaxDrawable = new ParallaxDrawable(drawableBackground, multiplier);
        // We request the size before attaching just incase the view has drawn we can prepopulate the drawable with the extra height/width
        requestScrollableWidthHeight(view, multiplier, new ParallaxHelper.ScrollableWidthHeightCallback() {
            @Override
            public void onScrollableWidthHeight(final float width, final float height) {
                // This is called back when the view has (hopefully) the correct width/height
                parallaxDrawable.setParallaxExtraWidthHeight(width, height);
            }
        });
        view.setBackgroundDrawable(parallaxDrawable);
        return parallaxDrawable;
    }

    static void requestScrollableWidthHeight(final View view, final float multiplier, final ScrollableWidthHeightCallback callback) {
        if (callback == null) return;
        // Have we done a layout pass?
        if (view.getHeight() > 0 || view.getWidth() > 0) {
            final float[] xy = calculateScrollableWidthHeightFromView(view, multiplier);
            callback.onScrollableWidthHeight(xy[0], xy[1]);
        } else {
            // Call back to self when we have laid out
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    requestScrollableWidthHeight(view, multiplier, callback);
                    return true;
                }
            });
        }
    }

    static float[] calculateScrollableWidthHeightFromView(View view, float factor) {
        if (view == null) return new float[2];
        if (view instanceof ScrollView) {
            // Get the ScrollView's only child
            final View child = ((ScrollView) view).getChildAt(0);

            return new float[]{view.getWidth(), calculateExtraScroll(view.getHeight(), child.getHeight(), factor)};
        }
        if (view instanceof HorizontalScrollView) {
            // Get the ScrollView's only child
            final View child = ((HorizontalScrollView) view).getChildAt(0);
            return new float[]{calculateExtraScroll(view.getWidth(), child.getWidth(), factor), view.getHeight()};
        }
        if (view instanceof ListView) {
            //TODO
        }
        // Not sure what it is? Just use the width/height
        return new float[]{view.getWidth(), view.getHeight()};
    }

    static float calculateExtraScroll(float parent, float child, float factor) {
        return parent + (child - parent) * factor;
    }

    static interface ScrollableWidthHeightCallback {
        void onScrollableWidthHeight(float width, float height);
    }

}
