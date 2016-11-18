package com.itsnowball.educraftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.net.URLCodec;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class SignupActivity extends ActionBarActivity {

    private String TAG            = "SignupActivity.class";

    private String email          = null;
    private String name           = null;
    private String phone          = null;
    private String result         = null;
    private String recommend      = null;
    private String MACAddress     = null;
    private String transactionID  = null;
    private String encodedpassword= null;

    private ActionBar actionBar   = null;
    private Button    signupBtn   = null;

    private TextView  tvEmail     = null;
    private TextView  tvPw        = null;
    private TextView  tvPwAgain   = null;
    private TextView  tvName      = null;
    private TextView  tvPhone     = null;
    private TextView  tvRecommend = null;

    private EditText  etEmail     = null;
    private EditText  etPw        = null;
    private EditText  etPwAgain   = null;
    private EditText  etName      = null;
    private EditText  etPhone     = null;
    private EditText  etRecommend = null;

    private Typeface typeface     = null;
    private Typeface typeface_b   = null;
    private Typeface typeface_m   = null;

    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadTypeface();
        setContentView(R.layout.activity_signup);

        actionBar = getSupportActionBar();
        actionBar.setTitle("회원가입하기");
        actionBar.setDisplayHomeAsUpEnabled(true);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPw = (EditText) findViewById(R.id.et_password);
        etPwAgain = (EditText) findViewById(R.id.et_password_again);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etRecommend = (EditText) findViewById(R.id.et_recommend);

        tvEmail = (TextView) findViewById(R.id.textView6);
        tvPw = (TextView) findViewById(R.id.textView8);
        tvPwAgain = (TextView) findViewById(R.id.textView9);

        tvName = (TextView) findViewById(R.id.textView10);
        tvPhone = (TextView) findViewById(R.id.textView11);
        tvRecommend = (TextView) findViewById(R.id.textView12);

        tvEmail.setTypeface(typeface_m);
        tvPw.setTypeface(typeface_m);
        tvPwAgain.setTypeface(typeface_m);
        tvName.setTypeface(typeface_m);
        tvPhone.setTypeface(typeface_m);
        tvRecommend.setTypeface(typeface_m);

        signupBtn = (Button) findViewById(R.id.signup_button);
        signupBtn.setTypeface(typeface_m);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().equals("")) {
                    showDialog("이메일 오류", "이메일을 입력해주세요.", 1);
                } else if(!isValidEmail(etEmail.getText().toString())){
                    showDialog("이메일 형식 오류", "이메일 형식이 아닙니다.", 1);
                } else if(etPw.getText().toString().equals("")) {
                    showDialog("비밀번호 오류", "비밀번호를 입력해주세요.", 1);
                } else if(!etPw.getText().toString().equals(etPwAgain.getText().toString())) {
                    showDialog("비밀번호 불일치", "비밀번호가 일치하지 않습니다.", 1);
                } else if(etName.getText().toString().equals("")) {
                    showDialog("이름 오류", "이름을 입력해주세요.", 1);
                } else if(etPhone.getText().toString().equals("")) {
                    showDialog("휴대전화 번호 오류", "휴대전화 번호를 입력해주세요.", 1);
                } else {
                    if(etRecommend.getText().toString().equals("")) {
                        showDialog("추천인 입력", "추천인 입력 항목이 비었습니다. 계속하시겠습니까?", 3);
                    } else  {
                        signupProcess();
                    }
                }
            }
        });

    }

    private void loadTypeface(){
        if(typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);

        if(typeface_m == null)
            typeface_m = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_MID);

        if(typeface_b == null)
            typeface_b = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_BOLD);

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

    // Dialog 테스트
    public void showDialog(String input_title, String input_content, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupActivity.this);

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
                        dialog.cancel();
                        finish();
                    }
                });
                break;

            // Yes and No
            case 3:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code
                        signupProcess();
                    }
                });

                alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code
                    }
                });
                break;
        }

        alertDialog.show();
    }

    // 서버로 정보전송하여 회원 가입 진행
    public void signupProcess() {
        Log.d("SignupActivity.class", "signupProcess: Start");
        String randomString = Data.generateRandomValue();

        String url = "http://52.78.34.164/api/external/fn101.do";

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDDHHmmss");
        long tempTime = System.currentTimeMillis();
        String currentDateTimeString = sdf.format(tempTime);
        transactionID = currentDateTimeString + randomString;

        String MACAddr = Utils.getMACAddress("wlan0");
        Log.d(TAG, "signupProcess: MAC : " + MACAddr);

        email = etEmail.getText().toString();
        recommend = etRecommend.getText().toString();
        MACAddress = MACAddr;
        name = etName.getText().toString();
        phone = etPhone.getText().toString();

        String key = MACAddr; 		// key는 16자 이상
        AES256Util aes256 = null;
        StringEncrypter strEnc = null;
        URLCodec codec = new URLCodec();

        try {
            aes256 = new AES256Util(key);
            strEnc = new StringEncrypter(key);

            //String encText = aes256.aesEncode(etPw.getText().toString());
            String encText =strEnc.encrypt(etPw.getText().toString());

            encodedpassword = encText;

            Log.d("TAG", "Before Connection");
            String response = new HttpUtils(url, transactionID, MACAddress,
                    "FN101", currentDateTimeString, email,
                    etPw.getText().toString(),
                    recommend,
                    phone,
                    name).execute().get();

            JSONObject job = new JSONObject(response);

            String resultText = job.getString("result_code");

            if(resultText.equals("S0001")) {
                showDialog("회원가입 완료", "회원가입이 완료되었습니다.", 2);
            } else {
                switch(resultText) {
                    case "E1011":
                        showDialog("이메일 오류", "이미 존재하는 이메일입니다.", 1);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Email Format Validation Code
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}