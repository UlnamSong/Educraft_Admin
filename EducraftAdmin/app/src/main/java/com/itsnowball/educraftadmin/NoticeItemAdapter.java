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

public class NoticeItemAdapter extends ArrayAdapter<NoticeData> {
    private Activity myContext;
    private NoticeData[] datas;

    private Typeface typeface_m    = null;

    private static final String TYPEFACE_NAME      = "fonts/NanumSquareOTFRegular.otf";
    private static final String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFExtraBold.otf";
    private static final String TYPEFACE_NAME_MID  = "fonts/NanumSquareOTFBold.otf";

    public NoticeItemAdapter(Context context, int textViewResourceId, NoticeData[] objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
    }

    public View getView(int position1, View convertView, ViewGroup parent) {

        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.noticeitem, null);

        final int position = position1;
        typeface_m = Typeface.createFromAsset(myContext.getAssets(), TYPEFACE_NAME_MID);

        TextView postTitleView = (TextView) rowView.findViewById(R.id.tv_notice_title);
        postTitleView.setText(datas[position].title);
        postTitleView.setTypeface(typeface_m);

        return rowView;
    }
}

