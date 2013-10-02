package uk.co.chrisjenx.paralloid;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

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

    private ParallaxScrollController(T wrappedView) {
        mWrappedView = wrappedView;
        init();
    }

    public T getWrappedView() {
        return mWrappedView;
    }

    private void init(){
        if(mWrappedView == null)
            throw new IllegalArgumentException("The wrapped view cannot be null");

        final ViewTreeObserver observer = mWrappedView.getViewTreeObserver();
        if (observer != null) {
            observer.addOnScrollChangedListener(this);
        }
    }

    @Override
    public void onScrollChanged() {
        final int offsetX = mWrappedView.getScrollX();
        final int offsetY = mWrappedView.getScrollY();
        Log.d(TAG, String.format("X: %d, Y: %d", offsetX, offsetY));
    }

    /**
     * Suggests that this view will let you parallax others.
     */
    public static interface Parallaxor {
        public void parallaxViewBy(View view);
    }
}
