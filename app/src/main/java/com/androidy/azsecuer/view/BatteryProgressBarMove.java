package com.androidy.azsecuer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ljh on 2016/8/15.
 */
public class BatteryProgressBarMove extends ProgressBar {
    public BatteryProgressBarMove(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgressMoved(final int targetProgress){
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            private  int moveStated = getProgress()>targetProgress?1:2;
            @Override
            public void run() {
                switch (moveStated){
                    case 1:
                        setProgress(getProgress()-1);
                        break;
                    case 2:
                        setProgress(getProgress()+1);
                        break;
                }
                if(getProgress()==targetProgress)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask,50,50);

    }
}
