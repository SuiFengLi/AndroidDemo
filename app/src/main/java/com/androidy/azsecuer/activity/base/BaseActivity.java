package com.androidy.azsecuer.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * Created by ljh on 2016/8/3.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public abstract void initView();
    protected void startActivity(Class<?> targetClass){
        Intent intent = new Intent(this,targetClass);
        startActivity(intent);
    }
    protected void startActivity(Class<?> targetClass,Bundle bundle){
        Intent intent = new Intent(this,targetClass);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}
