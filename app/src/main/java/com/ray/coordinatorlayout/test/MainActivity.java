package com.ray.coordinatorlayout.test;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tomtaw.widget.multi_swipe_refresh_layout.MultiSwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SimpleAdapter(this, 20));

//        MultiSwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.multi_swipe);
//        swipeRefreshLayout.setCanChildScrollUpCallback(new MultiSwipeRefreshLayout.CanChildScrollUpCallback() {
//            @Override
//            public boolean canSwipeRefreshChildScrollUp() {
//                return ViewCompat.canScrollVertically(recyclerView, -1);
//            }
//        });
    }
}
