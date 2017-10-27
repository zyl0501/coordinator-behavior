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

public class TouchView extends View {
    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TouchView", "method dispatchTouchEvent");
        logEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TouchView", "method onTouchEvent");
        logEvent(event);
        return true;
    }


    private void logEvent(MotionEvent ev){
        int action = ev.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e("TouchView", "action: "+ "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("TouchView", "action: "+ "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TouchView", "action: "+ "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("TouchView", "action: "+ "ACTION_CANCEL");
                break;
        }
    }
}
