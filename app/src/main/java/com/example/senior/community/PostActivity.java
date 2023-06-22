package com.example.senior.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.senior.R;
import com.example.senior.SQLDB.DBHelper;

import java.text.SimpleDateFormat;

public class PostActivity extends AppCompatActivity {
    private EditText titleView, contentView;
    private Button cancleBtn, postBtn;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_input);

        titleView = findViewById(R.id.titleEditText);
        contentView = findViewById(R.id.detailEditText);
        cancleBtn = findViewById(R.id.cancelButton);
        postBtn = findViewById(R.id.uploadButton);

        DBHelper helper;
        helper = new DBHelper(this, "post.db", null, 2);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        cancleBtn.setOnClickListener(view -> {
            finish();
        });

        postBtn.setOnClickListener(view -> {
            String title = titleView.getText().toString();
            String content = contentView.getText().toString();
            uploadDB(title, content);
            finish();
        });
    }

    public void uploadDB(String name, String text) {
        ContentValues values = new ContentValues();
        values.put("userName", name);
        values.put("postTime", new SimpleDateFormat("yy/MM/dd HH:mm").format(System.currentTimeMillis()));
        values.put("postData", text);
        db.insert("mytable",null,values);
    }
}