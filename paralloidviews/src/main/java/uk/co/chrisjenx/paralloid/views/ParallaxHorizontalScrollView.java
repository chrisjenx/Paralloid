package uk.co.chrisjenx.paralloid.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import uk.co.chrisjenx.paralloid.OnScrollChangedListener;
import uk.co.chrisjenx.paralloid.ParallaxScrollController;
import uk.co.chrisjenx.paralloid.Parallaxor;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxHorizontalScrollView extends HorizontalScrollView implements Parallaxor {

    ParallaxScrollController mParallaxScrollController;

    public ParallaxHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public ParallaxHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallaxHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
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
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mParallaxScrollController.onScrollChanged(this, l, t, oldl, oldt);
    }
}
