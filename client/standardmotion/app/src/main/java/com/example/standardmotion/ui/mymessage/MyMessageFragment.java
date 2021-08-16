package com.example.standardmotion.ui.mymessage;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.standardmotion.ui.mymessage.result.MyMessageFragmentResult;

public class MyMessageFragment extends Fragment {
    private MyMessageViewModel myMessageViewModel;
    private TextView jumptoresultlist;


    private FragmentManager manager;
    private FragmentTransaction ft;



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
        manager = getFragmentManager();

        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jumptoresultlist = (TextView) getActivity().findViewById(R.id.jumptoresultlist);
        jumptoresultlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                MyMessageFragmentResult myJDEditFragment = new MyMessageFragmentResult();
                ft = manager.beginTransaction();
//当前的fragment会被myJDEditFragment替换
                ft.replace(R.id.nav_host_fragment, myJDEditFragment);
                ft.addToBackStack(null);
                ft.commit();
//                 MainActivity  mainActivity = (MainActivity) getActivity();
//                 mainActivity. gotoresultFragment ();
            }
        });
    }
}
