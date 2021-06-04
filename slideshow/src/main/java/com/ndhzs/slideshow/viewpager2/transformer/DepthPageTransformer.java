package com.ndhzs.slideshow.viewpager2.transformer;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    private static final float DEFAULT_CENTER = 0.5f;
    private static final float DEFAULT_MIN_SCALE = 0.75f;
    private float mMinScale = DEFAULT_MIN_SCALE;

    public DepthPageTransformer() {
    }

    public DepthPageTransformer(float minScale) {
        this.mMinScale = minScale;
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0f);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1f);
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);

        } else if (position <= 1) { // (0,1]
            //进入页面时
            view.setVisibility(View.VISIBLE);
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = mMinScale
                    + (1 - mMinScale) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            //退出页面时
            if(position ==1){
                view.setVisibility(View.INVISIBLE);
            }

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0f);
        }
    }
}
