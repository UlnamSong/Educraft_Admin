package com.itsnowball.educraftadmin;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class UsageagreeActivity extends ActionBarActivity {

    private ActionBar actionBar = null;
    private Typeface typeface_m = null;
    private TextView tv_content = null;
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFRegular.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTypeface();
        setContentView(R.layout.activity_usageagree);

        actionBar = getSupportActionBar();
        actionBar.setTitle("서비스 이용약관");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_content = (TextView) findViewById(R.id.textView14);
        tv_content.setTypeface(typeface_m);
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

    private void loadTypeface(){
        if(typeface_m == null)
            typeface_m = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_MID);
    }
}
