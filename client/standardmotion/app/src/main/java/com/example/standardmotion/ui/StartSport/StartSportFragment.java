package com.example.standardmotion.ui.StartSport;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.R;
import com.example.standardmotion.ui.postvideo.PostVideoFragment;
import com.example.standardmotion.ui.postvideo.ProgressRequestBody;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StartSportFragment extends Fragment {
    private  StartSportViewModel startSportViewModel;

    private static final String TAG = StartSportFragment.class.getName();;
    private SurfaceView surfaceView;
    private String videofullpath="";
    private Button uploadButton;
    private CameraUtils cameraUtils;
    private String path, name;
    private ImageView btn;
    private ImageView camera;
    private ImageView change;
    int x = 0;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS).build();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        startSportViewModel = ViewModelProviders.of(this).get(StartSportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_start_sport, container, false);
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


        btn = (ImageView) getActivity().findViewById(R.id.take_video);
        camera = (ImageView) getActivity().findViewById(R.id.take_photo);
        change = (ImageView) getActivity().findViewById(R.id.change_camera);
        surfaceView = (SurfaceView) getActivity().findViewById(R.id.surfaceView);
        uploadButton=(Button)getActivity().findViewById(R.id.video_upload_while_taking);

        cameraUtils = new CameraUtils();
        cameraUtils.create(surfaceView, getActivity());
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        name = "Video";
        Log.d("path",path);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videofullpath!=""){
                    Log.d("The path is",videofullpath);

                    //上传视频 采用okhttp 参照postvide中
                    /***
                     * okHTTP上传视频
                     * 获取PictureSize的大小(控制在w：1000-2000之间)
                     */
                    File file=new File(videofullpath);
                    String postUrl = "http://192.168.43.177:5000/api/upload";

                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    Log.i("test","files[0].getName()=="+file.toString());
                    builder.addFormDataPart("myfile",file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),file));
                    MultipartBody multipartBody = builder.build();
                    Request request  = new Request.Builder().url(postUrl).post(multipartBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("Post Video","Failure");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response != null) {
                                String result = response.body().string();
                                Log.i(TAG, "result===" + result);
                            }
                        }
                    });
                    //
                }else{
                    Log.d("No","Video");
                    Toast.makeText(getActivity(),"请先拍摄视频",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x == 0) {
                    // cameraUtils.changeCamera();
                    btn.setImageResource(R.drawable.video);
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    name=timeStamp;
                    videofullpath=cameraUtils.startRecord(path, name);
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
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                String picname=timeStamp+".png";
                cameraUtils.takePicture(path, picname);
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
