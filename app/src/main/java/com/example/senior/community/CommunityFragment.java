package com.example.senior.community;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senior.R;
import com.example.senior.SQLDB.DBHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CommunityFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        DBHelper helper;
        helper = new DBHelper(container.getContext(), "post.db", null, 2);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        recyclerView = view.findViewById(R.id.fragment_community_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        Log.d("tesdfasdfasdf", "123");

        return view;
    }

    public void uploadDB(String name, String text) {
        ContentValues values = new ContentValues();
        values.put("userName", name);
        values.put("postTime", new SimpleDateFormat("yy/MM/dd HH:mm").format(System.currentTimeMillis()));
        values.put("postData", text);
        db.insert("mytable",null,values);
    }

}