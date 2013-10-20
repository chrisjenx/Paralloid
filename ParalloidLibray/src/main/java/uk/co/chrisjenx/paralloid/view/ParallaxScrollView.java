package uk.co.chrisjenx.paralloid.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import uk.co.chrisjenx.paralloid.ParallaxScrollController;
import uk.co.chrisjenx.paralloid.Parallaxor;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxScrollView extends ScrollView implements Parallaxor {

    ParallaxScrollController mParallaxScrollController;

    public ParallaxScrollView(Context context) {
        super(context);
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
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
        //TODO
    }
}
