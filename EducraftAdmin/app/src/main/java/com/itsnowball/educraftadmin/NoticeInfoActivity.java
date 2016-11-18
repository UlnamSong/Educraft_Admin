package com.itsnowball.educraftadmin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeInfoActivity extends AppCompatActivity {

    private ActionBar actionBar = null;
    private TextView tvTitle = null;
    private TextView tvContent = null;

    String title = "";
    String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_info);

        actionBar = getSupportActionBar();
        actionBar.setTitle("공지사항 세부내용");
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) this.findViewById(R.id.listView);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");

        tvTitle = (TextView) findViewById(R.id.tvTitleContent);
        tvContent = (TextView) findViewById(R.id.tvContentContent);

        tvTitle.setText(title);
        tvContent.setText(content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
