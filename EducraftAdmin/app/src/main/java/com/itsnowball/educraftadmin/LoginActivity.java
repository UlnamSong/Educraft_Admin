package com.itsnowball.educraftadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;

public class LoginActivity extends Activity {

    private Button loginButton     = null;
    private long backKeyPressedTime= 0;

    private Typeface typeface      = null;
    private Typeface typeface_b    = null;
    private Typeface typeface_m    = null;

    private TextView logoText      = null;
    private TextView email_text    = null;
    private TextView password_text = null;
    private TextView signup_text   = null;

    private EditText et_email      = null;
    private EditText et_password   = null;
    public Bitmap bmp2;

    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadTypeface();
        setContentView(R.layout.activity_login);

        logoText = (TextView) findViewById(R.id.textView2);
        logoText.setTypeface(typeface_b);

        email_text = (TextView) findViewById(R.id.textView3);
        email_text.setTypeface(typeface_m);

        password_text = (TextView) findViewById(R.id.textView5);
        password_text.setTypeface(typeface_m);

        signup_text = (TextView) findViewById(R.id.textView4);
        signup_text.setTypeface(typeface_m);

        et_email = (EditText) findViewById(R.id.et_email);
        //et_email.setTypeface(typeface_m);

        et_password = (EditText) findViewById(R.id.et_password);
        //et_password.setTypeface(typeface_m);

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setTypeface(typeface_m);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Email Blank
                if(et_email.getText().toString().equals("")) {
                    showDialog("이메일 오류", "이메일을 입력해주세요.", 1);
                } else if(et_password.getText().toString().equals("")) {
                    showDialog("비밀번호 오류", "비밀번호를 입력해주세요.", 1);
                } else {
                    // Server Check

                    String randomString = Data.generateRandomValue();

                    String url = "http://52.78.34.164/api/external/fn201.do";

                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDDHHmmss");
                    long tempTime = System.currentTimeMillis();
                    String currentDateTimeString = sdf.format(tempTime);

                    String transactionID = currentDateTimeString + randomString;

                    String MACAddr = Utils.getMACAddress("wlan0");

                    String email = et_email.getText().toString();
                    String MACAddress = MACAddr;

                    String key = MACAddr; 		// key는 16자 이상
                    AES256Util aes256 = null;
                    StringEncrypter strEnc = null;

                    try {
                        aes256 = new AES256Util(key);
                        strEnc = new StringEncrypter(key);

                        //String encText = aes256.aesEncode(et_password.getText().toString());
                        String encText = strEnc.encrypt(et_password.getText().toString());
                        String encodedpassword = encText;

                        Log.d("TAG", "Before Connection");
                        String response = new HttpUtils(url, transactionID, MACAddress,
                                "FN201", currentDateTimeString, email,
                                et_password.getText().toString(),
                                "",
                                "",
                                "").execute().get();

                        JSONObject job = new JSONObject(response);
                        String result_code = job.getString("result_code");
                        if(result_code.equals("S0001")) {
                            // Main 화면으로 이동하는 코드 입력
                            UserData.userEmail = email;
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            switch(result_code) {
                                case "E2011":
                                    showDialog("로그인 오류", "이메일 또는 비밀번호가 틀립니다.", 1);
                                    break;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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

    // Dialog 테스트
    public void showDialog(String input_title, String input_content, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

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
                    }
                });
                break;

            // Yes and No
            case 3:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
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

    public void onBackPressed() {
        showDialog("어플리케이션 종료", "종료하시겠습니까?", 3);
    }
}
