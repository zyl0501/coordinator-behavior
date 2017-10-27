package com.ray.coordinatorlayout.test.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyl on 2017/10/27.
 */

public class TouchViewGroup extends ViewGroup {
    public TouchViewGroup(Context context) {
        super(context);
    }

    public TouchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        if(child != null){
            child.layout(100,100,400,400);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TouchViewGroup", "method dispatchTouchEvent");
        logEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TouchViewGroup", "method onInterceptTouchEvent");
        logEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TouchViewGroup", "method onTouchEvent");
        logEvent(event);
        return false;
    }

    private void logEvent(MotionEvent ev){
        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e("TouchViewGroup", "action: "+ "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("TouchViewGroup", "action: "+ "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TouchViewGroup", "action: "+ "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("TouchViewGroup", "action: "+ "ACTION_CANCEL");
                break;
        }
    }
}
