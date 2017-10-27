package com.ray.coordinatorlayout.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyl on 2017/10/26.
 */
public class ScrollBehavior extends CoordinatorLayout.Behavior<View> {

    private int targetId;
    private int offsetTotal = 0;
    private boolean scrolling = false;
    private View dependency;
    final Rect mTempRect1 = new Rect();
    final Rect mTempRect2 = new Rect();

    public ScrollBehavior() {
        super();
    }

    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollBehavior);
        targetId = a.getResourceId(R.styleable.ScrollBehavior_target_id, View.NO_ID);
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (targetId != View.NO_ID && targetId == dependency.getId()) {
            this.dependency = dependency;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec,
                                  int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            final View header = dependency;
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    ViewCompat.setFitsSystemWindows(child, true);

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }

                int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.getHeight();
                }

                final int height = availableHeight - header.getMeasuredHeight()
                        + getScrollRange(header);
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                                ? View.MeasureSpec.EXACTLY
                                : View.MeasureSpec.AT_MOST);

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed);

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        final View header = dependency;

        if (header != null) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final Rect available = mTempRect1;
            available.set(parent.getPaddingLeft() + lp.leftMargin,
                    header.getBottom() + lp.topMargin,
                    parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
                    parent.getHeight() + header.getBottom()
                            - parent.getPaddingBottom() - lp.bottomMargin);

            final WindowInsetsCompat parentInsets = null;
//            final WindowInsetsCompat parentInsets = parent.getLastWindowInsets();
            if (parentInsets != null && ViewCompat.getFitsSystemWindows(parent)
                    && !ViewCompat.getFitsSystemWindows(child)) {
                // If we're set to handle insets but this child isn't, then it has been measured as
                // if there are no insets. We need to lay it out to match horizontally.
                // Top and bottom and already handled in the logic above
                available.left += parentInsets.getSystemWindowInsetLeft();
                available.right -= parentInsets.getSystemWindowInsetRight();
            }

            final Rect out = mTempRect2;
            GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
                    child.getMeasuredHeight(), available, out, layoutDirection);


            child.layout(out.left, out.top, out.right, out.bottom);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.e("raytest", "method onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.e("raytest", "method onNestedScrollAccepted");
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        Log.e("raytest", "method onStopNestedScroll");
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.e("raytest", "method onNestedScroll");
        Log.e("raytest", "onNestedScroll " + "dxConsumed:" + dxConsumed + " dyConsumed:" + dyConsumed + " dxUnconsumed:" + dxUnconsumed + " dyUnconsumed:" + dyUnconsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.e("raytest", "method onNestedPreScroll");
        Log.e("raytest", "onNestedPreScroll " + "dy:" + dy);
        if (dependency != null) {
            if (dy != 0) {
                Log.e("raytest", "onNestedPreScroll " + "canScrollVertically 向上:" + ViewCompat.canScrollVertically(child, 1));
                Log.e("raytest", "onNestedPreScroll " + "canScrollVertically 向下:" + ViewCompat.canScrollVertically(child, -1));
                int consumedY = 0;
                int top = child.getTop();
                int depTop = dependency.getTop();
                Log.e("raytest", "onNestedPreScroll " + "top:" + top + " dy:" + dy);
                //dy 小于0表示向上滑动，大于0表示向下滑动
                if (dy < 0) {
                    // We're scrolling down
                    if (!ViewCompat.canScrollVertically(child, -1)) {
                        if (depTop < 0) {
                            consumedY = -Math.max(dy, depTop);
                        }
                    }
                } else {
                    // We're scrolling up
                    //canScrollVertically 小于0表示能否向下滚动，大于0表示能否向上滚动
                    if (ViewCompat.canScrollVertically(child, 1)) {
                        if (top > 0) {
                            consumedY = -Math.min(dy, top);
                        }
                    }
                }
                Log.e("raytest", "onNestedPreScroll " + "consumedY:" + consumedY);
                //offsetTopAndBottom 小于0向上，大于0向下
                ViewCompat.offsetTopAndBottom(dependency, consumedY);
                ViewCompat.offsetTopAndBottom(child, consumedY);
                consumed[1] = -consumedY;
            }
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("raytest", "method onNestedFling");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        Log.e("raytest", "method onNestedPreFling");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private static int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }

    private int getScrollRange(View v) {
        return v.getMeasuredHeight();
    }
}
