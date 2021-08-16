package com.example.standardmotion.ui.mymessage.score;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.gson.ResultList;
import com.example.standardmotion.ui.mymessage.result.Result;
import com.example.standardmotion.ui.mymessage.util.HttpUtil;
import com.example.standardmotion.ui.mymessage.util.Utility;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyMessageFragmentScore extends Fragment {
    private MyMessageViewModelScore myMessageViewModelScore;
    private TextView Score;
    private Double scoreNum=0.0;

    private List<Result> resultList = new ArrayList<Result>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myMessageViewModelScore =
                ViewModelProviders.of(this).get(MyMessageViewModelScore.class);
        View root = inflater.inflate(R.layout.fragment_my_message_score, container, false);
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
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Score=(TextView)getActivity().findViewById(R.id.score);
        try {
            TimeUnit.MILLISECONDS.sleep(300);//线程问题 休眠100ms 不然ScoreNum会是0
        }
        catch (InterruptedException ex){
            Log.d("Error","error");
        }
        //为了变成两位小数
        DecimalFormat df=new DecimalFormat(".##");
        String ScoreNum_String=df.format(scoreNum);

        Score.setText(ScoreNum_String);
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
                    resultList.clear();
                    for (Result result:resultlist.resultList){
                        resultList.add(result);
                        Log.d("result",result.getEnergy());
                        scoreNum+= Double.parseDouble(result.getEnergy());
                    }
                    Log.d("score",scoreNum.toString());

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
}

