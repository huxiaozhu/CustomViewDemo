package com.liuxiaozhu.testshader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * Author：Created by liuxiaozhu on 2018/1/19.
 * Email: chenhuixueba@163.com、
 * 渲染器
 */

public class MyShader extends View {
    private Paint mPaint;
    private Bitmap mBitMap = null;

    private int mWidth;
    private int mHeight;
    private int[] mColors = {Color.RED,Color.BLUE,Color.YELLOW};
    private int degress;


    public MyShader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitMap = ((BitmapDrawable)getResources().getDrawable(R.drawable.timg)).getBitmap();
        mPaint = new Paint();
        mWidth = mBitMap.getWidth();
        mHeight = mBitMap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        //位图渲染
//        drawBitMap(canvas);
        // 线性渲染
//        drawLinearGradient(canvas);
        // 环形渲染
//        drawRadialGradient(canvas);
        // 渐变渲染
//        drawSweepGradient(canvas);
        //组合渲染
        drawComposeShader(canvas);
    }

    /**
     * 组合渲染，给bitmap添加线性效果
     * @param canvas
     */
    private void drawComposeShader(Canvas canvas) {
        //创建BitmapShader，用以绘制心
        BitmapShader bitmapShader = new BitmapShader(mBitMap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //创建LinearGradient，用以产生从左上角到右下角的颜色渐变效果
        LinearGradient linearGradient = new LinearGradient(0, 0, mWidth, mHeight, Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP);
        //bitmapShader对应目标像素，linearGradient对应源像素，像素颜色混合采用MULTIPLY模式
        ComposeShader composeShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY);
        //将组合的composeShader作为画笔paint绘图所使用的shader
        mPaint.setShader(composeShader);
        //用composeShader绘制矩形区域
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
    }

    /**
     *
     * @param canvas
     */
    private void drawSweepGradient(Canvas canvas) {
        degress += 10;
        SweepGradient mSweepGradient = new SweepGradient(300, 300, mColors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(degress,300,300);
        mSweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(mSweepGradient);
        canvas.drawCircle(300, 300, 300, mPaint);
        postInvalidateDelayed(50);
    }

    /**
     * 环形渲染
     * @param canvas
     */
    private void drawRadialGradient(Canvas canvas) {
        RadialGradient mRadialGradient = new RadialGradient(300, 300, 100, mColors, null, Shader.TileMode.REPEAT);
        mPaint.setShader(mRadialGradient);
        canvas.drawCircle(300, 300, 300, mPaint);
    }

    /**
     * 线性填充
     * @param canvas
     */
    private void drawLinearGradient(Canvas canvas) {
        /**线性渐变
         * x0, y0, 起始点
         *  x1, y1, 结束点
         * int[]  mColors, 中间依次要出现的几个颜色
         * float[] positions,数组大小跟colors数组一样大，中间依次摆放的几个颜色分别放置在那个位置上(参考比例从左往右)
         *    tile
         */
        LinearGradient linearGradient = new LinearGradient( 0, 0,800, 800, mColors, null, Shader.TileMode.CLAMP);
        // linearGradient = new LinearGradient(0, 0, 400, 400, mColors, null, Shader.TileMode.REPEAT);
        mPaint.setShader(linearGradient);
        canvas.drawRect(0, 0, 800, 800, mPaint);
    }

    private void drawBitMap(Canvas canvas) {
        /**
         * TileMode.CLAMP 拉伸最后一个像素去铺满剩下的地方
         * TileMode.MIRROR 通过镜像翻转铺满剩下的地方。
         * TileMode.REPEAT 重复图片平铺整个画面（电脑设置壁纸）
         */
        BitmapShader bitMapShader = new BitmapShader(mBitMap, Shader.TileMode.MIRROR,
                Shader.TileMode.MIRROR);
        mPaint.setShader(bitMapShader);
        mPaint.setAntiAlias(true);
        //1.绘制圆形图片
//        canvas.drawCircle(mHeight / 2,mHeight / 2, mHeight / 2 ,mPaint);
        //2.绘制椭圆图片
//        canvas.drawOval(new RectF(0 , 0, mWidth, mHeight),mPaint);
        //3.绘制矩形图片
//        canvas.drawRect(new Rect(0,0 , 1000, 1600),mPaint);


        //4.绘制圆形图片
        //设置像素矩阵，来调整大小，为了解决宽高不一致的问题。
        float scale = Math.max(mWidth,mHeight) / Math.min(mWidth,mHeight);

        Matrix matrix = new Matrix();
        matrix.setScale(scale,scale);
        bitMapShader.setLocalMatrix(matrix);
        //通过shapeDrawable也可以实现
        ShapeDrawable shapeDrawble = new ShapeDrawable(new OvalShape());
        shapeDrawble.getPaint().setShader(bitMapShader);
        shapeDrawble.setBounds(0,0,mWidth,mWidth);
        shapeDrawble.draw(canvas);
    }
}
