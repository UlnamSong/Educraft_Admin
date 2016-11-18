package com.itsnowball.educraftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AgreementActivity extends ActionBarActivity {

    private ActionBar actionBar    = null;

    private Typeface typeface      = null;
    private Typeface typeface_b    = null;
    private Typeface typeface_m    = null;

    private TextView tv_title      = null;
    private TextView tv_content    = null;

    private Button pButton         = null;
    private Button uButton         = null;
    private Button continueBtn     = null;

    private ImageButton pImageBtn  = null;
    private ImageButton uImageBtn  = null;

    private String TAG             = "AgreementActivity.java";

    // 약관 동의 체크 버튼 (위, 아래) 체크여부 Boolean 변수
    private boolean isOneChecked   = false;
    private boolean isTwoChecked   = false;


    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadTypeface();
        setContentView(R.layout.activity_agreement);

        actionBar = getSupportActionBar();
        actionBar.setTitle("회원가입하기");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_title = (TextView) findViewById(R.id.textView7);
        tv_title.setTypeface(typeface_m);

        tv_content = (TextView) findViewById(R.id.textView13);
        tv_content.setTypeface(typeface);

        pButton = (Button) findViewById(R.id.button_p);
        uButton = (Button) findViewById(R.id.button_u);
        pButton.setTypeface(typeface_m);
        uButton.setTypeface(typeface_m);

        pImageBtn = (ImageButton) findViewById(R.id.imageButton_p);
        uImageBtn = (ImageButton) findViewById(R.id.imageButton_u);

        pImageBtn.bringToFront();
        uImageBtn.bringToFront();

        continueBtn = (Button) findViewById(R.id.button3);
        continueBtn.setTypeface(typeface_m);

        uImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: uImageBtn");
                if(!isOneChecked) {
                    isOneChecked = true;
                    uImageBtn.setImageResource(R.drawable.check_yes);
                } else {
                    isOneChecked = false;
                    uImageBtn.setImageResource(R.drawable.check_no);
                }
            }
        });

        pImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pImageBtn");
                if(!isTwoChecked) {
                    isTwoChecked = true;
                    pImageBtn.setImageResource(R.drawable.check_yes);
                } else {
                    isTwoChecked = false;
                    pImageBtn.setImageResource(R.drawable.check_no);
                }
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreementActivity.this, MemberagreeActivity.class);
                startActivity(intent);
            }
        });

        uButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreementActivity.this, UsageagreeActivity.class);
                startActivity(intent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + isOneChecked + ", " + isTwoChecked);
                if(isOneChecked) {
                    if(isTwoChecked) {
                        showDialog("계속 진행", "모든 약관에 동의하고 진행합니다.", 3);
                    } else {
                        showDialog("약관 동의 오류", "개인정보 취급방침에 동의해주세요.", 1);
                    }
                } else {
                    showDialog("약관 동의 오류", "서비스 약관 동의에 동의해주세요.", 1);
                }
            }
        });
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
        if(typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);

        if(typeface_m == null)
            typeface_m = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_MID);

        if(typeface_b == null)
            typeface_b = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_BOLD);

    }

    // Dialog 테스트
    public void showDialog(String input_title, String input_content, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AgreementActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        TextView title = (TextView) dialogView.findViewById(R.id.tv_dialog_title);
        TextView content = (TextView) dialogView.findViewById(R.id.tv_dialog_content);

        title.setTypeface(typeface_m);
        content.setTypeface(typeface_m);

        title.setText(input_title);
        content.setText(input_content);
        alertDialog.setView(dialogView);

        switch(type) {
            // OK with Error
            case 1:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code
                    }
                });
                break;

            // OK with no Error
            case 2:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(AgreementActivity.this, SignupActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                        finish();
                    }
                });
                break;

            // Yes and No
            case 3:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("계속 진행", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(AgreementActivity.this, SignupActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                        finish();
                    }
                });

                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code
                    }
                });
                break;
        }

        alertDialog.show();
    }
}
