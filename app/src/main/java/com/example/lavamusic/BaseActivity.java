package com.example.lavamusic;
//@author 石同尘 中央民族大学 计算机科学与技术
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    //bundle传输正确，加载活动布局，初始化控件，初始化数据
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!initArgs(getIntent().getExtras())){
            finish();
        }
        setContentView(getLayoutResId());
        initView();
        initData();
    }

    //参数bundle传输正确，返回true，否则返回fasle
    protected boolean initArgs(Bundle bundle){
        return true;
    }
    //获取布局资源id
    protected abstract int getLayoutResId();
    //初始化控件
    protected void initView(){
        ButterKnife.bind(this);
    }
    //初始化数据
    protected void initData(){

    }
}
