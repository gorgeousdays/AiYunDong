package com.example.standardmotion.ui.sportnews;

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

public class SportNewsFragment extends Fragment {
    private SportNewsViewModel sportNewsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sportNewsViewModel = ViewModelProviders.of(this).get(SportNewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sport_news, container, false);
        final TextView textView = root.findViewById(R.id.text_sport_news);
        sportNewsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
