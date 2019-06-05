package com.liuxiaozhu.testshader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Author：Created by liuxiaozhu on 2018/1/19.
 * Email: chenhuixueba@163.com
 * 放大镜效果
 */
class ZoomImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    // 原图
    private val mBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.timg)
    // 放大后的图
    private var mBitmapScale: Bitmap? = null
    // 制作的圆形的图片（放大的局部），盖在Canvas上面
    private val mShapeDrawable: ShapeDrawable

    private val mMatrix: Matrix

    init {
        mBitmapScale = mBitmap
        //放大后的整个图片
        mBitmapScale = Bitmap.createScaledBitmap(
            mBitmapScale!!, mBitmapScale!!.width * FACTOR,
            mBitmapScale!!.height * FACTOR, true
        )
        val bitmapShader = BitmapShader(
            mBitmapScale!!, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )

        mShapeDrawable = ShapeDrawable(OvalShape())
        mShapeDrawable.paint.shader = bitmapShader
        // 切出矩形区域，用来画圆（内切圆）
        mShapeDrawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2)

        mMatrix = Matrix()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1、画原图
        canvas.drawBitmap(mBitmap, 0f, 0f, null)

        // 2、画放大镜的图
        mShapeDrawable.draw(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()

        // 将放大的图片往相反的方向挪动
        mMatrix.setTranslate((RADIUS - x * FACTOR).toFloat(), (RADIUS - y * FACTOR).toFloat())
        mShapeDrawable.paint.shader.setLocalMatrix(mMatrix)
        // 切出手势区域点位置的圆
        mShapeDrawable.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS)
        invalidate()
        return true
    }

    companion object {

        //放大倍数
        private const val FACTOR = 2
        //放大镜的半径
        private const val RADIUS = 100
    }
}
