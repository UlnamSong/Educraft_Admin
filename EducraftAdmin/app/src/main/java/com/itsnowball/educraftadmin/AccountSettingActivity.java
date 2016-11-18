package com.itsnowball.educraftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.codec.net.URLCodec;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class AccountSettingActivity extends ActionBarActivity {

    private String TAG = "AccountsettingActivity";

    private ActionBar actionBar           = null;

    private Typeface typeface             = null;
    private Typeface typeface_b           = null;
    private Typeface typeface_m           = null;

    private TextView tvCurEmail           = null;
    private TextView tvCurPassword        = null;
    private TextView tvNewPassword        = null;
    private TextView tvNewPasswordConfirm = null;
    private TextView tvNewPhone           = null;

    private EditText etCurEmail           = null;
    private EditText etCurPassword        = null;
    private EditText etNewPassword        = null;
    private EditText etNewPasswordConfirm = null;
    private EditText etNewPhone           = null;

    private Button requestButton          = null;

    private String curEmail               = "";
    private String curPassword            = "";
    private String newPassword            = "";
    private String newPasswordConfirm     = "";
    private String newPhone               = "";

    private String MACAddress     = null;
    private String transactionID  = null;
    private String encodedpassword= null;


    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadTypeface();
        setContentView(R.layout.activity_account_setting);

        actionBar = getSupportActionBar();
        actionBar.setTitle("회원 정보 수정");
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContent();

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curEmail = etCurEmail.getText().toString();
                curPassword = etCurPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                newPasswordConfirm = etNewPasswordConfirm.getText().toString();
                newPhone = etNewPhone.getText().toString();

                if (curEmail.equals("")) {
                    showDialog("현재 이메일 오류", "사용중인 이메일을 입력해주세요.", 1);
                } else if(!curEmail.equals(UserData.userEmail)) {
                    showDialog("이메일 오류", "이메일이 올바르지 않습니다.", 1);
                } else if(curPassword.equals("")) {
                    showDialog("현재 비밀번호 오류", "사용중인 비밀번호를 입력해주세요.", 1);
                } else if (newPhone.equals("")) {
                    showDialog("휴대전화번호 오류", "휴대전화 번호를 입력해주세요 .", 1);
                } else {
                    if (newPassword.equals(newPasswordConfirm)) {
                        // 서버로부터 회원 정보 수정 체크
                        requestToServer();
                    } else {
                        //비밀번호 불일치 (새 비밀번호/ 새 비밀번호 확인)
                        showDialog("새로운 비밀번호 불일치", "비밀번호가 일치하지 않습니다.", 1);
                    }
                }
            }
        });

    }

    private void requestToServer() {
        String randomString = Data.generateRandomValue();

        String url = "http://52.78.34.164/api/external/fn102.do";

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDDHHmmss");
        long tempTime = System.currentTimeMillis();
        String currentDateTimeString = sdf.format(tempTime);
        transactionID = currentDateTimeString + randomString;

        String MACAddr = Utils.getMACAddress("wlan0");
        Log.d(TAG, "signupProcess: MAC : " + MACAddr);

        MACAddress = MACAddr;

        String key = MACAddr; 		// key는 16자 이상
        //AES256Util aes256 = null;
        StringEncrypter strEnc = null;
        URLCodec codec = new URLCodec();

        try {
            //aes256 = new AES256Util(key);
            strEnc = new StringEncrypter(key);

            //String encText = aes256.aesEncode(etPw.getText().toString());
            String encText =strEnc.encrypt(newPassword);

            encodedpassword = encText;

            Log.d("TAG", "Before Connection");
            String response = new HttpUtils(url, transactionID, MACAddress,
                    "FN102", currentDateTimeString, curEmail,
                    newPassword,
                    "",
                    newPhone,
                    UserData.userName).execute().get();

            JSONObject job = new JSONObject(response);

            String resultText = job.getString("result_code");

            if(resultText.equals("S0001")) {
                showDialog("수정 완료", "정상적으로 수정되었습니다.", 2);
            } else {
                switch(resultText) {
                    case "E1021":
                        showDialog("존재하지 않는 회원", "존재하지 않는 회원입니다.", 1);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTypeface(){
        if(typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);

        if(typeface_m == null)
            typeface_m = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_MID);

        if(typeface_b == null)
            typeface_b = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_BOLD);

    }

    protected void setContent() {
        tvCurEmail           = (TextView) findViewById(R.id.tv_curemail);
        tvCurPassword        = (TextView) findViewById(R.id.tv_curpassword);
        tvNewPassword        = (TextView) findViewById(R.id.tv_newpassword);
        tvNewPasswordConfirm = (TextView) findViewById(R.id.tv_newpassword_confirm);
        tvNewPhone           = (TextView) findViewById(R.id.tv_newphone);

        tvCurEmail.setTypeface(typeface_m);
        tvCurPassword.setTypeface(typeface_m);
        tvNewPassword.setTypeface(typeface_m);
        tvNewPasswordConfirm.setTypeface(typeface_m);
        tvNewPhone.setTypeface(typeface_m);

        etCurEmail           = (EditText) findViewById(R.id.et_CurEmail);
        etCurPassword        = (EditText) findViewById(R.id.et_CurPassword);
        etNewPassword        = (EditText) findViewById(R.id.et_NewPassword);
        etNewPasswordConfirm = (EditText) findViewById(R.id.et_NewPasswordConfirm);
        etNewPhone           = (EditText) findViewById(R.id.et_NewPhone);

        //etCurEmail.setTypeface(typeface);
        //etCurPassword.setTypeface(typeface);
        //etNewPassword.setTypeface(typeface);
        //etNewPasswordConfirm.setTypeface(typeface);

        requestButton        = (Button) findViewById(R.id.req_button);
        requestButton.setTypeface(typeface_m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // doing something
                finish();
                return true;
            default:
                return false;
        }
    }

    public void showDialog(String input_title, String input_content, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountSettingActivity.this);

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
        }

        alertDialog.show();
    }
}
