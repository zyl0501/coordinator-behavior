package com.ray.coordinatorlayout.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyl on 2017/10/30.
 */

public class ScrollHeaderBehavior extends ViewOffsetBehavior<View> {
    private int targetId;
    private View dependency;

    public ScrollHeaderBehavior() {
    }

    public ScrollHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderBehavior);
        targetId = a.getResourceId(R.styleable.HeaderBehavior_hb_target_id, View.NO_ID);
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (targetId != View.NO_ID && targetId == dependency.getId()) {
            this.dependency = dependency;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        dependency.getLayoutParams();
         ((ViewGroup)dependency).onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
         dependency.onTouchEvent(ev);
         return false;
    }
}
