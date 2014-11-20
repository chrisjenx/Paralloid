Paralloid - DEPRECATED
=========

DEPRECATED - Use the project for more of guide and ideas how to apply parallax techniques. I will not support this library anymore.

Paralloid - (Pah-rah-loid) The Android Parallax library

Compatible back to API 7

An example application, [Paralloid Library Example](https://play.google.com/store/apps/details?id=uk.co.chrisjenx.paralloidexample&hl=en),  can be downloaded from the Play Store.

ParallaxScrollView is dead..
----------------------------

I originally wrote ParallaxScrollView a while back as proof of concept, people took surprisingly well to it;
but from the get-go it was a fundamentally flawed.

Tightly-coupled and inflexible, made it difficult to maintain and confusing for people to use.

Features
--------

Currently limited but will expand when requests.

+ Parallax another view when the parent scrolls.
+ Parallax multiple backgrounds.
+ Transformers, Parallax in different ways and directions

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

Temporary steps from @therealkris.

- First, I cloned the library into `myproject/libs/paralloid`.
- In my `settings.gradle`, I added include `':libs:paralloid:paralloid'`, `':libs:paralloid:paralloidviews'`.
- In my `build.gradle`, I added `compile project(':libs:paralloid:paralloid')`, `project(':libs:paralloid:paralloidviews')`.
- In `libs/paralloid/paralloid`, I removed the `uploadArchives {}` block.
- In `libs/paralloid/paralloidviews`, I removed the `uploadArchives {}` block AND changed the dependency to read: `compile project(':libs:paralloid:paralloid')` instead of `compile project(':paralloid')`

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


Custom Views
-------------

I tried to keep this as simple as possible, you can nearly copy and paste this.

- Extend the Scrollable view (or the one you allready have).
- Implement the `Parallaxor` interface.
- Job done! See below for an example

__Example implementation:__

    public class MyScrollView extends ScrollView implements Parallaxor {
      //...
      ParallaxViewController mParallaxViewController;

      // Call init() in constructors
      private void init() {
        mParallaxViewController = ParallaxViewController.wrap(this);
      }

      @Override
      protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mParallaxViewController.onScrollChanged(this, l, t, oldl, oldt);
      }

      // region Implementation of Parallaxor

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

      // endregion
    }
