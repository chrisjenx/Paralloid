Paralloid
=========

Paralloid - (Pah-rah-loid) The Android Parallax library.

ParallaxScrollView is dead..
----------------------------

I originally wrote ParallaxScrollView a while back as proof of concept, people took surprisingly well to it;
but from the get-go it was a fundamentally flawed.

Tightly-coupled and inflexible, made it difficult to maintain and confusing for people to use.

Features
--------

Currently limited but will expand when requests.

+ Parallax another view when the parent scrolls.
+ Parallax the background

__Supports:__

- `ListView`
- `ScrollView`
- `HorizontalScrollView`


Modular
-------

Paralloid is designed to be modular and very loosely coupled (to an extent).

Two high level modules exist

- `paralloid` - which is the library which everything extends from.
- `paralloidviews` - which extends the library with a couple of pre-extended ScrollableViews.
- `paralloidexamples` - which is of course the examples App.

Generally use `paralloidviews` as it contains everything you need to get going.

Getting Started
---------------

### Dependency

_Currently I only support Gradle_

#### Locally:

    dependencies {
        compile project(':paralloidviews')
    }
    
#### Or Repository (coming soon):

    dependencies {
        compile 'uk.co.chrisjenx.paralloid:paralloid:0.1.+'
    }

### Layout

#### ScrollView

This is an example, please refer to the `paralloidexample` App for full code.

    <FrameLayout ..>
    <FrameLayout 
    		android:id="@+id/top_content"
               	android:layout_width="match_parent"
               	android:layout_height="192dp"/>

    <uk.co.chrisjenx.paralloid.views.ParallaxScrollView
            android:id="@+id/scroll_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fillViewport="true">

        <LinearLayout
        	android:id="@+id/scroll_content"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:paddingTop="192dp"/>
                
    </uk.co.chrisjenx.paralloid.views.ParallaxScrollView>
    </FrameLayout>
    
    
### Fragment

Inside your `onViewCreated()` or `onCreateView()`.

    //...
    FrameLayout topContent = (FrameLayout) rootView.findViewById(R.id.top_content);
    ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
    if (scrollView instanceof Parallaxor) {
            ((Parallaxor) scrollView).parallaxViewBy(topContent, 0.5f);
    }
    // TODO: add content to top/scroll content
    
    
Thats it!

Have a look at the `Parallaxor` interface for applicable Parallax methods.
    

Custom View's
-------------

I tried to keep this as simple as possible, you can nearly copy and paste this.

- Extend the Scrollable view (or the one you allready have).
- Impliment the `Parallaxor` interface.
- Job done! See below for an example

__Example implimentation:__
    
    public class MyScrollView extends ScrollView impliments Parallaxor {
      //...
      ParallaxScrollController mParallaxScrollController;
      
      private void init() {
        mParallaxScrollController = ParallaxScrollController.wrap(this);
      }
      
      @Override
      public void parallaxViewBy(View view, float factor) {
        mParallaxScrollController.parallaxViewBy(view, factor);
      }
      
      @Override
      public void parallaxBackgroundBy(Drawable drawable, float factor) {
        mParallaxScrollController.parallaxBackgroundBy(drawable, factor);
      }
      
      @Override
      protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mParallaxScrollController.onScrollChanged(this, l, t, oldl, oldt);
      }
    }
    
    
