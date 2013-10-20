package uk.co.chrisjenx.paralloid.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import uk.co.chrisjenx.paralloid.OnScrollChangedListener;
import uk.co.chrisjenx.paralloid.ParallaxScrollController;
import uk.co.chrisjenx.paralloid.Parallaxor;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxListView extends ListView implements Parallaxor {

    ParallaxScrollController mParallaxScrollController;

    public ParallaxListView(Context context) {
        super(context);
        init();
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mParallaxScrollController = ParallaxScrollController.wrap(this);
    }

    @Override
    public void parallaxViewBy(View view, float multiplier) {
        mParallaxScrollController.parallaxViewBy(view, multiplier);
    }

    @Override
    public void parallaxBackgroundBy(Drawable drawable, float multiplier) {
        mParallaxScrollController.parallaxBackgroundBy(drawable, multiplier);
    }

    @Override
    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        mParallaxScrollController.setOnScrollListener(onScrollChangedListener);
    }
}
