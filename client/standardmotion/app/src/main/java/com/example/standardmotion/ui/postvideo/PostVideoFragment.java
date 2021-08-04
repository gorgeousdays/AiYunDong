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

import com.example.standardmotion.MainActivity;
import com.example.standardmotion.R;
import com.example.standardmotion.ui.mymessage.MyMessageViewModel;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.File;
import java.net.URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PostVideoFragment extends Fragment{

    private PostVideoViewModel postVideoViewModel;
    public static final String TAG = PostVideoFragment.class.getName();
    public  final static int VEDIO_KU = 101;
    private String path = "";//文件路径
    private ProgressBar post_progress;
    private TextView post_text;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postVideoViewModel =
                ViewModelProviders.of(this).get(PostVideoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_post_video, container, false);
//        final TextView textView = root.findViewById(R.id.text_post_video);
//        postVideoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("视频上传");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText video_name = (EditText)getActivity().findViewById(R.id.upload_video_name);
        post_progress = (ProgressBar) getActivity().findViewById(R.id.post_progress);
        post_text = (TextView) getActivity().findViewById(R.id.post_text);
        getActivity().findViewById(R.id.video_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleteVedio();
                video_name.setText(path);
            }
        });
        getActivity().findViewById(R.id.video_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(VideoUploadActivity.this, "路径："+basePath, Toast.LENGTH_LONG).show();
                if(path.equals(""))
                    Toast.makeText(getActivity(), "请选择视频后，再点击上传！", Toast.LENGTH_LONG).show();
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);

                    File file = new File(path);
//                    GetFileMessage getfile=new GetFileMessage();
//                    getfile.initView(path);
//                    File[] files=file.listFiles();
//                    if(files!=null && file.length()!=0){
//                        Log.d("files","not null");
//                    }
//                    if(files==null){
//                        Log.d("files","null");
//                    }
//                    if(file==null){
//                        Log.d("file","null");
//                    }
                    String postUrl = "http://192.168.43.177:5000/api/uploadandjudge";
                    Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();

                    HttpUtil.postFile(postUrl, new ProgressListener() {
                        @Override
                        public void onProgress(long currentBytes, long contentLength, boolean done) {
                            Log.i(TAG, "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                            int progress = (int) (currentBytes * 100 / contentLength);
                            post_progress.setProgress(progress);
                            post_text.setText(progress + "%");
                        }
                    }, new Callback() {
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
                    }, file);

                }

            }
        });
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void seleteVedio() {
            // 启动相册
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("video/*");
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent,PostVideoFragment.VEDIO_KU);
    }

    /**
     * 选择回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 获取视频
            case PostVideoFragment.VEDIO_KU:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        Uri uri = data.getData();
                        uri = geturi(getActivity(), data);
                        File file = null;
                        if (uri.toString().indexOf("file") == 0) {
                            file = new File(new URI(uri.toString()));
                            path = file.getPath();
                        } else {
                            path = getPath(uri);
                            file = new File(path);
//                            Log.d("Path is","test1");
//                            Log.d("Path is","test1");
                        }
                        if (!file.exists()) {
                            break;
                        }
                        if (file.length() > 200 * 1024 * 1024) {
//                        if (file.length() > 100 * 1024 * 1024) {
//                            "文件大于100M";
                            break;
                        }
                        //视频播放
//                        mVideoView.setVideoURI(uri);
//                        mVideoView.start();
                        //开始上传视频，
//                        submitVedio();
                        Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        String a = e + "";
                    } catch (OutOfMemoryError e) {
                        String a = e + "";
                    }
                }
                break;
        }

    }

    public static Uri geturi(Context context, android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                        Log.i("urishi", uri.toString());
                    }
                }
            }
        }
        return uri;
    }

    private String getPath(Uri uri) {
//        String[] projection = {MediaStore.Video.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//        cursor.moveToFirst();
//        Log.d("Path","hello");
//        Log.d("Path",cursor.getString(column_index));
//        return cursor.getString(column_index);
        String thePath = null;
        // 通过 Uri 和 selection 来获取真实的图片路径
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri,projection , null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                thePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            }
            cursor.close();
        }
        return thePath;
    }
}
