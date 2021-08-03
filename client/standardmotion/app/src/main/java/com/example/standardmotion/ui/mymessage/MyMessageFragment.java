package com.example.standardmotion.ui.mymessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;

public class MyMessageFragment extends Fragment {
    private MyMessageViewModel myMessageViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myMessageViewModel =
                ViewModelProviders.of(this).get(MyMessageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_message, container, false);
        final TextView textView = root.findViewById(R.id.text_my_message);
        myMessageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
