package com.example.standardmotion.ui.mymessage.result;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.standardmotion.R;


public class ResultContentActivity extends AppCompatActivity {
    private TextView sport_id;
    private TextView sport_time;
    private TextView sport_num;
    private TextView sport_advice;
    private TextView sport_score;
    private TextView sport_now_pic_text;
    private ImageView sport_now_pic;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_result);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        sport_id=(TextView)findViewById(R.id.sport_id);
        sport_time=(TextView)findViewById(R.id.sport_time);
        sport_advice=(TextView)findViewById(R.id.sport_advice);
        sport_now_pic_text=(TextView)findViewById(R.id.sport_now_pic_text);
        sport_num=(TextView)findViewById(R.id.sport_num);
        sport_score=(TextView)findViewById(R.id.sport_score);
        sport_now_pic=(ImageView)findViewById(R.id.sport_now_pic);


        String id=getIntent().getStringExtra("id");
        String time=getIntent().getStringExtra("time");
        String advice=getIntent().getStringExtra("advice");
        String num=getIntent().getStringExtra("num");
        String score=getIntent().getStringExtra("score");
        String imgurl=getIntent().getStringExtra("imgurl");


        sport_id.setText(id);
        sport_time.setText(time);
        sport_advice.setText(advice);
        sport_num.setText(num);
        sport_score.setText(score);

        String imageUrl="http://api.drinkmilker.com/image/"+imgurl;
        //String imageUrl="testException";
        //Log.d("url",result.getImgurl());

        Glide.with(this).load(imageUrl).into(sport_now_pic);
        sport_now_pic_text.setText("运动图片");
    }
}
