package com.stories.stories.storyBottomViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

public class CubeTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
//if position = 0 (current image) pivot on x axis is on the right, else if
// position > 0, (next image) pivot on x axis is on the left (origin of the axis)
//        view.setPivotX(position <= 0 ? view.getWidth() : 0.0f);
//        view.setPivotY(view.getHeight() * 0.5f);
////it rotates with 90 degrees multiplied by current position
//        view.setRotationY(90f * position);

        if (position < -1){    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        }
        else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(-90 * Math.abs(position));

        }
        else if (position <= 1){    // (0,1]
            page.setAlpha(1);
            page.setPivotX(0);
            page.setRotationY(90 * Math.abs(position));

        }
        else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }
    }
}
