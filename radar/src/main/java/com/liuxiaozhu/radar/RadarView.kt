package com.liuxiaozhu.radar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View

/**
 * Author：Created by liuxiaozhu on 2018/1/19.
 * Email: chenhuixueba@163.com
 * 自定义实现雷达效果
 */

class RadarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val mSweepGradient: SweepGradient
    private val color = intArrayOf(-0xff0100, -0x7fff0100, 0x0000ff00)
    private var degress: Float = 0F
    private val mPaint: Paint = Paint()
    private val mLinsPaint: Paint

    init {
        mPaint.isAntiAlias = true
        mSweepGradient = SweepGradient(300f, 300f, color, null)
        mLinsPaint = Paint()
        mLinsPaint.strokeWidth = 5f
        mLinsPaint.color = 0xff00000
        mLinsPaint.style = Paint.Style.STROKE
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        degress += 10f
        val matrix = Matrix()
        matrix.setRotate(degress, 300f, 300f)
        mSweepGradient.setLocalMatrix(matrix)
        mPaint.shader = mSweepGradient
        canvas.drawCircle(300f, 300f, 300f, mPaint)
        canvas.drawLine(0f, 300f, 600f, 300f, mLinsPaint)
        canvas.drawLine(300f, 0f, 300f, 600f, mLinsPaint)
        var radio = 300
        while (radio > 50) {
            radio -= 50
            canvas.drawCircle(300f, 300f, radio.toFloat(), mLinsPaint)
        }
        postInvalidateDelayed(50)
    }
}
