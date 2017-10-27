/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ray.coordinatorlayout.test;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

/**
 * Behavior will automatically sets up a {@link ViewOffsetHelper} on a {@link View}.
 */
class ViewOffsetBehavior2<V extends View> extends CoordinatorLayout.Behavior<V> {

//    private ViewOffsetHelper mViewOffsetHelper;
//
//    private int mTempTopBottomOffset = 0;
//    private int mTempLeftRightOffset = 0;

    private SparseArray<ViewOffsetHelper> offsetHelpers;
    private SparseIntArray tempTopBottomOffsets;
    private SparseIntArray tempLeftRightOffsets;

    public ViewOffsetBehavior2() {
        offsetHelpers = new SparseArray<>(1);
        tempTopBottomOffsets = new SparseIntArray(1);
        tempLeftRightOffsets = new SparseIntArray(1);
    }

    public ViewOffsetBehavior2(Context context, AttributeSet attrs) {
        super(context, attrs);
        offsetHelpers = new SparseArray<>(1);
        tempTopBottomOffsets = new SparseIntArray(1);
        tempLeftRightOffsets = new SparseIntArray(1);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        // First let lay the child out
        layoutChild(parent, child, layoutDirection);

//        if (mViewOffsetHelper == null) {
//            mViewOffsetHelper = new ViewOffsetHelper(child);
//        }
//        mViewOffsetHelper.onViewLayout();
//
//        if (mTempTopBottomOffset != 0) {
//            mViewOffsetHelper.setTopAndBottomOffset(mTempTopBottomOffset);
//            mTempTopBottomOffset = 0;
//        }
//        if (mTempLeftRightOffset != 0) {
//            mViewOffsetHelper.setLeftAndRightOffset(mTempLeftRightOffset);
//            mTempLeftRightOffset = 0;
//        }

        int size = offsetHelpers.size();
        for (int i = 0; i < size; i++) {
            int key = offsetHelpers.keyAt(i);
            ViewOffsetHelper helper = offsetHelpers.get(key);
            if (helper != null) {
                helper.onViewLayout();
                int topBottomOffset = tempTopBottomOffsets.get(key, 0);
                if (topBottomOffset != 0) {
                    helper.setTopAndBottomOffset(topBottomOffset);
                    tempTopBottomOffsets.put(key, 0);
                }
                int leftRightOffset = tempLeftRightOffsets.get(key, 0);
                if (leftRightOffset != 0) {
                    helper.setLeftAndRightOffset(leftRightOffset);
                    tempLeftRightOffsets.put(key, 0);
                }
            }
        }
        return true;
    }

    protected void layoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        // Let the parent lay it out by default
        parent.onLayoutChild(child, layoutDirection);
    }

    public boolean setTopAndBottomOffset(View view, int offset) {
        ViewOffsetHelper helper = getOffsetHelper(view);
        if (helper != null) {
            return helper.setTopAndBottomOffset(offset);
        } else {
            tempTopBottomOffsets.put(view.hashCode(), offset);
        }
        return false;
    }

    public boolean setLeftAndRightOffset(View view, int offset) {
        ViewOffsetHelper helper = getOffsetHelper(view);
        if (helper != null) {
            return helper.setLeftAndRightOffset(offset);
        } else {
            tempLeftRightOffsets.put(view.hashCode(), offset);
        }
        return false;
    }

    public int getTopAndBottomOffset(View view) {
        ViewOffsetHelper helper = getOffsetHelper(view);
        return helper != null ? helper.getTopAndBottomOffset() : 0;
    }

    public int getLeftAndRightOffset(View view) {
        ViewOffsetHelper helper = getOffsetHelper(view);
        return helper != null ? helper.getLeftAndRightOffset() : 0;
    }

    private ViewOffsetHelper getOffsetHelper(View view) {
        int hashCode = view.hashCode();
        ViewOffsetHelper helper = offsetHelpers.get(hashCode);
        if (helper == null) {
            helper = new ViewOffsetHelper(view);
            offsetHelpers.put(hashCode, helper);
        }
        return helper;
    }
}