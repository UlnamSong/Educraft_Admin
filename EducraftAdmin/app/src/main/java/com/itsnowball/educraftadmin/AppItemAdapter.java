package com.itsnowball.educraftadmin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

/**
 * Created by Ulnamsong on 2016. 11. 15..
 */

public class AppItemAdapter extends ArrayAdapter<AppData> {
    private Activity myContext;
    private AppData[] datas;
    public Bitmap bmp = null;
    public URL url = null;

    private Typeface typeface      = null;
    private Typeface typeface_b    = null;
    private Typeface typeface_m    = null;

    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    public AppItemAdapter(Context context, int textViewResourceId, AppData[] objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
    }

    public View getView(int position1, View convertView, ViewGroup parent) {

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.postitem, null);

        final int position = position1;

        final ImageView thumbImageView = (ImageView) rowView.findViewById(R.id.postThumb);

        if (datas[position].appImageUrl == "") {
            thumbImageView.setImageResource(R.drawable.set_icon);
        } else {
            try {
                final Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        ImageStorage.imageArr[position] = bmp;
                        thumbImageView.setImageBitmap(bmp);
                    }
                };

                new Thread() {
                    public void run() {
                        try {
                            url = new URL(datas[position].appImageUrl);
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        typeface = Typeface.createFromAsset(myContext.getAssets(), TYPEFACE_NAME);
        typeface_m = Typeface.createFromAsset(myContext.getAssets(), TYPEFACE_NAME_MID);
        typeface_b = Typeface.createFromAsset(myContext.getAssets(), TYPEFACE_NAME_BOLD);

        TextView postTitleView = (TextView) rowView.findViewById(R.id.tv_notice_title);
        postTitleView.setText(datas[position].appName);
        postTitleView.setTypeface(typeface_b);

        TextView isInstalledView = (TextView) rowView.findViewById(R.id.isSetup_tv);
        TextView postPointView = (TextView) rowView.findViewById(R.id.app_point_tv);
        isInstalledView.setTypeface(typeface_m);
        postPointView.setTypeface(typeface_b);

        if(datas[position].appIsInstalled) {
            isInstalledView.setText("설치됨");
            isInstalledView.setTextColor(myContext.getResources().getColor(R.color.colorLoginButton));

            postPointView.setText("");
        } else {
            isInstalledView.setText("미설치");
            isInstalledView.setTextColor(Color.parseColor("#bdccb8"));

            postPointView.setText(datas[position].appPoint + "P");
        }
        return rowView;
    }
}

