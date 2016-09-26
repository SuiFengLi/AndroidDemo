package com.androidy.azsecuer.activity.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidy.azsecuer.R;


/**
 * Created by ljh on 2016/8/4.
 */
public abstract class BaseActionBarActivity extends BaseActivity {
    protected ImageView iv_action_bar_left_icon,iv_action_bar_right_icon;
    protected TextView tv_action_bar_title;

    /*
        绑定include_home_actionbar.xml中的控件
     */
        private void findActionBarView() throws NotFoundActionBarException{
            iv_action_bar_left_icon=(ImageView)findViewById(R.id.iv_action_bar_left_icon);
            iv_action_bar_right_icon=(ImageView)findViewById(R.id.iv_action_bar_right_icon);
            tv_action_bar_title=(TextView)findViewById(R.id.tv_action_bar_title);
        }
    /*
        可能会产生没有ActionBar的异常类
     */
       class NotFoundActionBarException extends Exception{
           public NotFoundActionBarException(){
               super("是否有ActionBar？这里没有找到");
           }
       }
    protected void setActionBar(int actionBarLeftIconId,int actionBarRightIconId,String actionBarTitle){
        try {
            findActionBarView();
        } catch (NotFoundActionBarException e) {
            e.printStackTrace();
        }
        if(actionBarLeftIconId==-1){
            iv_action_bar_left_icon.setVisibility(View.INVISIBLE);
        }else{
            iv_action_bar_left_icon.setImageResource(actionBarLeftIconId);
            iv_action_bar_left_icon.setOnClickListener(this);
        }
        if(actionBarRightIconId==-1){
            iv_action_bar_right_icon.setVisibility(View.INVISIBLE);
        }else {
            iv_action_bar_right_icon.setImageResource(actionBarRightIconId);
            iv_action_bar_right_icon.setOnClickListener(this);
        }
        if(actionBarTitle==null){
            tv_action_bar_title.setVisibility(View.INVISIBLE);
        }else{
            tv_action_bar_title.setText(actionBarTitle);
        }

    }

    protected void setActionBarBack(String actionBarTitle){
        setActionBar(R.drawable.btn_homeasup_default,-1,actionBarTitle);
    }
    protected void setActionBarHome(String actionBarTitle){
        setActionBar(R.drawable.ic_launcher,R.drawable.ic_child_configs,actionBarTitle);
    }



}
