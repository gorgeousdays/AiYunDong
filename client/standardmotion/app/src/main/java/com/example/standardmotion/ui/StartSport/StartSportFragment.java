package com.example.standardmotion.ui.StartSport;

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

public class StartSportFragment extends Fragment {
    private  StartSportViewModel startSportViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        startSportViewModel = ViewModelProviders.of(this).get(StartSportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_start_sport, container, false);
        final TextView textView = root.findViewById(R.id.text_start_sport);
        startSportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
