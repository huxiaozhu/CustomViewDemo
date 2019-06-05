package com.liuxiaozhu.xfromode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * Author：Created by liuxiaozhu on 2018/1/20.
 * Email: chenhuixueba@163.com
 * Xfermode混合效果图(跟Google原图不太一样，没有处理)
 */

public class XfromodeView extends View {
    Paint mPaint;
    float mItemSize = 0;
    float mItemHorizontalOffset = 0;
    float mItemVerticalOffset = 0;
    float mCircleRadius = 0;
    float mRectSize = 0;
    int mCircleColor = Color.RED;//黄色
    int mRectColor = Color.BLUE;//蓝色
    float mTextSize = 25;

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN),
            new PorterDuffXfermode(PorterDuff.Mode.ADD),
            new PorterDuffXfermode(PorterDuff.Mode.OVERLAY),
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen",
            "Add", "Overlay"
    };


    public XfromodeView(Context context) {
        this(context, null);
    }

    public XfromodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfromodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < (row == 4 ? 2 : 4); column++) {
                int layer = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
                mPaint.setXfermode(null);
                int index = row * 4 + column;
                float translateX = (mItemSize + mItemHorizontalOffset) * column;
                float translateY = (mItemSize + mItemVerticalOffset) * row;
                canvas.translate(translateX, translateY);
                //画文字
                String text = sLabels[index];
                mPaint.setColor(Color.BLACK);
                float textXOffset = mItemSize / 2;
                float textYOffset = mTextSize + (mItemVerticalOffset - mTextSize) / 2;
                canvas.drawText(text, textXOffset, textYOffset, mPaint);
                canvas.translate(0, mItemVerticalOffset);
                //画边框
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xff000000);
                canvas.drawRect(2, 2, mItemSize - 2, mItemSize - 2, mPaint);
                mPaint.setStyle(Paint.Style.FILL);
                //画圆(目标图片)
                mPaint.setColor(mCircleColor);
                float left = mCircleRadius + 3;
                float top = mCircleRadius + 3;
                canvas.drawCircle(left, top, mCircleRadius, mPaint);
                mPaint.setXfermode(sModes[index]);
                //画矩形（源图片）
                mPaint.setColor(mRectColor);
                float rectRight = mCircleRadius + mRectSize;
                float rectBottom = mCircleRadius + mRectSize;

                canvas.drawRect(left, top, rectRight, rectBottom, mPaint);
                mPaint.setXfermode(null);
                canvas.restoreToCount(layer);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemSize = w / 4.5f;
        mItemHorizontalOffset = mItemSize / 6;
        mItemVerticalOffset = mItemSize * 0.426f;
        mCircleRadius = mItemSize / 3;
        mRectSize = mItemSize * 0.6f;
    }
}
