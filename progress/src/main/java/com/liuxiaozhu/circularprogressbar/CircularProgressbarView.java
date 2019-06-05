package com.liuxiaozhu.circularprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.View;

/**
 * Author：Created by liuxiaozhu on 2018/1/17.
 * Email: chenhuixueba@163.com
 * 绘制圆形进度条（环形和扇形）
 */

public class CircularProgressbarView extends View {
    private Paint mPaint;
    //进度的最大值
    private int max;
    //    进度条背景的颜色
    private int roundColor;
    //进度条颜色
    private int roundProgressColor;
    //字体的颜色
    private int textColor;
    //字体的大小
    private float textSize;
    //进度条的宽度
    private float roundWidth;
    //是否显示字体
    private boolean textShow;
    //进度值
    private int progress;
    //进度条的样式  0：环形  1：扇形
    public int mStyle;

    public CircularProgressbarView(Context context) {
        this(context, null);
    }

    public CircularProgressbarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void initData(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        //获取自定义的属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressbarView);
        max = typedArray.getInteger(R.styleable.CircularProgressbarView_max, 100);
        roundColor = typedArray.getColor(R.styleable.CircularProgressbarView_roundBackgroundColor, Color.RED);
        roundProgressColor = typedArray.getColor(R.styleable.CircularProgressbarView_roundProgressColor, Color.BLUE);
        textColor = typedArray.getColor(R.styleable.CircularProgressbarView_textColor, Color.BLACK);
        textSize = typedArray.getDimension(R.styleable.CircularProgressbarView_textSize, 55);
        roundWidth = typedArray.getDimension(R.styleable.CircularProgressbarView_textSize, 10);
        textShow = typedArray.getBoolean(R.styleable.CircularProgressbarView_textSize, true);
        mStyle = typedArray.getInt(R.styleable.CircularProgressbarView_style, 0);
        //回收
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景圆环（圆）
        int center = getWidth() / 2;

        float radius = center - roundWidth / 2;
        mPaint.setColor(roundColor);
        if (mStyle == 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(roundWidth); // 圆环的宽度
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }
        mPaint.setAntiAlias(true);
        canvas.drawCircle(center,center,radius,mPaint);

        // 画圆弧（扇形）
        RectF oval = new RectF(center - radius, center - radius,
                center + radius, center + radius);
        mPaint.setColor(roundProgressColor);
        if (mStyle == 0) {
            mPaint.setStrokeWidth(roundWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawArc(oval, 0 , 360 * progress / max, false, mPaint);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(oval, 0 , 360 * progress / max, true, mPaint);
        }

        // 画进度文字
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(0);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT);
        int percent = (int)(progress / (float)max * 100);
        String strPercent = percent + "%";
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        if(percent != 0){
            canvas.drawText(strPercent, getWidth() / 2 - mPaint.measureText(strPercent) / 2 ,
                    getWidth() / 2  +(fm.bottom - fm.top)/2 - fm.bottom, mPaint);
        }
    }

    /**
     * 设置进度条
     * @param progress
     */
    public void setProgress(int progress) {
        if(progress < 0 ){
            throw new IllegalArgumentException("进度Progress不能小于0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }
}
