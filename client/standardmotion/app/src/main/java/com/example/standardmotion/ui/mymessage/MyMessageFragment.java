package com.example.standardmotion.ui.mymessage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.recent.MyMessageFragmentRecent;
import com.example.standardmotion.ui.mymessage.result.MyMessageFragmentResult;
import com.example.standardmotion.ui.mymessage.score.MyMessageFragmentScore;
import com.example.standardmotion.ui.mymessage.score.MyMessageViewModelScore;

public class MyMessageFragment extends Fragment {
    private MyMessageViewModel myMessageViewModel;
    private TextView jumptoresultlist;
    private  TextView jumptorecent;
    private  TextView jumptoscore;


    private FragmentManager manager;
    private FragmentTransaction ft;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myMessageViewModel =
                ViewModelProviders.of(this).get(MyMessageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_message, container, false);
        manager = getFragmentManager();

        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        jumptoresultlist = (TextView) getActivity().findViewById(R.id.jumptoresultlist);
        jumptoresultlist.setClickable(true);
        jumptoresultlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                MyMessageFragmentResult fragment = new MyMessageFragmentResult();
                ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, fragment);
                //ft.addToBackStack(null);
                ft.commit();
//                 MainActivity  mainActivity = (MainActivity) getActivity();
//                 mainActivity. gotoresultFragment ();
            }
        });
        jumptorecent=(TextView) getActivity().findViewById(R.id.jumptorecent);
        jumptorecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Onclik","click");
                MyMessageFragmentRecent fragment = new MyMessageFragmentRecent();
                ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, fragment);
                //ft.addToBackStack(null);
                ft.commit();
            }
        });
        jumptoscore=(TextView)getActivity().findViewById(R.id.jumptoscore);
        jumptoscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMessageFragmentScore fragment=new MyMessageFragmentScore();
                ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, fragment);
                //ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
