package com.liuxiaozhu.circularprogressbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

/**
 * Author：Created by liuxiaozhu on 2018/1/17.
 * Email: chenhuixueba@163.com
 * 绘制圆形进度条（环形和扇形）
 */

class CircularProgressbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    //进度的最大值
    private var max: Int = 0
    //    进度条背景的颜色
    private var roundColor: Int = 0
    //进度条颜色
    private var roundProgressColor: Int = 0
    //字体的颜色
    private var textColor: Int = 0
    //字体的大小
    private var textSize: Float = 0f
    //进度条的宽度
    private var roundWidth: Float = 0.toFloat()
    //是否显示字体
    private var textShow: Boolean = false
    //进度值
    private var progress: Int = 0
    //进度条的样式  0：环形  1：扇形
    var mStyle: Int = 0

    init {
        mPaint = Paint()
        //获取自定义的属性值
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressbarView)
        max = typedArray.getInteger(R.styleable.CircularProgressbarView_max, 100)
        roundColor = typedArray.getColor(R.styleable.CircularProgressbarView_roundBackgroundColor, Color.RED)
        roundProgressColor = typedArray.getColor(R.styleable.CircularProgressbarView_roundProgressColor, Color.BLUE)
        textColor = typedArray.getColor(R.styleable.CircularProgressbarView_textColor, Color.BLACK)
        textSize = typedArray.getDimension(R.styleable.CircularProgressbarView_textSize, 55f)
        roundWidth = typedArray.getDimension(R.styleable.CircularProgressbarView_textSize, 10f)
        textShow = typedArray.getBoolean(R.styleable.CircularProgressbarView_textSize, true)
        mStyle = typedArray.getInt(R.styleable.CircularProgressbarView_style, 0)
        //回收
        typedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画背景圆环（圆）
        val center = width / 2

        val radius = center - roundWidth / 2
        mPaint!!.color = roundColor
        if (mStyle == 0) {
            mPaint!!.style = Paint.Style.STROKE
            mPaint!!.strokeWidth = roundWidth // 圆环的宽度
        } else {
            mPaint!!.style = Paint.Style.FILL
        }
        mPaint!!.isAntiAlias = true
        canvas.drawCircle(center.toFloat(), center.toFloat(), radius, mPaint!!)

        // 画圆弧（扇形）
        val oval = RectF(
            center - radius, center - radius,
            center + radius, center + radius
        )
        mPaint!!.color = roundProgressColor
        if (mStyle == 0) {
            mPaint!!.strokeWidth = roundWidth
            mPaint!!.style = Paint.Style.STROKE
            mPaint!!.strokeCap = Paint.Cap.ROUND
            canvas.drawArc(oval, 0f, (360 * progress / max).toFloat(), false, mPaint!!)
        } else {
            mPaint!!.style = Paint.Style.FILL
            canvas.drawArc(oval, 0f, (360 * progress / max).toFloat(), true, mPaint!!)
        }

        // 画进度文字
        mPaint!!.color = textColor
        mPaint!!.strokeWidth = 0f
        mPaint!!.textSize = textSize
        mPaint!!.typeface = Typeface.DEFAULT
        val percent = (progress / max.toFloat() * 100).toInt()
        val strPercent = "$percent%"
        val fm = mPaint!!.fontMetricsInt
        if (percent != 0) {
            canvas.drawText(
                strPercent, width / 2 - mPaint!!.measureText(strPercent) / 2,
                (width / 2 + (fm.bottom - fm.top) / 2 - fm.bottom).toFloat(), mPaint!!
            )
        }
    }

    /**
     * 设置进度条
     * @param progress
     */
    fun setProgress(progress: Int) {
        var progress = progress
        if (progress < 0) {
            throw IllegalArgumentException("进度Progress不能小于0")
        }
        if (progress > max) {
            progress = max
        }
        if (progress <= max) {
            this.progress = progress
            postInvalidate()
        }
    }
}
