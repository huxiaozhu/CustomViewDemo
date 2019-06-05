package com.liuxiaozhu.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * Author：Created by liuxiaozhu on 2018/1/19.
 * Email: chenhuixueba@163.com
 * 自定义实现雷达效果
 */

public class RadarView extends View {
    private SweepGradient mSweepGradient;
    private int[] color = new int[]{0xff00ff00,0x8000ff00,0x0000ff00};
    private float degress;
    private Paint mPaint;
    private Paint mLinsPaint;

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mSweepGradient = new SweepGradient(300,300,color,null);
        mLinsPaint = new Paint();
        mLinsPaint.setStrokeWidth(5);
        mLinsPaint.setColor(0xff00000);
        mLinsPaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        degress += 10;
        Matrix matrix = new Matrix();
        matrix.setRotate(degress,300,300);
        mSweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(mSweepGradient);
        canvas.drawCircle(300, 300, 300, mPaint);
        canvas.drawLine(0,300,600,300,mLinsPaint);
        canvas.drawLine(300,0,300,600,mLinsPaint);
        int radio=300;
        while (radio > 50) {
            radio -= 50;
            canvas.drawCircle(300, 300, radio, mLinsPaint);
        }
        postInvalidateDelayed(50);
    }
}
