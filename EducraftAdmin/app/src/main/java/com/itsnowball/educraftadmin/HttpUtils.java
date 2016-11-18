package com.itsnowball.educraftadmin;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.codec.net.URLCodec;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ulnamsong on 2016. 11. 13..
 */
public class HttpUtils extends AsyncTask<String, Void, String> {

    public String reuslt_code;

    private String password;
    private String email;
    private String transactionID;
    private String func_code;
    private String datetime;
    private String recommend;
    private String version;
    private String mobile_num;
    private String macaddress;
    private String name;
    private String url;

    private String response;

    HttpUtils(String url, String transactionID, String macaddress, String func_code, String datetime, String email, String password, String recommend, String mobile_num, String name) {
        Log.d("TAG", "HttpUtils Constructor");
        this.password = password;
        this.email = email;
        this.transactionID = transactionID;
        this.func_code = func_code;
        this.datetime = datetime;
        this.version = Data.version;
        this.recommend = recommend;
        this.mobile_num = mobile_num;
        this.macaddress = macaddress;
        this.name = name;
        this.url = url;
    }

    @Override
    public String doInBackground(String... params) {
        OutputStream os   = null;
        InputStream is   = null;
        ByteArrayOutputStream baos = null;

        Log.i("TAG", HttpURLConnection.HTTP_OK + "");

        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            URLCodec codec = new URLCodec();

            conn.setReadTimeout(6000);
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/json");

            StringEncrypter strEnc = null;
            strEnc = new StringEncrypter(macaddress);

            Log.d("TAG", "JSON Body");
            JSONObject body = new JSONObject();
            body.put("password", password); // 암호화// 한 후 보내기
            body.put("recommender", "");
            body.put("name", name);
            body.put("mobile_num", "");

            Log.d("TAG", "JSON Data");
            JSONObject data = new JSONObject();
            data.put("version", version);
            data.put("transaction_id", transactionID); // XXXXXX 랜덤으로 반드시 생성한 후 전송 + 날짜받아서 넣기
            data.put("func_code", func_code);
            data.put("datetime", datetime); // 날짜받아서 넣기
            data.put("email", email);
            data.put("mac_addr", macaddress);
            data.put("body", strEnc.encrypt(body.toString()));

            Log.d("DATA TAG", data.toString());

            os = conn.getOutputStream();
            os.write( data.toString().getBytes() );
            os.close();

            Log.i("TAG", "Before Get Code");

            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
                JSONObject job2 = new JSONObject(response);
                job2.getString("result_code");
                Log.e("TAG", "DATA response = " + response);
            } else {
                Log.e("TAG", "DATA response = ERROR");
                Log.e("TAG", "responseCode is not HTTP_OK!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}

