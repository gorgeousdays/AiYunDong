package com.example.standardmotion.ui.mymessage.recent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.result.MyMessageViewModelResult;

public class MyMessageFragmentRecent extends Fragment {
    private MyMessageViewModelRecent myMessageViewModelRecent;
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
        return root;
    }
}
