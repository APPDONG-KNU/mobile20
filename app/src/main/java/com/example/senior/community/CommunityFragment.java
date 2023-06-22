package com.example.senior.community;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.senior.R;
import com.example.senior.SQLDB.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommunityFragment extends Fragment {
    private View view;
    private TextView logo;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        DBHelper helper;
        helper = new DBHelper(container.getContext(), "post.db", null, 2);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view1 -> {
            Intent it = new Intent(container.getContext(), PostActivity.class);
            startActivity(it);
        });

        logo = view.findViewById(R.id.top);
        logo.setOnClickListener(view1 -> {
            recyclerView.smoothScrollToPosition(0);
        });

        recyclerView = view.findViewById(R.id.fragment_community_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(new CommunityFeedAdapter(getDB()));

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setAdapter(new CommunityFeedAdapter(getDB()));
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    public List<CommunityFeedData> getDB() {
        List<CommunityFeedData> list = new ArrayList<>();
        String sql = "select * from mytable;";
        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex("userName"));
            @SuppressLint("Range") String time = c.getString(c.getColumnIndex("postTime"));
            @SuppressLint("Range") String text = c.getString(c.getColumnIndex("postData"));
            list.add(new CommunityFeedData(name, time, text));
        }
        Collections.reverse(list);
        return list;
    }

    public void clearDBAll() {
        db.execSQL("delete from mytable");
    }
}