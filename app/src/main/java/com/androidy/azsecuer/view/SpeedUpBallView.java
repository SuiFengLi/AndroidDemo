package com.androidy.azsecuer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ljh on 2016/8/25.
 */
public class SpeedUpBallView extends View {
    private RectF oval = null;
    private Paint paint = null;
    private int startAngle = -90;
    private int sweepAngle = 0;

    public SpeedUpBallView(Context context, AttributeSet attrs) {
        super(context,attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xFFFF8C00);
    }

    /**
     * 开始循环旋转动画
     *
     * @return {@link #MyTimer};结束旋转动画时将使用;{@link MyTimer#endWhileAnim()}
     */
    public MyTimer startWhileAnim() {
        this.sweepAngle = 180;
        final MyTimer timer = new MyTimer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startAngle += 22;
                postInvalidate();
            }
        };
        timer.schedule(timerTask, 40, 40);
        return timer;
    }

    public class MyTimer extends Timer {
        public void endWhileAnim() {
            this.cancel();
            startAngle = -90;
        }
    }

    // 0:当前角度退回到0度; 1:从0度前进到目标角度
    private int moveState = 0;
    // 动画是否运行中
    private boolean isRuning = false;
    // 回退：从慢到快
    private int[] backData = { 4, 4, 4, 8, 8, 8, 8, 12, 12, 16, 16, 16, 22, 22, 24, 28 };
    private int indexb;
    // 前进：从慢到快
    private int[] goData = { 4, 4, 4, 8, 8, 8, 8, 12, 12, 16, 16, 16, 22, 22, 24, 28 };
    private int indexg;

    /**
     * 设置角度带动画(退回到0度后，再前进到目标目标角度)
     *
     * @param targetAngle
     *            目标角度
     */
    public void setAngleWithAnim(final int targetAngle) {
        if (isRuning) {
            return;
        }
        this.isRuning = true;
        this.moveState = 0;
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                switch (moveState) {
                    case 0:// 回退
                        // 角度变化,每次变化数据不一样,表现出“从慢到快”效果
                        sweepAngle -= backData[indexb];
                        indexb++;
                        if (indexb >= backData.length) {
                            indexb = backData.length - 1;
                        }
                        if (sweepAngle <= 0) {
                            sweepAngle = 0;
                            moveState = 1;
                        }
                        break;
                    case 1:// 前进
                    default:
                        // 角度变化,每次变化数据不一样,表现出“从慢到快”效果
                        sweepAngle += goData[indexg];
                        indexg++;
                        if (indexg >= goData.length) {
                            indexg = goData.length - 1;
                        }
                        if (sweepAngle >= targetAngle) {
                            sweepAngle = targetAngle;
                            moveState = 0;
                            isRuning = false;
                            timer.cancel();
                        }
                        break;
                }
                // 重新Draw
                postInvalidate();
            }
        };
        timer.schedule(task, 40, 40); // 1000/24
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 得到测量后的宽和高
        int mesureWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int mesureHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mesureWidth, mesureHeight);
        // 初始实例圆弧大小
        oval = new RectF(0, 0, 0 + mesureWidth, 0 + mesureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(oval, startAngle, sweepAngle, true, paint);
    }

}
