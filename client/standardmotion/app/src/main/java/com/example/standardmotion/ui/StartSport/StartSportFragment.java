package com.example.standardmotion.ui.StartSport;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.standardmotion.BuildConfig;
import com.example.standardmotion.R;
import com.example.standardmotion.ui.postvideo.PostVideoFragment;
import com.example.standardmotion.ui.postvideo.ProgressRequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

    private String picfilename;
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


        Button sharevideo=(Button)getActivity().findViewById(R.id.share_video);
        sharevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//  Android版本太高 如下操作报错
//                Intent intent =new Intent();
//                ComponentName comp =new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                intent.setComponent(comp);
//                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                intent.setType("image/*");
//                intent.putExtra("Kdescription", "hello world");//这里title 为朋友圈的文字描述
//                ArrayList<Uri> imageUris = new ArrayList();
//
//                ArrayList<File> files=new ArrayList<>();//这里file 类型需要是图片格式,因为发送朋友圈的是图片
//
//                files.add(new File(videofullpath));
//
////files.add(new File(Environment.getExternalStorageDirectory()+File.separator+"abc1201.jpg"));
//                for(File f : files) {
//                    imageUris.add(Uri.fromFile(f));
//                }
//                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//                startActivity(intent);//直接打开朋友圈页面发送朋友圈
                  if(picfilename==null){
                      Toast.makeText(getActivity(),"请先拍摄照片",Toast.LENGTH_SHORT).show();
                  }else{
                      Log.d("pic",picfilename);
                      PlatformUtil.shareWechatMoment(getContext(),picfilename);
                  }

//                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                ComponentName comp =new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                intent.setComponent(comp);
//                intent.setType("*/*");
//                Log.d("path",videofullpath);
//                intent.putExtra("Kdescription", "hello world");
//                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", new File(videofullpath)));
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(intent);
            }
        });


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
                    Toast.makeText(getActivity(), "视频处理中，稍后可在我的界面看到结果", Toast.LENGTH_LONG).show();
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
                    Log.d("video",videofullpath);
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
                picfilename=cameraUtils.takePicture(path, picname);
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
