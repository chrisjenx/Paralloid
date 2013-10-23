package uk.co.chrisjenx.paralloidexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.chrisjenx.paralloid.views.ParallaxScrollView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ParallaxBackgroundFragment extends Fragment {

    public ParallaxBackgroundFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parallax_background, container, false);
        ParallaxScrollView scrollView = (ParallaxScrollView) rootView.findViewById(R.id.scroll_view);
        scrollView.parallaxViewBackgroundBy(scrollView, getResources().getDrawable(R.drawable.example_image), .2f);

        return rootView;
    }
}