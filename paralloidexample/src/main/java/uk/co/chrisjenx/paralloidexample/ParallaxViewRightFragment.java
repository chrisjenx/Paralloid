package uk.co.chrisjenx.paralloidexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import uk.co.chrisjenx.paralloid.Parallaxor;
import uk.co.chrisjenx.paralloid.transform.RightAngleTransformer;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ParallaxViewRightFragment extends Fragment {

    public ParallaxViewRightFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invert_transformer, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
        if (scrollView instanceof Parallaxor) {
            ((Parallaxor) scrollView).parallaxViewBy(imageView, new RightAngleTransformer(), 0.4f);
        }

        return rootView;
    }
}