package com.example.standardmotion.ui.mymessage.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;

public class MyMessageFragmentScore extends Fragment {
    private MyMessageViewModelScore myMessageViewModelScore;
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
        return root;
    }
}

