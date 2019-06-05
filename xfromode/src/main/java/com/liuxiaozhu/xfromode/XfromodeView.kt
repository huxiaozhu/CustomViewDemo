package com.liuxiaozhu.xfromode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Xfermode
import android.os.Build
import android.util.AttributeSet
import android.view.View

/**
 * Author：Created by liuxiaozhu on 2018/1/20.
 * Email: chenhuixueba@163.com
 * Xfermode混合效果图(跟Google原图不太一样，没有处理)
 */

class XfromodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var mPaint: Paint
    private var mItemSize = 0f
    private var mItemHorizontalOffset = 0f
    private var mItemVerticalOffset = 0f
    private var mCircleRadius = 0f
    private var mRectSize = 0f
    private var mCircleColor = Color.RED//黄色
    private var mRectColor = Color.BLUE//蓝色
    private var mTextSize = 25f

    init {
        //view被绘制到一个bitmap中
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textSize = mTextSize
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186)

        val canvasWidth = width
        val canvasHeight = height

        for (row in 0..4) {
            for (column in 0 until if (row == 4) 2 else 4) {
                val layer =
                    canvas.saveLayer(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
                mPaint.xfermode = null
                val index = row * 4 + column
                val translateX = (mItemSize + mItemHorizontalOffset) * column
                val translateY = (mItemSize + mItemVerticalOffset) * row
                canvas.translate(translateX, translateY)
                //画文字
                val text = sLabels[index]
                mPaint.color = Color.BLACK
                val textXOffset = mItemSize / 2
                val textYOffset = mTextSize + (mItemVerticalOffset - mTextSize) / 2
                canvas.drawText(text, textXOffset, textYOffset, mPaint)
                canvas.translate(0f, mItemVerticalOffset)
                //画边框
                mPaint.style = Paint.Style.STROKE
                mPaint.color = -0x1000000
                canvas.drawRect(2f, 2f, mItemSize - 2, mItemSize - 2, mPaint)
                mPaint.style = Paint.Style.FILL
                //画圆(目标图片)
                mPaint.color = mCircleColor
                val left = mCircleRadius + 3
                val top = mCircleRadius + 3
                canvas.drawCircle(left, top, mCircleRadius, mPaint)
                mPaint.xfermode = sModes[index]
                //画矩形（源图片）
                mPaint.color = mRectColor
                val rectRight = mCircleRadius + mRectSize
                val rectBottom = mCircleRadius + mRectSize

                canvas.drawRect(left, top, rectRight, rectBottom, mPaint)
                mPaint.xfermode = null
                canvas.restoreToCount(layer)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mItemSize = w / 4.5f
        mItemHorizontalOffset = mItemSize / 6
        mItemVerticalOffset = mItemSize * 0.426f
        mCircleRadius = mItemSize / 3
        mRectSize = mItemSize * 0.6f
    }

    companion object {

        private val sModes = arrayOf<Xfermode>(
            PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            PorterDuffXfermode(PorterDuff.Mode.SRC),
            PorterDuffXfermode(PorterDuff.Mode.DST),
            PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            PorterDuffXfermode(PorterDuff.Mode.XOR),
            PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            PorterDuffXfermode(PorterDuff.Mode.SCREEN),
            PorterDuffXfermode(PorterDuff.Mode.ADD),
            PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
        )

        private val sLabels = arrayOf(
            "Clear",
            "Src",
            "Dst",
            "SrcOver",
            "DstOver",
            "SrcIn",
            "DstIn",
            "SrcOut",
            "DstOut",
            "SrcATop",
            "DstATop",
            "Xor",
            "Darken",
            "Lighten",
            "Multiply",
            "Screen",
            "Add",
            "Overlay"
        )
    }
}
