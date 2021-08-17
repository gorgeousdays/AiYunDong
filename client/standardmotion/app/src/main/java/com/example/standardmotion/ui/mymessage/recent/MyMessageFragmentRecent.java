package com.example.standardmotion.ui.mymessage.recent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.gson.ResultList;
import com.example.standardmotion.ui.mymessage.result.MyMessageViewModelResult;
import com.example.standardmotion.ui.mymessage.result.Result;
import com.example.standardmotion.ui.mymessage.util.HttpUtil;
import com.example.standardmotion.ui.mymessage.util.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyMessageFragmentRecent extends Fragment {
    private MyMessageViewModelRecent myMessageViewModelRecent;
    Result result;

    private TextView sport_id_recent;
    private TextView sport_time_recent;
    private  TextView sport_num_recent;
    private  TextView sport_advice_recent;
    private TextView sport_score_recent;
    private ImageView sport_now_pic_recent;
    private  TextView sport_now_pic_text_recent;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myMessageViewModelRecent =
                ViewModelProviders.of(this).get(MyMessageViewModelRecent.class);
        View root = inflater.inflate(R.layout.fragment_my_message_recent, container, false);
//        final TextView textView = root.findViewById(R.id.text_my_message);
//        myMessageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        requestResult();
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sport_advice_recent=(TextView)getActivity().findViewById(R.id.sport_advice_recent);
        sport_id_recent=(TextView)getActivity().findViewById(R.id.sport_id_recent);
        sport_now_pic_recent=(ImageView)getActivity().findViewById(R.id.sport_now_pic_recent);
        sport_num_recent=(TextView)getActivity().findViewById(R.id.sport_num_recent);
        sport_score_recent=(TextView)getActivity().findViewById(R.id.sport_score_recent);
        sport_time_recent=(TextView)getActivity().findViewById(R.id.sport_time_recent);
        sport_now_pic_text_recent=(TextView)getActivity().findViewById(R.id.sport_now_pic_text_recent);
        try {
            TimeUnit.MILLISECONDS.sleep(1000);//线程问题 休眠100ms 不然ScoreNum会是0
        }
        catch (InterruptedException ex){
            Log.d("Error","error");
        }
        sport_time_recent.setText(result.getTime());
        sport_score_recent.setText(result.getEnergy());
        sport_num_recent.setText(result.getNum());
        sport_id_recent.setText(result.getId());
        sport_advice_recent.setText(result.getAdvice());
        String imageUrl="http://api.drinkmilker.com/image/"+result.getImgurl();
        //String imageUrl="testException";
        //Log.d("url",result.getImgurl());

        Glide.with(getContext()).load(imageUrl).into(sport_now_pic_recent);
        sport_now_pic_text_recent.setText("运动图片");

//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
//            sport_now_pic_recent.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            Log.d("error","error");
//        } catch (IOException e) {
//            Log.d("error","error");
//        }
    }

    private void requestResult() {
        String address = "http://api.drinkmilker.com/api/getmessage/result";
        Log.d("ADDRESS",address);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "内容加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.d("responseText",responseText);
                final ResultList resultlist = Utility.parseJsonWithGson(responseText);
                final String message = resultlist.message;
                Log.d("message:",message);
                if (message.equals("success")){
                    result=resultlist.resultList.get(resultlist.resultList.size()-1);
                    //Log.d("1",result.getId());
                    //Log.d("2",result.getEnergy());
                    //Log.d("3",result.getImgurl());
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "数据错误返回",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

    }

    //通过URL请求图片 采用此方法不可行
    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

}
