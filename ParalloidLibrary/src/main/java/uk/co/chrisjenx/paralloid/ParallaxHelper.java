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
        if(view == null) return;
        view.scrollTo((int)(x * factor),(int)(y * factor));
    }

    public static void scrollBackgroundBy(final ParallaxDrawable drawable, final int scrollX, final int scrollY, final float factor) {
        if(drawable == null) return;
        drawable.setScrollTo(scrollX * factor, scrollY * factor);
    }


    public static ParallaxDrawable setParallaxBackground(final View view, final Drawable drawableBackground) {
        if(view == null || drawableBackground == null) return null;
        ParallaxDrawable parallaxDrawable = new ParallaxDrawable(drawableBackground);
        view.setBackgroundDrawable(parallaxDrawable);
        return parallaxDrawable;
    }

    public static void requestScrollableWidthHeight(final View view, final ScrollableWidthHeightCallback callback) {
        if(callback == null) return;
        // Have we done a layout pass?
        if (view.getHeight() > 0 || view.getWidth() > 0) {
            final int[] xy = getScrollableWidthHeightFromView(view);
            callback.onScrollableWidthHeight(xy[0],xy[1]);
        } else {
            // Call back to self when we have laid out
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    requestScrollableWidthHeight(view, callback);
                    return true;
                }
            });
        }
    }

    private static int[] getScrollableWidthHeightFromView(View view) {
        if (view == null) return new int[2];
        if (view instanceof ScrollView) {
            // Get the ScrollView's only child
            final View child = ((ScrollView) view).getChildAt(0);
            return new int[]{view.getWidth(), child.getHeight()};
        }
        if (view instanceof HorizontalScrollView) {
            // Get the ScrollView's only child
            final View child = ((HorizontalScrollView) view).getChildAt(0);
            return new int[]{child.getWidth(), view.getHeight()};
        }
        if (view instanceof ListView) {
            //TODO
        }
        // Not sure what it is? Just use the width/height
        return new int[]{view.getWidth(), view.getHeight()};
    }

    public static interface ScrollableWidthHeightCallback{
        void onScrollableWidthHeight(int width, int height);
    }

}
