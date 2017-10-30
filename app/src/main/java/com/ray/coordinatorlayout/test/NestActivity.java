package com.ray.coordinatorlayout.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by zyl on 2017/10/27.
 */

public class NestActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SimpleAdapter(this, 20));

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("raytest", "recycler height " + recyclerView.getMeasuredHeight());
            }
        }, 2000);
    }
}
