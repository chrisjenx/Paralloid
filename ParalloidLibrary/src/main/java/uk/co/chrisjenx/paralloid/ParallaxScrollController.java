package uk.co.chrisjenx.paralloid;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import uk.co.chrisjenx.paralloid.graphics.ParallaxDrawable;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxScrollController<T extends View & Parallaxor> implements Parallaxor {

    public static String TAG = ParallaxScrollController.class.getSimpleName();

    public static <T extends View & Parallaxor> ParallaxScrollController wrap(T wrappedView) {
        return new ParallaxScrollController<T>(wrappedView);
    }

    /**
     * The wrapped view.
     */
    final T mWrappedView;
    /**
     * Observer used to listen to the mWrappedView scroll changes.
     * (This is done to stop making {@code onScrollChanged()} public to the user.
     */
    final ScrollControllerOnScrollObserver mScrollObserver = new ScrollControllerOnScrollObserver(this);

    /**
     * HashMap which contains the parallaxed views.
     */
    private WeakHashMap<View, Float> mViewHashMap;
    /**
     * The background of the wrapped View to Parallax
     */
    private ParallaxDrawable mWrappedParallaxBackground;
    /**
     * BackgroundScaleFactor
     */
    private float mParallaxBackgroundMultiplier;
    /**
     * The Optional Scroll Changed Listener for the user to listen to scroll events.
     */
    private OnScrollChangedListener mScrollChangedListener;

    private int mLastScrollX = 0;
    private int mLastScrollY = 0;

    private ParallaxScrollController(T wrappedView) {
        mWrappedView = wrappedView;
        init();
    }

    public T getWrappedView() {
        return mWrappedView;
    }

    /**
     * Init this controller
     */
    private void init() {
        if (mWrappedView == null)
            throw new IllegalArgumentException("The wrapped view cannot be null");

        final ViewTreeObserver observer = mWrappedView.getViewTreeObserver();
        if (observer != null) {
            observer.addOnScrollChangedListener(mScrollObserver);
        }
    }

    /**
     * Add a view to be parallax'd by. If already set this will replace the current factor.
     *
     * @param view
     * @param factor
     */
    public void parallaxViewBy(View view, float factor) {
        if (view == null) return;
        if (view == mWrappedView)
            throw new IllegalArgumentException("You can't parallax the Parallaxor, this would end badly, Parallax other views");
        if (mViewHashMap == null)
            mViewHashMap = new WeakHashMap<View, Float>();

        mViewHashMap.put(view, Float.valueOf(factor));
        onScrollChanged(false);
    }

    @Override
    public void parallaxBackgroundBy(final Drawable drawable, final float multiplier) {
        mWrappedParallaxBackground = ParallaxHelper.setParallaxBackground(mWrappedView, drawable);
        mParallaxBackgroundMultiplier = multiplier;
        if(mWrappedParallaxBackground == null) return;
        ParallaxHelper.requestScrollableWidthHeight(mWrappedView,new ParallaxHelper.ScrollableWidthHeightCallback() {
            @Override
            public void onScrollableWidthHeight(int width, int height) {
                // This is called back when the view has (hopefully) the correct width/height
                mWrappedParallaxBackground.setParallaxBounds(width, height);
            }
        });
    }

    /**
     * Feel free to implement {@link uk.co.chrisjenx.paralloid.OnScrollChangedListener} to get call
     * backs to the wrapped view for scroll changed events.
     *
     * <b>Note</b>: this will get called, AFTER any parallax modification.
     *
     * @param onScrollChangedListener Null is valid (it will remove it if set).
     */
    @Override
    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        mScrollChangedListener = onScrollChangedListener;
    }

    /**
     * Something has changed.
     *
     * @param force force call through to updating the listening views,
     *              default to false to not force scrolling.
     */
    private void onScrollChanged(boolean force) {
        final int offsetX = mWrappedView.getScrollX();
        final int offsetY = mWrappedView.getScrollY();
        if (offsetX != mLastScrollX || offsetY != mLastScrollY || force) {
            doScrollChanged(offsetX, offsetY, mLastScrollX, mLastScrollY);
            mLastScrollX = offsetX;
            mLastScrollY = offsetY;
        }
    }

    // --
    // doScrollChanged Pointers to keep memory consumption down during fast scrolling
    //
    private Set<Map.Entry<View, Float>> entriesPointer;
    private Iterator<Map.Entry<View, Float>> iteratorPointer;
    private Map.Entry<View, Float> entryPointer;
    private View viewPointer;
    // --

    /**
     * Will do the scroll changed stuff.
     *
     * @param x    currentX of Parallaxor View
     * @param y    currentX of Parallaxor View
     * @param oldX Previous X
     * @param oldY Previous Y
     */
    private void doScrollChanged(final int x, final int y, final int oldX, final int oldY) {
        if (mViewHashMap != null) {
            entriesPointer = mViewHashMap.entrySet();
            iteratorPointer = entriesPointer.iterator();
            while (iteratorPointer.hasNext()) {
                entryPointer = iteratorPointer.next();

                if (entryPointer == null)
                    continue;

                // Remove if view removed
                viewPointer = entryPointer.getKey();
                if (viewPointer == null) entriesPointer.remove(entryPointer);

                // Parallax the other view
                ParallaxHelper.scrollViewBy(viewPointer, x, y, entryPointer.getValue());
            }
        }
        //Parallax this background if we can
        if (mWrappedParallaxBackground != null) {
            ParallaxHelper.scrollBackgroundBy(mWrappedParallaxBackground, x, y, mParallaxBackgroundMultiplier);
        }
        // Scroll Changed Listener?
        if (mScrollChangedListener != null) {
            mScrollChangedListener.onScrollChanged(mWrappedView, x, y, oldX, oldY);
        }
    }

    /**
     * Internal Class that listens to the ScrollChanged ViewTree, stops onScrollChanged() becoming public on
     * {@link uk.co.chrisjenx.paralloid.ParallaxScrollController}
     */
    static class ScrollControllerOnScrollObserver implements ViewTreeObserver.OnScrollChangedListener {

        private final ParallaxScrollController mParallaxScrollController;

        public ScrollControllerOnScrollObserver(ParallaxScrollController parallaxScrollController) {
            mParallaxScrollController = parallaxScrollController;
        }

        @Override
        public void onScrollChanged() {
            mParallaxScrollController.onScrollChanged(false);
        }
    }


}
