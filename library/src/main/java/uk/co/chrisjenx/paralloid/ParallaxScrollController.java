package uk.co.chrisjenx.paralloid;

import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static android.view.ViewTreeObserver.OnScrollChangedListener;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxScrollController<T extends View & ParallaxScrollController.Parallaxor> implements OnScrollChangedListener {

    public static String TAG = ParallaxScrollController.class.getSimpleName();

    public static <T extends View & ParallaxScrollController.Parallaxor> ParallaxScrollController wrap(T wrappedView) {
        return new ParallaxScrollController<T>(wrappedView);
    }

    /**
     * The wrapped controller.
     */
    final T mWrappedView;

    /**
     * HashMap which contains the parallaxed views.
     */
    private WeakHashMap<View, Float> mViewHashMap;

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
            observer.addOnScrollChangedListener(this);
        }
    }

    /**
     * Add a view to be parallax'd by. If already set this will replace the current factor.
     *
     * @param view
     * @param factor
     */
    public void parallaxViewBy(View view, float factor) {
        if(view == null) return;
        if(mViewHashMap == null)
            mViewHashMap = new WeakHashMap<View, Float>();

        mViewHashMap.put(view, Float.valueOf(factor));
        onScrollChanged();
    }

    /**
     * Listens to the view tree observer
     */
    @Override
    public void onScrollChanged() {
        onScrollChanged(false);
    }

    /**
     * Something has changed.
     * @param force
     */
    private void onScrollChanged(boolean force) {
        final int offsetX = mWrappedView.getScrollX();
        final int offsetY = mWrappedView.getScrollY();
        if (offsetX != mLastScrollX || offsetY != mLastScrollY || force)
            doScrollChanged(offsetX, offsetY, mLastScrollX, mLastScrollY);
    }


    private Set<Map.Entry<View,Float>> mEntriesPointer;

    /**
     * Will do the scroll changed stuff
     *
     * @param x
     * @param y
     * @param oldX
     * @param oldY
     */
    private void doScrollChanged(final int x, final int y, final int oldX, final int oldY) {
        if (mViewHashMap != null) {
            mEntriesPointer = mViewHashMap.entrySet();
            final Iterator<Map.Entry<View,Float>> iterator = mEntriesPointer.iterator();
            View view;
            Map.Entry<View, Float> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();

                if (entry == null)
                    continue;

                // Remove if view removed
                view = entry.getKey();
                if (view == null) mEntriesPointer.remove(entry);

                // Parallax the other view
                ParallaxHelper.scrollViewBy(view, x, y, entry.getValue());
            }
        }
    }

    /**
     * Suggests that this view will let you parallax others.
     */
    public static interface Parallaxor {
        /**
         * Which view we want to move by.
         *
         * The view that implements this should call too {@link uk.co.chrisjenx.paralloid.ParallaxScrollController#parallaxViewBy(android.view.View, float)}
         *
         * @param view View to move when this moves
         * @param multiplier 1.0f is the normal amount, a 1:1 ratio, 0.5f would move at half the distance of this view etc..
         */
        public void parallaxViewBy(View view, float multiplier);
    }


}
