package uk.co.chrisjenx.paralloid;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import uk.co.chrisjenx.paralloid.graphics.ParallaxDrawable;
import uk.co.chrisjenx.paralloid.utils.AbsListViewHelper;
import uk.co.chrisjenx.paralloid.utils.ParallaxHelper;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxScrollController<T extends View & Parallaxor> implements ParallaxorListener, AbsListView.OnScrollListener {

    public static String TAG = ParallaxScrollController.class.getSimpleName();

    public static <T extends View & Parallaxor> ParallaxScrollController wrap(T wrappedView) {
        return new ParallaxScrollController<T>(wrappedView);
    }

    /**
     * The wrapped view.
     */
    final T mWrappedView;
    /**
     * HashMap which contains the parallaxed views.
     */
    private WeakHashMap<View, ViewParallaxInfo> mViewHashMap;
    /**
     * The background of the wrapped View to Parallax
     */
    private ParallaxDrawable mWrappedParallaxBackground;
    /**
     * The Optional Scroll Changed Listener for the user to listen to scroll events.
     */
    private OnScrollChangedListener mScrollChangedListener;

    /**
     * Gets set to true if a View which has its own OnScrollListener
     */
    private boolean mIgnoreOnScrollListener;
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

        if (mWrappedView instanceof AbsListView) {
            mIgnoreOnScrollListener = true;
            ((AbsListView) mWrappedView).setOnScrollListener(this);
        }
    }

    /**
     * Add a view to be parallax'd by. If already set this will replace the current factor.
     *
     * @param view
     * @param multiplier
     */
    public void parallaxViewBy(View view, float multiplier) {
        if (view == null) return;
        if (view == mWrappedView)
            throw new IllegalArgumentException("You can't parallax the Parallaxor, this would end badly, Parallax other views");
        if (mViewHashMap == null)
            mViewHashMap = new WeakHashMap<View, ViewParallaxInfo>();

        mViewHashMap.put(view, new ViewParallaxInfo(multiplier, null));
        onScrollChanged(false);
    }

//    @Override
    public void parallaxViewBy(View view, Interpolator interpolator, float multiplier) {
        if (view == null) return;
        if (view == mWrappedView)
            throw new IllegalArgumentException("You can't parallax the Parallaxor, this would end badly, Parallax other views");
        if (mViewHashMap == null)
            mViewHashMap = new WeakHashMap<View, ViewParallaxInfo>();

        mViewHashMap.put(view, new ViewParallaxInfo(multiplier, interpolator));
        onScrollChanged(false);
    }

    @Override
    public void parallaxBackgroundBy(final Drawable drawable, final float multiplier) {
        mWrappedParallaxBackground = ParallaxHelper.setParallaxBackground(mWrappedView, drawable, multiplier);
    }

    /**
     * Feel free to implement {@link uk.co.chrisjenx.paralloid.OnScrollChangedListener} to get call
     * backs to the wrapped view for scroll changed events.
     * <p/>
     * <b>Note</b>: this will get called, AFTER any parallax modification.
     *
     * @param onScrollChangedListener Null is valid (it will remove it if set).
     */
    @Override
    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        mScrollChangedListener = onScrollChangedListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * Used internally by the AbsListView implementation, calling through to this is unnecessary, the controller
     * will happily set the OnScrollListener
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final int offsetY = AbsListViewHelper.calculateOffset(view);
        onScrollChanged(offsetY, false);
    }

    /**
     * Used to do parallax, Your View Should call directly through to this. Some implimentations ignore this
     * on purpose (e.g. the ListView)
     */
    @Override
    public void onScrollChanged(View who, int l, int t, int oldl, int oldt) {
        if (who != mWrappedView || mIgnoreOnScrollListener) return;
        onScrollChanged(l, t, oldl, oldt, false);
        mLastScrollX = oldl;
        mLastScrollY = oldt;
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
        onScrollChanged(offsetX, offsetY, mLastScrollX, mLastScrollY, force);
        mLastScrollX = offsetX;
        mLastScrollY = offsetY;
    }

    private void onScrollChanged(int offsetY, boolean force) {
        final int offsetX = mWrappedView.getScrollX();
        onScrollChanged(offsetX, offsetY, mLastScrollX, mLastScrollY, force);
        mLastScrollX = offsetX;
        mLastScrollY = offsetY;
    }

    private void onScrollChanged(int offsetX, int offsetY, int oldOffsetX, int oldOffsetY, boolean force) {
        if (offsetX != oldOffsetX || offsetY != oldOffsetY || force) {
            doScrollChanged(offsetX, offsetY, oldOffsetX, oldOffsetY);
        }
    }

    /**
     * Will do the scroll changed stuff.
     *
     * @param x    currentX of Parallaxor View
     * @param y    currentX of Parallaxor View
     * @param oldX Previous X
     * @param oldY Previous Y
     */
    protected void doScrollChanged(final int x, final int y, final int oldX, final int oldY) {
        doScrollViews(x, y);
        //Parallax this background if we can
        doScrollBackground(x, y);
        // Scroll Changed Listener
        doScrollListener(x, y, oldX, oldY);
    }

    // --
    // doScrollChanged Pointers to keep memory consumption down during fast scrolling
    //
    private Set<Map.Entry<View,ViewParallaxInfo>> entriesPointer;
    private Iterator<Map.Entry<View,ViewParallaxInfo>> iteratorPointer;
    private Map.Entry<View, ViewParallaxInfo> entryPointer;
    private ViewParallaxInfo parallaxInfoPointer;
    private View viewPointer;
    // --

    private final void doScrollViews(final int x, final int y) {
        if (mViewHashMap != null) {
            entriesPointer = mViewHashMap.entrySet();
            iteratorPointer = entriesPointer.iterator();
            while (iteratorPointer.hasNext()) {
                entryPointer = iteratorPointer.next();

                if (entryPointer == null)
                    continue;

                // Remove if view removed
                viewPointer = entryPointer.getKey();
                parallaxInfoPointer = entryPointer.getValue();
                if (viewPointer == null) entriesPointer.remove(entryPointer);

                // Parallax the other view
                ParallaxHelper.scrollViewBy(viewPointer, x, y, parallaxInfoPointer.interpolator, parallaxInfoPointer.factor);
            }
        }
    }

    private final void doScrollBackground(final int x, final int y) {
        if (mWrappedParallaxBackground != null) {
            ParallaxHelper.scrollBackgroundBy(mWrappedParallaxBackground, x, y);
        }
    }

    private final void doScrollListener(final int x, final int y, final int oldX, final int oldY) {
        if (mScrollChangedListener != null) {
            mScrollChangedListener.onScrollChanged(mWrappedView, x, y, oldX, oldY);
        }
    }

    private static class ViewParallaxInfo {
        private final float factor;
        private final Interpolator interpolator;

        private ViewParallaxInfo(float factor, Interpolator interpolator) {
            this.factor = factor;
            this.interpolator = interpolator;
        }
    }
}
