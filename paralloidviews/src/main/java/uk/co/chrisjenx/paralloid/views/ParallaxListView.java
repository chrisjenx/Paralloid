package uk.co.chrisjenx.paralloid.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import uk.co.chrisjenx.paralloid.ParallaxViewController;
import uk.co.chrisjenx.paralloid.Parallaxor;
import uk.co.chrisjenx.paralloid.transform.Transformer;

/**
 * Created by chris on 02/10/2013
 * Project: Paralloid
 */
public class ParallaxListView extends ListView implements Parallaxor {

    ParallaxViewController mParallaxViewController;

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
        mParallaxViewController = ParallaxViewController.wrap(this);
    }

    @Override
    public void parallaxViewBy(View view, float multiplier) {
        mParallaxViewController.parallaxViewBy(view, multiplier);
    }

    @Override
    public void parallaxViewBy(View view, Transformer transformer, float multiplier) {
        mParallaxViewController.parallaxViewBy(view, transformer, multiplier);
    }

    @Override
    public void parallaxViewBackgroundBy(View view, Drawable drawable, float multiplier) {
        mParallaxViewController.parallaxViewBackgroundBy(view, drawable, multiplier);
    }
}
