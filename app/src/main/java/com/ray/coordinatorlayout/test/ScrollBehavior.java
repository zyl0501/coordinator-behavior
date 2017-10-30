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
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Created by zyl on 2017/10/26.
 */
public class ScrollBehavior extends ViewOffsetBehavior<View> {
    private static final String TAG = ScrollBehavior.class.getSimpleName();

    private int targetId;
    private int scrollId;
    private View dependency;
    private View scrollView;
    final Rect mTempRect1 = new Rect();
    final Rect mTempRect2 = new Rect();

    public ScrollBehavior() {
        super();
    }

    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollBehavior);
        targetId = a.getResourceId(R.styleable.ScrollBehavior_sb_target_id, View.NO_ID);
        scrollId = a.getResourceId(R.styleable.ScrollBehavior_sb_scroll_id, View.NO_ID);
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(final CoordinatorLayout parent, View child, final View dependency) {
        if (targetId != View.NO_ID && targetId == dependency.getId()) {
            this.dependency = dependency;
            if (scrollId != View.NO_ID) {
                scrollView = child.findViewById(scrollId);
            } else {
                scrollView = child;
            }

//            parent.post(new Runnable() {
//                @Override
//                public void run() {
//                    Rect bounds = new Rect();
//                    scrollView.getHitRect(bounds);
//                    bounds.top -= dependency.getMeasuredHeight();
//                    Log.e(TAG, "bounds: "+bounds);
//                    TouchDelegate touchDelegate = new TouchDelegate(bounds, scrollView);
//                    parent.setTouchDelegate(touchDelegate);
//                }
//            });
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

//    @Override
//    boolean canDragView(View view) {
//        return true;
//    }

    protected void layoutChild(final CoordinatorLayout parent, final View child,
                               final int layoutDirection) {
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

//            final WindowInsetsCompat parentInsets = null;
            final WindowInsetsCompat parentInsets = getCoordinatorLastWindowInsets(parent);
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
//            ViewCompat.offsetTopAndBottom(dependency, getTopAndBottomOffset());

            scrollDependency(getTopAndBottomOffset());
        } else {
            super.layoutChild(parent, child, layoutDirection);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dependency != null) {
            if (dy != 0) {
                int consumedY = 0;
                int top = child.getTop();
                int depTop = dependency.getTop();
                //dy 小于0表示向上滑动，大于0表示向下滑动
                if (dy < 0) {
                    // We're scrolling down
                    if (!ViewCompat.canScrollVertically(scrollView, -1)) {
                        if (depTop < 0) {
                            consumedY = -Math.max(dy, depTop);
                        }
                    }
                } else {
                    // We're scrolling up
                    //canScrollVertically 小于0表示能否向下滚动，大于0表示能否向上滚动
                    if (ViewCompat.canScrollVertically(scrollView, 1)) {
                        if (top > 0) {
                            consumedY = -Math.min(dy, top);
                        }
                    }
                }
                //offsetTopAndBottom 小于0向上，大于0向下
                scrollDependency(getTopAndBottomOffset() + consumedY);
                setTopAndBottomOffset(getTopAndBottomOffset() + consumedY);
                consumed[1] = -consumedY;
            }
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    private void scrollDependency(int offset) {
        if (dependency != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            if (behavior != null && (behavior instanceof ViewOffsetBehavior)) {
                ((ViewOffsetBehavior) behavior).setTopAndBottomOffset(offset);
            } else {
                ViewCompat.offsetTopAndBottom(dependency, offset - getTopAndBottomOffset());
            }
        }
    }

    private static int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }

    private int getScrollRange(View v) {
        return v.getMeasuredHeight();
    }

    private WindowInsetsCompat getCoordinatorLastWindowInsets(CoordinatorLayout coordinatorLayout) {
        try {
            Method method = CoordinatorLayout.class.getDeclaredMethod("getLastWindowInsets");
            return (WindowInsetsCompat) method.invoke(coordinatorLayout);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
