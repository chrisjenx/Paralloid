package uk.co.chrisjenx.paralloid;

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import uk.co.chrisjenx.paralloid.graphics.ParallaxDrawable;
import uk.co.chrisjenx.paralloid.utils.ParallaxHelper;

/**
 * Parallax Controller that take input from anything.
 * <p/>
 * Created by chris on 23/10/2013
 * Project: Paralloid
 */
public class ParallaxController<T extends Object> implements ParallaxorListener {

    public static final String TAG = ParallaxController.class.getSimpleName();
    /**
     * HashMap which contains the parallaxed views.
     */
    protected WeakHashMap<View, ParallaxViewInfo> mViewHashMap;
    /**
     * The background of the wrapped View to Parallax
     */
    protected ParallaxDrawable mWrappedParallaxBackground;
    /**
     * The Optional Scroll Changed Listener for the user to listen to scroll events.
     */
    protected OnScrollChangedListener mScrollChangedListener;
    /**
     * Gets set to true if a View which has its own OnScrollListener
     */
    protected boolean mIgnoreOnScrollListener;
    //
    protected int mLastScrollX = 0;
    protected int mLastScrollY = 0;

    /**
     * The Wrapped Object
     */
    protected final T mWrapped;

    protected ParallaxController(T wrapped) {
        mWrapped = wrapped;
    }

    public T getWrapped() {
        return mWrapped;
    }

    /**
     * Add a view to be parallax'd by. If already set this will replace the current factor.
     *
     * @param view
     * @param multiplier
     */
    public void parallaxViewBy(View view, float multiplier) {
        if (view == null) return;
        if (view == mWrapped)
            throw new IllegalArgumentException("You can't parallax yourself, this would end badly, Parallax other Views");
        if (mViewHashMap == null)
            mViewHashMap = new WeakHashMap<View, ParallaxViewInfo>();

        mViewHashMap.put(view, new ParallaxViewInfo(multiplier, null));
        // We force this to update the view just added
        onScrollChanged(mLastScrollX, mLastScrollY, mLastScrollX, mLastScrollY, true);
    }

    /**
     * @param view View which to attach the drawable to.
     * @param drawable Drawable to Parallax
     * @param multiplier How much to move it by, 1 would be a 1:1 relationship. 0.5 would move the
     */
    @Override
    public void parallaxViewBackgroundBy(final View view, final Drawable drawable, final float multiplier) {
        mWrappedParallaxBackground = ParallaxHelper.getParallaxDrawable(drawable, multiplier);
        ParallaxHelper.setParallaxBackground(view, mWrappedParallaxBackground);
    }

    /**
     * Feel free to implement {@link OnScrollChangedListener} to get call
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

    /**
     * Used to do parallax, Your implementation should call directly through to this. Some implementations ignore this
     * on purpose (e.g. the ListView)
     *
     * @param who if Who != getWrapped we ignore scrolling, only the object the controller is attached too can
     *            scroll it.
     */
    @Override
    public final void onScrollChanged(Object who, int l, int t, int oldl, int oldt) {
        if (who != getWrapped() || mIgnoreOnScrollListener) return;
        onScrollChanged(l, t, oldl, oldt, false);
    }

    protected final void onScrollChanged(int offsetX, int offsetY, int oldOffsetX, int oldOffsetY, boolean force) {
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
    protected final void doScrollChanged(final int x, final int y, final int oldX, final int oldY) {
        doScrollViews(x, y);
        //Parallax this background if we can
        doScrollBackground(x, y);
        // Scroll Changed Listener
        doScrollListener(getWrapped(),x, y, oldX, oldY);

        // Set new LastScrollPos
        mLastScrollX = x;
        mLastScrollY = y;
    }
    // --
    // doScrollChanged Pointers to keep memory consumption down during fast scrolling
    //
    private Set<Map.Entry<View, ParallaxViewInfo>> entriesPointer;
    private Iterator<Map.Entry<View, ParallaxViewInfo>> iteratorPointer;
    private Map.Entry<View, ParallaxViewInfo> entryPointer;
    private ParallaxViewInfo parallaxInfoPointer;
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
                ParallaxHelper.scrollViewBy(viewPointer, x, y, parallaxInfoPointer.factor);
            }
        }
    }



    private final void doScrollBackground(final int x, final int y) {
        if (mWrappedParallaxBackground != null) {
            ParallaxHelper.scrollBackgroundBy(mWrappedParallaxBackground, x, y);
        }
    }

    private final void doScrollListener(final Object who, final int x, final int y, final int oldX, final int oldY) {
        if (mScrollChangedListener != null && (x != oldX || y != oldY)) {
            mScrollChangedListener.onScrollChanged(who, x, y, oldX, oldY);
        }
    }
}
