package com.liuxiaozhu.testshader

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

/**
 * Author：Created by liuxiaozhu on 2018/1/19.
 * Email: chenhuixueba@163.com
 * 跑马灯文字
 */

class LinearGradientTextView : AppCompatTextView {
    private var mPaint: TextPaint? = null
    private var mLinearGradient: LinearGradient? = null
    private var mMatrix: Matrix? = null
    private var mTranslate: Float = 0.toFloat()
    private var DELTAX = 20f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPaint = paint
        val text = text.toString()
        val textWidth = mPaint!!.measureText(text)
        // 3个文字的宽度
        val gradientSize = (textWidth / text.length * 3).toInt()

        mLinearGradient = LinearGradient(
            (-gradientSize).toFloat(), 0f, 0f, 0f, //起始点跟结束点
            intArrayOf(-0x10000, -0xff0100, -0xffff01), //渐变色
            floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.MIRROR
        )//边缘色填充
        mPaint!!.shader = mLinearGradient
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mTranslate += DELTAX
        val textWidth = paint.measureText(text.toString())
        if (mTranslate > textWidth + 1 || mTranslate < 1) {
            DELTAX = -DELTAX
        }
        mMatrix = Matrix()
        mMatrix!!.setTranslate(mTranslate, 0f)
        mLinearGradient!!.setLocalMatrix(mMatrix)
        postInvalidateDelayed(50)
    }
}
