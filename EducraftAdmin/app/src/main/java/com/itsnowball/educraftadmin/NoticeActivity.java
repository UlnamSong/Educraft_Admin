package com.itsnowball.educraftadmin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class NoticeActivity extends AppCompatActivity {

    private ActionBar actionBar = null;
    private NoticeData[] listData2 = null;
    public ListView listView;

    private String noticeDataJSONString = "[ " +
            "{ \"title\" : \"어플리케이션 이용 안내 공지사항입니다.\", " +
            "\"content\" : \"알아서 잘 이용하시기 바랍니다.\" }," +
            "{ \"title\" : \"계정 등록 안내 공지사항입니다.\", " +
            "\"content\" : \"알아서 회원 가입 잘 하시면 됩니다.\"}," +
            "{ \"title\" : \"에듀크래프트 공지사항입니다.\", " +
            "\"content\" : \"안녕하세요 에듀크래프트 공지사항입니다.\" }," +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        actionBar = getSupportActionBar();
        actionBar.setTitle("공지사항");
        actionBar.setDisplayHomeAsUpEnabled(true);

        NoticeData data = new NoticeData();;
        NoticeData[] temp = null;

        try {
            JSONArray jar = new JSONArray(noticeDataJSONString);
            listData2 = new NoticeData[jar.length() - 1];

            Log.d(TAG, "generateDummyData: jar.length() : " + jar.length());

            for(int i = 0; i < (jar.length() - 1); i++) {
                JSONObject job = jar.getJSONObject(i);

                String title = job.getString("title");
                String content = job.getString("content");

                Log.d(TAG, "generateDummyData: i : " + i);
                Log.d(TAG, "generateDummyData: data : " + title + ", " + content);
                data.title = title;
                data.content = content;
                listData2[i] = data;
                Log.d(TAG, "generateDummyData: listData2.get(" + i + ") : " + listData2[i].title);
                for(int a = 0; a <= i; ++a) {
                    Log.d(TAG, "onCreate: listData2.get[" + a + "] : " + listData2[i].title);
                }
            }

            listView = (ListView) this.findViewById(R.id.listView);

            final NoticeItemAdapter itemAdapter = new NoticeItemAdapter(this, R.layout.noticeitem, listData2);
            listView.setAdapter(itemAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String text = "id : " + id + "/ position : " + position;
                    //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(NoticeActivity.this, NoticeInfoActivity.class);
                    intent.putExtra("title", listData2[position].title);
                    intent.putExtra("content", listData2[position].content);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
