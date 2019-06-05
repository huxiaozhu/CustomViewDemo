package com.liuxiaozhu.testshader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Author：Created by liuxiaozhu on 2018/1/19.
 * Email: chenhuixueba@163.com
 * 跑马灯文字
 */

public class LinearGradientTextView extends AppCompatTextView {
    private TextPaint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mMatrix;
    private float mTranslate;
    private float DELTAX = 20f;

    public LinearGradientTextView(Context context) {
        super(context);
    }

    public LinearGradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint = getPaint();
        String text = getText().toString();
        float textWidth = mPaint.measureText(text);
        // 3个文字的宽度
        int gradientSize = (int) (textWidth / text.length() * 3);

        mLinearGradient = new LinearGradient(-gradientSize,0,0,0,//起始点跟结束点
                new int[]{0xffff0000, 0xff00ff00, 0xff0000ff},//渐变色
                new float[]{0f,0.5f,1f}, Shader.TileMode.MIRROR);//边缘色填充
        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTranslate += DELTAX;
        float textWidth = getPaint().measureText(getText().toString());
        if (mTranslate > textWidth + 1 || mTranslate < 1) {
            DELTAX = -DELTAX;
        }
        mMatrix = new Matrix();
        mMatrix.setTranslate(mTranslate, 0);
        mLinearGradient.setLocalMatrix(mMatrix);
        postInvalidateDelayed(50);
    }
}
