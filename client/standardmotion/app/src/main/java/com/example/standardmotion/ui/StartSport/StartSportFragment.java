package com.example.standardmotion.ui.StartSport;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.postvideo.PostVideoFragment;

public class StartSportFragment extends Fragment {
    private  StartSportViewModel startSportViewModel;

    private static final String TAG = StartSportFragment.class.getName();;
    private SurfaceView surfaceView;
    private CameraUtils cameraUtils;
    private String path, name;
    private ImageView btn;
    private ImageView camera;
    private ImageView change;
    int x = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        startSportViewModel = ViewModelProviders.of(this).get(StartSportViewModel.class);
        View root = inflater.inflate(R.layout.test, container, false);
//        final TextView textView = root.findViewById(R.id.text_start_sport);
//        startSportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        Log.d(TAG, "onCreate: ");


        btn = (ImageView) getActivity().findViewById(R.id.btn);
        camera = (ImageView) getActivity().findViewById(R.id.camera);
        change = (ImageView) getActivity().findViewById(R.id.change);
        surfaceView = (SurfaceView) getActivity().findViewById(R.id.surfaceView);

        cameraUtils = new CameraUtils();
        cameraUtils.create(surfaceView, getActivity());
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        name = "Video";
        Log.d("path",path);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x == 0) {
                    // cameraUtils.changeCamera();
                    btn.setImageResource(R.drawable.video);
                    cameraUtils.startRecord(path, name);
                    x = 1;
                } else if (x == 1) {
                    cameraUtils.stopRecord();
                    btn.setImageResource(R.drawable.video1);
                    x = 0;
                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.takePicture(path, "name.png");
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.changeCamera();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        cameraUtils.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        cameraUtils.destroy();
    }
}
