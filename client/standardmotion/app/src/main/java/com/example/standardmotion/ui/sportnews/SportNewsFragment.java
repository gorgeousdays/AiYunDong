package com.example.standardmotion.ui.sportnews;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.MainActivity;
import com.example.standardmotion.R;
import com.example.standardmotion.ui.sportnews.gson.News;
import com.example.standardmotion.ui.sportnews.gson.NewsList;
import com.example.standardmotion.ui.sportnews.util.HttpUtil;
import com.example.standardmotion.ui.sportnews.util.Utility;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SportNewsFragment extends Fragment {
    private SportNewsViewModel sportNewsViewModel;

    private List<Title> titleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sportNewsViewModel = ViewModelProviders.of(this).get(SportNewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sport_news, container, false);
//        final TextView textView = root.findViewById(R.id.text_sport_news);
//        sportNewsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.tool_bar);

        refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView)getActivity().findViewById(R.id.list_view);
        adapter = new TitleAdapter(getActivity(),R.layout.list_view_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(getActivity(), ContentActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(view==null){
//                    Log.d("NULL","VIEW");
//                }else{
//                    Log.d("NOT NULL","VIEW");
//                }
                Title title = titleList.get(position);
                intent.putExtra("title",title.getTitle());
                intent.putExtra("uri",title.getUri());
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestNew("http://api.drinkmilker.com/api/getmessage/news");
            }
        });
        requestNew("http://api.drinkmilker.com/api/getmessage/news");

        //设置滑动栏项目
        TextView chinning=(TextView) getActivity().findViewById(R.id.chinning);
        TextView swimming=(TextView)getActivity().findViewById(R.id.swimming);
        TextView solidball=(TextView)getActivity().findViewById(R.id.solidball);
        TextView petest=(TextView)getActivity().findViewById(R.id.petest);
        TextView running=(TextView)getActivity().findViewById(R.id.running);
        TextView jumping=(TextView)getActivity().findViewById(R.id.jumping);

        chinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNew("http://api.drinkmilker.com/api/getmessage/chinning");
            }
        });
        swimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNew("http://api.drinkmilker.com/api/getmessage/swimming");
            }
        });
        solidball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNew("http://api.drinkmilker.com/api/getmessage/solidball");
            }
        });
        petest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNew("http://api.drinkmilker.com/api/getmessage/petest");
            }
        });
        running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNew("http://api.drinkmilker.com/api/getmessage/running");
            }
        });
        jumping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNew("http://api.drinkmilker.com/api/getmessage/jumping");
            }
        });
    }
    public void requestNew(String address){
        // 根据返回到的 URL 链接进行申请和返回数据
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
                final NewsList newlist = Utility.parseJsonWithGson(responseText);
                final int code = newlist.code;
                final String msg = newlist.msg;
                if (code == 200){
                    titleList.clear();
                    for (News news:newlist.newsList){
                        Title title = new Title(news.title,news.description,news.picUrl, news.url);
                        titleList.add(title);
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
