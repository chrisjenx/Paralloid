Paralloid
=========

Paralloid - (Pah-rah-loid) The Android Parallax library.

ParallaxScrollView is dead..
----------------------------

I originally wrote ParallaxScrollView a while back as proof of concept, people took surprisingly well to it;
but from the get-go it was a fundamentally flawed.

Tightly-coupled and inflexible, made it difficult to maintain and confusing for people to use.

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

__Gradle (I will not support for Maven)__
Locally:

    dependencies {
	    compile 'com.android.support:appcompat-v7:18.0.+'
        compile project(':paralloidviews')
    }
    
Or Repository:

    dependencies {
    	compile 'com.android.support:appcompat-v7:18.0.+'
        compile 'uk.co.chrisjenx.paralloid:paralloid:0.1.+'
    }


Custom View's
-------------

I tried to keep this as simple as possible, you can nearlly copy and paste this.

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
    
    
