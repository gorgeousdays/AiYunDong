package com.example.standardmotion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
//TODO StartSport中的postUrl需要跟进实际情况更改
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_sport_news, R.id.navigation_start_sport, R.id.navigation_post_video,R.id.navigation_my_message)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getPermission();
    }
    /** 获取权限*/
    private void getPermission() {
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},50);
            }else {
                //说明已经获取到摄像头权限了
                Log.i("MainActivity","已经获取了权限");
            }
        }else {
//这个说明系统版本在6.0之下，不需要动态获取权限。
            Log.i("MainActivity","这个说明系统版本在6.0之下，不需要动态获取权限。");
        }
    }

}