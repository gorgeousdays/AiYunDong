package com.example.standardmotion.ui.postvideo;

import android.util.Log;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS).build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public static void postFile(String url, final ProgressListener listener, okhttp3.Callback callback, File file){
        //file =new File("/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/0378dc98308c535044d0dd0d26866ff5/video/2107221446205115.mp4");
        //file= new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_2021-08-02-20-55-18-790_com.tencent.mm.jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //Log.i("test","files[0].getName()=="+file.getName());
        //第一个参数要与Servlet中的一致
        Log.i("test","files[0].getName()=="+file.toString());
        //builder.addFormDataPart("file",file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),file));
        //builder.addFormDataPart("myfile",file.getName(), RequestBody.create(MediaType.parse("mp4/*"),file.toString()));
        builder.addFormDataPart("myfile",file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),file));
        MultipartBody multipartBody = builder.build();

        Request request  = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody,listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


}
