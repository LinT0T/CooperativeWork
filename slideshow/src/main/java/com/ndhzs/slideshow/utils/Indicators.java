package com.ndhzs.slideshow.utils;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;
import java.lang.annotation.Retention;
import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;
import static com.ndhzs.slideshow.utils.Indicators.Gravity.*;
import static com.ndhzs.slideshow.utils.Indicators.Style.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * .....
 *
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/29
 */
public class Indicators {

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    @Retention(SOURCE)
    @IntDef({NULL_VIEW, NORMAL})
    public @interface Style {
        int NULL_VIEW = 0;
        int NORMAL = 1;
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    @Retention(SOURCE)
    @IntDef({
            TOP,
            BOTTOM,
            LEFT,
            RIGHT,
            TOP_LEFT,
            TOP_CENTER,
            TOP_RIGHT,
            BOTTOM_LEFT,
            BOTTOM_CENTER,
            BOTTOM_RIGHT,
            LEFT_CENTER,
            RIGHT_CENTER,})
    public @interface Gravity {
        int TOP = 0x30;
        int BOTTOM = 0x50;
        int LEFT = 0x03;
        int RIGHT = 0x05;
        int CENTER = 0x11;
        int CENTER_VERTICAL = 0x10;
        int CENTER_HORIZONTAL = 0x01;
        int TOP_LEFT = TOP | LEFT;
        int TOP_CENTER = TOP | CENTER_HORIZONTAL;
        int TOP_RIGHT = TOP | RIGHT;
        int BOTTOM_LEFT = BOTTOM | LEFT;
        int BOTTOM_CENTER = BOTTOM | CENTER_HORIZONTAL;
        int BOTTOM_RIGHT = BOTTOM | RIGHT;
        int LEFT_CENTER = LEFT | CENTER_VERTICAL;
        int RIGHT_CENTER = RIGHT | CENTER_VERTICAL;
    }

    public static boolean isError(int gravity) {
        return !(gravity == TOP ||
                gravity == BOTTOM ||
                gravity == LEFT ||
                gravity == RIGHT ||
                gravity == CENTER ||
                gravity == CENTER_VERTICAL ||
                gravity == CENTER_HORIZONTAL ||
                gravity == TOP_LEFT ||
                gravity == TOP_CENTER ||
                gravity == TOP_RIGHT ||
                gravity == BOTTOM_LEFT ||
                gravity == BOTTOM_CENTER ||
                gravity == BOTTOM_RIGHT ||
                gravity == LEFT_CENTER ||
                gravity == RIGHT_CENTER);
    }
}
