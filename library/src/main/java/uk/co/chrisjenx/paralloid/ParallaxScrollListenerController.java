package uk.co.chrisjenx.paralloid;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import static android.view.ViewTreeObserver.OnScrollChangedListener;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxScrollListenerController<T extends View & ParallaxScrollListenerController.ParallaxScrollView> implements OnScrollChangedListener {

    public static String TAG = ParallaxScrollListenerController.class.getSimpleName();

    /**
     * The wrapped controller.
     */
    final T mWrappedView;

    public ParallaxScrollListenerController(T wrappedView) {
        mWrappedView = wrappedView;
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
        Log.d(TAG, "Scroll Changed");
        int offsetX = mWrappedView.getScrollX();
        int offsetY = mWrappedView.getScrollY();
    }

    public static interface ParallaxScrollView {
        public void parallaxViewBy(View view);
    }
}
