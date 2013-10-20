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