package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
 */
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends BaseActivity {
    @Override
    protected void initView() {
        super.initView();
        MyUtil.fillScreen(this);
    }

    @Override
    protected void initData() {
        super.initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,SongListActivity.class));
                finish();
            }
        },2000);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
