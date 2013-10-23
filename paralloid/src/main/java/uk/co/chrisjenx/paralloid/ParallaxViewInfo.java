package uk.co.chrisjenx.paralloid;

import uk.co.chrisjenx.paralloid.transform.Transformer;

class ParallaxViewInfo {

    final float factor;
    final Transformer interpolator;

    private int maxX;
    private int maxY;

    ParallaxViewInfo(float factor, Transformer interpolator) {
        this.factor = factor;
        this.interpolator = interpolator;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}