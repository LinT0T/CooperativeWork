package com.ndhzs.slideshow.viewpager2.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class RotateDownPageTransformer implements ViewPager2.PageTransformer {
    private static final float DEFAULT_CENTER = 0.5f;
    private static final float DEFAULT_MAX_ROTATE = 15.0f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;

    public RotateDownPageTransformer() {
    }

    public RotateDownPageTransformer(float maxRotate) {
        mMaxRotate = maxRotate;
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position < -1) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setRotation(mMaxRotate * -1);
            view.setPivotX(view.getWidth());
            view.setPivotY(view.getHeight());

        } else if (position <= 1) { // [-1,1]
            if (position < 0) {//[0，-1]
                view.setPivotX(view.getWidth() * (DEFAULT_CENTER + DEFAULT_CENTER * (-position)));
                view.setPivotY(view.getHeight());
                view.setRotation(mMaxRotate * position);
            } else {//[1,0]
                view.setPivotX(view.getWidth() * DEFAULT_CENTER * (1 - position));
                view.setPivotY(view.getHeight());
                view.setRotation(mMaxRotate * position);
            }
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setRotation(mMaxRotate);
            view.setPivotX(0);
            view.setPivotY(view.getHeight());
        }
    }
}
