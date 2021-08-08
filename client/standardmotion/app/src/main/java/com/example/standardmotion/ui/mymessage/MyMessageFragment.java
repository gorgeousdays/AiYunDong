package com.example.standardmotion.ui.mymessage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.gson.ResultList;
import com.example.standardmotion.ui.mymessage.util.HttpUtil;
import com.example.standardmotion.ui.mymessage.util.Utility;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyMessageFragment extends Fragment {
    private MyMessageViewModel myMessageViewModel;

    private List<Result> resultList = new ArrayList<Result>();
    private ResultAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myMessageViewModel =
                ViewModelProviders.of(this).get(MyMessageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_message, container, false);
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
    }

    private void requestResult() {
        String address = "http://192.168.43.177:5000/api/getmessage/result";
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
