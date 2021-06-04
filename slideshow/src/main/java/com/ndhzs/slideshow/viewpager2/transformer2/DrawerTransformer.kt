package com.ndhzs.slideshow.viewpager2.transformer2

import android.view.View
import com.ndhzs.slideshow.viewpager2.transformer2.ABaseTransformer

/**
 * Created by dkzwm on 2017/3/2.
 *
 * @author dkzwm
 */
open class DrawerTransformer : ABaseTransformer() {

    override fun onTransform(page: View, position: Float) {
        if (position <= 0) page.translationX = 0f
        else if (position <= 1) page.translationX = -page.width / 2 * position
    }
}
