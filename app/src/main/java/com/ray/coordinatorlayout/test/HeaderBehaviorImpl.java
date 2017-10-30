package com.ray.coordinatorlayout.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zyl on 2017/10/27.
 */

public class HeaderBehaviorImpl extends HeaderBehavior<View> {
    public HeaderBehaviorImpl() {}

    public HeaderBehaviorImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    boolean canDragView(View view) {
        return true;
    }
}
