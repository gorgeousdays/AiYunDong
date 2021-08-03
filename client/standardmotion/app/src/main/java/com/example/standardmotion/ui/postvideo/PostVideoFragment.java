package com.example.standardmotion.ui.postvideo;

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
import com.example.standardmotion.ui.mymessage.MyMessageViewModel;

public class PostVideoFragment extends Fragment {
    private PostVideoViewModel postVideoViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postVideoViewModel =
                ViewModelProviders.of(this).get(PostVideoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_post_video, container, false);
        final TextView textView = root.findViewById(R.id.text_post_video);
        postVideoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
