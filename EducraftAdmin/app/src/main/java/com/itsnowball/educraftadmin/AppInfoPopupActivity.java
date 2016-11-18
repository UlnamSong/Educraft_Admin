package com.itsnowball.educraftadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import static android.content.ContentValues.TAG;

public class AppInfoPopupActivity extends Activity implements OnClickListener {

    private Button installBtn = null;
    private Button cancelBtn  = null;
    private ImageView appImageView = null;
    private TextView appTitle = null;
    private TextView appTitleCon = null;
    private TextView appContent = null;
    private TextView appContentCon = null;
    private TextView appPointTV = null;

    public Bitmap bmp = null;

    private String appName = "";
    private String appPoint = "";
    private String appPackage = "";
    private int image_index = 0;

    public String appInstalled = "";
    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    private Typeface typeface      = null;
    private Typeface typeface_b    = null;
    private Typeface typeface_m    = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadTypeface();
        setContentView(R.layout.activity_app_info_popup);

        Intent intent = getIntent();
        appInstalled = intent.getStringExtra("app_installed");
        appName = intent.getStringExtra("app_name");
        appPoint = intent.getStringExtra("app_point");
        appPackage = intent.getStringExtra("app_package");
        image_index = intent.getIntExtra("image_index", 0);

        setContent();

        appTitleCon.setText(appName);
        appContentCon.setText("Dummy Information (Will be Corrected.");

        try {
            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    appImageView.setImageBitmap(ImageStorage.imageArr[image_index]);
                }
            };

            new Thread() {
                public void run() {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }.start();
        } catch(Exception e) {
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

    private void setContent() {
        installBtn = (Button) findViewById(R.id.install_button);
        cancelBtn = (Button) findViewById(R.id.cancel_button);
        appContent = (TextView) findViewById(R.id.tv_appcontent);
        appContentCon = (TextView) findViewById(R.id.tv_appcontent_con);
        appTitleCon = (TextView) findViewById(R.id.tv_apptitle_con);
        appTitle = (TextView) findViewById(R.id.tv_apptitle);
        appImageView = (ImageView) findViewById(R.id.iv_image);

        appContentCon.setTypeface(typeface);
        appContent.setTypeface(typeface_m);
        appTitleCon.setTypeface(typeface);
        appTitle.setTypeface(typeface_m);

        installBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        Log.d(TAG, "setContent: appInstalled : " + appInstalled);

        if(appInstalled.equals("true")) {
            installBtn.setText("실행하기");
        } else {
            installBtn.setText("내려받기");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.install_button:

                if(appInstalled.equals("true")) {
                    // 이미 설치된 앱일 경우 실행한다.
                    showDialog("앱 실행", "어플리케이션을 실행합니다.", 2);
                } else {
                    // 플레이 스토어로 이동시킨다.
                    showDialog("플레이스토어 이동", "설치를 위해 플레이스토어로 이동합니다.", 2);
                }

                break;

            case R.id.cancel_button:
                this.finish();
                break;
        }
    }

    public void showDialog(String input_title, String input_content, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AppInfoPopupActivity.this);

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
            // OK
            case 1:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code
                    }
                });
                break;
            // Yes or No
            case 2:
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel(); // Your custom code

                        if(appInstalled.equals("false")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=" + appPackage));
                            startActivity(intent);
                        } else {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appPackage);
                            startActivity(launchIntent);
                        }
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
}
