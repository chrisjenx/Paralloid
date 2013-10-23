package uk.co.chrisjenx.paralloidexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import uk.co.chrisjenx.paralloid.Parallaxor;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ShiftBackgroundFragment extends Fragment {

    public ShiftBackgroundFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parallax_background, container, false);
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
        if (scrollView instanceof Parallaxor) {
            ((Parallaxor) scrollView).parallaxViewBackgroundBy(scrollView, getResources().getDrawable(R.drawable.example_rainbow), 1f);
        }

        return rootView;
    }
}