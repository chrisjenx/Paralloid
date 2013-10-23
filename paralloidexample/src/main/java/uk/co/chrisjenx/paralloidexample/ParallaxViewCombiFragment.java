package uk.co.chrisjenx.paralloidexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import uk.co.chrisjenx.paralloid.Parallaxor;
import uk.co.chrisjenx.paralloid.transform.LeftAngleTransformer;
import uk.co.chrisjenx.paralloid.transform.RightAngleTransformer;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ParallaxViewCombiFragment extends Fragment {

    public ParallaxViewCombiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_combi_transformer, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
        ImageView imageView2 = (ImageView) rootView.findViewById(R.id.image_view2);
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
        if (scrollView instanceof Parallaxor) {
            ((Parallaxor) scrollView).parallaxViewBy(imageView, new LeftAngleTransformer(), 0.2f);
            ((Parallaxor) scrollView).parallaxViewBy(imageView2, new RightAngleTransformer(), 0.2f);
        }

        return rootView;
    }
}