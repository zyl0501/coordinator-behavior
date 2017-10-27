package com.ray.coordinatorlayout.test.touch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ray.coordinatorlayout.test.MainActivity;
import com.ray.coordinatorlayout.test.R;

/**
 * Created by zyl on 2017/10/27.
 */

public class TouchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
    }

    public void onBehavior(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
