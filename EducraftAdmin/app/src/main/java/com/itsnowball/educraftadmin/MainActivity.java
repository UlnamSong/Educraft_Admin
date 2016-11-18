package com.itsnowball.educraftadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private Typeface typeface      = null;
    private Typeface typeface_b    = null;
    private Typeface typeface_m    = null;

    private AppData[] listData     = null;

    private TextView top_title     = null;
    public TextView tvName         = null;
    public TextView tvPoint        = null;
    public TextView tvPointText    = null;

    public Bitmap bmp = null;

    private Button settingBtn      = null;
    private Button recommendBtn    = null;

    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    private String appDataJSONString = "[ " +
            "{ \"app_name\" : \"GALAXY FANTASY VR\", " +
            "\"app_image\" : \"https://lh3.googleusercontent.com/cDxxdwScQ7F7eVf6uzy3TX7vRA-ecgnjRQXcaf84kK6qFQTZvdTwdc7FmfU4mOOStow2=w300-rw\"," +
            "\"app_package\" : \"com.Snowball.UniverseVR\"," +
            "\"app_point\" : \"250\" }," +
            "{ \"app_name\" : \"애니팡2\", " +
            "\"app_image\" : \"http://www.katalkgame.co.kr/images/game/kz0lsxap8x3qwt4o3bzl.png\"," +
            "\"app_package\" : \"com.sundaytoz.mobile.anipang2.google.kakao.service\"," +
            "\"app_point\" : \"130\" }," +
            "{ \"app_name\" : \"카카오톡\", " +
            "\"app_image\" : \"https://lh6.ggpht.com/71QkuTssZGm4B9_Vf0RINVZRJaOdNzU9OYzR6jZH2b6u-V7E1Lrw1K3nl56PVRHueVQ=w300\"," +
            "\"app_package\" : \"com.kakao.talk\"," +
            "\"app_point\" : \"20\" }," +
            "{ \"app_name\" : \"모두의 마블\", " +
            "\"app_image\" : \"http://c3.img.netmarble.kr/web/6N/2015/12/211152/momak_pc.png\"," +
            "\"app_package\" : \"com.cjenm.ModooMarbleKakao\"," +
            "\"app_point\" : \"180\" }," +
            "{ \"app_name\" : \"애니팡\", " +
            "\"app_image\" : \"https://d3cg7yq507vrx4.cloudfront.net/c9/e2/22/3a/imagen-i-ee-i-2-for-kakao-0thumb.jpg\"," +
            "\"app_package\" : \"com.sundaytoz.mobile.anipang.google.service\"," +
            "\"app_point\" : \"10\" }," +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTypeface();
        setContentView(R.layout.activity_main);

        top_title = (TextView) findViewById(R.id.tv_title);
        top_title.setTypeface(typeface_b);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setTypeface(typeface_b);

        tvPoint = (TextView) findViewById(R.id.tv_point);
        tvPoint.setTypeface(typeface_m);

        tvPointText = (TextView) findViewById(R.id.pointText);
        tvPointText.setTypeface(typeface);

        settingBtn = (Button) findViewById(R.id.set_button);
        recommendBtn = (Button) findViewById(R.id.recommend_button);

        settingBtn.setTypeface(typeface_m);
        recommendBtn.setTypeface(typeface_m);

        loadUserData(UserData.userEmail);

        ListView listView = (ListView) this.findViewById(R.id.listView);

        this.generateDummyData();
        final AppItemAdapter itemAdapter = new AppItemAdapter(this, R.layout.postitem, listData);
        listView.setAdapter(itemAdapter);

        final Handler handler = new Handler();
       // handler.postDelayed( new Runnable() {
       //     @Override
       //     public void run() {
       //         itemAdapter.notifyDataSetChanged();
       //         //Log.d(TAG, "run: notify");
       //     }
       // }, 1500);


        // ListView Item Click Process
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String text = "id : " + id + "/ position : " + position;
                //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, AppInfoPopupActivity.class);
                intent.putExtra("image_index", position);
                intent.putExtra("app_name", listData[position].appName);
                intent.putExtra("app_point", listData[position].appPoint);
                intent.putExtra("app_package", listData[position].appPackage);

                String temp = "";

                if(listData[position].appIsInstalled) {
                    temp = "true";
                } else {
                    temp = "false";
                }
                intent.putExtra("app_installed", temp);

                Log.d(TAG, "onItemClick: appIsInstalled : " + listData[position].appIsInstalled);

                startActivity(intent);
            }
        });

        // 환경 설정 버튼 클릭시 이벤트 처리
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SettingActivity
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("name", UserData.userName);
                intent.putExtra("email", UserData.userEmail);
                startActivity(intent);
            }
        });

        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
                intent.putExtra("name", UserData.userName);
                intent.putExtra("email", UserData.userEmail);
                startActivity(intent);
            }
        });

    }

    //사용자 정보 조회 메소드
    public void loadUserData(String email) {
        String randomString = Data.generateRandomValue();

        String url = "http://52.78.34.164/api/external/fn103.do";

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDDHHmmss");
        long tempTime = System.currentTimeMillis();
        String currentDateTimeString = sdf.format(tempTime);

        String transactionID = currentDateTimeString + randomString;

        String MACAddr = Utils.getMACAddress("wlan0");
        String MACAddress = MACAddr;

        String key = MACAddr; 		// key는 16자 이상
        AES256Util aes256 = null;
        StringEncrypter strEnc = null;
        try {
            aes256 = new AES256Util(key);
            //String encText = aes256.aesEncode(et_password.getText().toString());
            //String encodedpassword = encText;

            strEnc = new StringEncrypter(key);

            Log.d("TAG", "Before Connection");
            String response = new HttpUtils(url, transactionID, MACAddress,
                    "FN103", currentDateTimeString, email,
                    "",
                    "",
                    "",
                    "").execute().get();

            JSONObject job = new JSONObject(response);
            String result_code = job.getString("result_code");
            if(result_code.equals("S0001")) {
                // Main 화면으로 이동하는 코드 입력
                Log.d(TAG, "loadUserData: Response : " + response);
                String bodyData = job.getString("body");

                String decodeData = strEnc.decrypt(bodyData);
                Log.d(TAG, "loadUserData: decodeData : " + decodeData);

                JSONObject job2 = new JSONObject(decodeData);
                UserData.userPoint = job2.getString("point");
                UserData.userName = job2.getString("name");

                tvName.setText(UserData.userName);
                tvPoint.setText(UserData.userPoint + "P");


                //UserData.userPoint = job.getString("")

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        showDialog("어플리케이션 종료", "종료하시겠습니까?", 3);
    }

    private void loadTypeface(){
        if(typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);

        if(typeface_m == null)
            typeface_m = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_MID);

        if(typeface_b == null)
            typeface_b = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_BOLD);

    }

    public void showDialog(String input_title, String input_content, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

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

    // Test Code
    private void generateDummyData() {
        AppData data = null;

        try {
            JSONArray jar = new JSONArray(appDataJSONString);
            listData = new AppData[jar.length() - 1];

            Log.d(TAG, "generateDummyData: jar.length() : " + jar.length());

            for(int i = 0; i < (jar.length() - 1); i++) {
                JSONObject job = jar.getJSONObject(i);

                String name = job.getString("app_name");
                String image = job.getString("app_image");
                String point = job.getString("app_point");
                String appPackage = job.getString("app_package");

                data = new AppData();
                data.appName = name;
                data.appPoint = point;
                data.appPackage = appPackage;
                data.appIsInstalled = checkInstalled(data.appPackage);

                // Dummy Image Url
                data.appImageUrl = image;

                Log.d(TAG, "generateDummyData: listData["+ i + "] : " + data.appImageUrl);
                listData[i] = data;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean checkInstalled(String appPackage) {
        // 설치 여부 확인
        Intent startLink = getPackageManager().getLaunchIntentForPackage(appPackage);

        if(startLink == null) {
            return false;
        } else {
            return true;
        }
    }
}
