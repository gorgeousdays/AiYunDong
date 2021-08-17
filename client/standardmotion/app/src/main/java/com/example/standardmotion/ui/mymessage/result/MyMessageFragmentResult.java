package com.example.standardmotion.ui.mymessage.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.gson.ResultList;
import com.example.standardmotion.ui.mymessage.util.HttpUtil;
import com.example.standardmotion.ui.mymessage.util.Utility;
import com.example.standardmotion.ui.sportnews.Title;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyMessageFragmentResult extends Fragment {
    private MyMessageViewModelResult myMessageViewModelResult;

    private List<Result> resultList = new ArrayList<Result>();
    private ResultAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myMessageViewModelResult =
                ViewModelProviders.of(this).get(MyMessageViewModelResult.class);
        View root = inflater.inflate(R.layout.fragment_my_message_result, container, false);
//        final TextView textView = root.findViewById(R.id.text_my_message);
//        myMessageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_layout_result);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView)getActivity().findViewById(R.id.list_view_result);
        adapter = new ResultAdapter(getActivity(),R.layout.list_view_result_item, resultList);
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestResult();
            }
        });
        requestResult();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            Intent intent = new Intent(getActivity(), ResultContentActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(view==null){
//                    Log.d("NULL","VIEW");
//                }else{
//                    Log.d("NOT NULL","VIEW");
//                }
                Result result = resultList.get(position);
                intent.putExtra("id",result.getId());
                intent.putExtra("advice",result.getAdvice());
                intent.putExtra("time",result.getTime());
                intent.putExtra("score",result.getEnergy());
                intent.putExtra("imgurl",result.getImgurl());
                intent.putExtra("num",result.getNum());
                startActivity(intent);
            }
        });
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
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                        };
                    });
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "数据错误返回",Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }

        });
    }
}
