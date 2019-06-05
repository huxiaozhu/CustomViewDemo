package com.liuxiaozhu.minions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Author：Created by liuxiaozhu on 2018/1/18.
 * Email: chenhuixueba@163.com
 */
internal class MinionView : View {
    private var mWidthUnspecified: Int = 0
    private var mHeightUnspecified: Int = 0

    private var mPaint: Paint? = null
    private var mBodyWidth: Float = 0.toFloat()
    private var mBodyHeight: Float = 0.toFloat()

    private var mStrokeWidth = 4f//描边宽度
    private var mOffset: Float = 0.toFloat()//计算时，部分需要 考虑找边偏移
    private var mRadius: Float = 0.toFloat()//身体上下半圆的半径
    private val mColorClothes = Color.rgb(32, 116, 160)//衣服的颜色
    private val mColorBody = Color.rgb(249, 217, 70)//衣服的颜色
    private val mColorStroke = Color.BLACK
    private var mBodyRect: RectF? = null
    private var mHandsHeight: Float = 0.toFloat()//计算出吊带的高度时，可以用来做手的高度
    private var mFootHeight: Float = 0.toFloat()//脚的高度，用来画脚部阴影时用

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false))
    }


    /**
     * @param origin
     * @param isWidth 是否在测量宽
     * @return
     */
    private fun measure(origin: Int, isWidth: Boolean): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(origin)
        val specSize = MeasureSpec.getSize(origin)
        when (specMode) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> {
                result = specSize
                if (isWidth) {
                    mWidthUnspecified = result
                } else {
                    mHeightUnspecified = result
                }
            }
            MeasureSpec.UNSPECIFIED -> {
                result = if (isWidth) {//宽或高未指定的情况下，可以由另一端推算出来 - -如果两边都没指定就用默认值
                    (mHeightUnspecified * BODY_WIDTH_HEIGHT_SCALE).toInt()
                } else {
                    (mWidthUnspecified / BODY_WIDTH_HEIGHT_SCALE).toInt()
                }
                if (result == 0) {
                    result = DEFAULT_SIZE
                }
            }
            else -> {
                result = if (isWidth) {
                    (mHeightUnspecified * BODY_WIDTH_HEIGHT_SCALE).toInt()
                } else {
                    (mWidthUnspecified / BODY_WIDTH_HEIGHT_SCALE).toInt()
                }
                if (result == 0) {
                    result = DEFAULT_SIZE
                }
            }
        }
        return result
    }


    override fun onDraw(canvas: Canvas) {
        initParams()
        initPaint()
        drawFeetShadow(canvas)//脚下的阴影
        drawFeet(canvas)//脚
        drawHands(canvas)//手
        drawBody(canvas)//身体
        drawClothes(canvas)//衣服
        drawEyesMouth(canvas)//眼睛,嘴巴
        drawBodyStroke(canvas)//最后画身体的描边，可以摭住一些过渡的棱角
    }

    /**
     * 初始化参数
     */
    private fun initParams() {
        mBodyWidth = Math.min(width.toFloat(), height * BODY_WIDTH_HEIGHT_SCALE) * BODY_SCALE
        mBodyHeight = Math.min(width.toFloat(), height * BODY_WIDTH_HEIGHT_SCALE) / BODY_WIDTH_HEIGHT_SCALE * BODY_SCALE

        mStrokeWidth = Math.max(mBodyWidth / 50, mStrokeWidth)
        mOffset = mStrokeWidth / 2

        mBodyRect = RectF()
        mBodyRect!!.left = (width - mBodyWidth) / 2
        mBodyRect!!.top = (height - mBodyHeight) / 2
        mBodyRect!!.right = mBodyRect!!.left + mBodyWidth
        mBodyRect!!.bottom = mBodyRect!!.top + mBodyHeight

        mRadius = mBodyWidth / 2
        mFootHeight = mRadius * 0.4333f

        mHandsHeight = (height + mBodyHeight) / 2 + mOffset - mRadius * 1.65f

    }

    private fun drawBody(canvas: Canvas) {

        initPaint()
        mPaint!!.color = mColorBody
        mPaint!!.style = Paint.Style.FILL

        canvas.drawRoundRect(mBodyRect!!, mRadius, mRadius, mPaint!!)

    }

    private fun drawBodyStroke(canvas: Canvas) {
        initPaint()
        mPaint!!.color = mColorStroke
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.STROKE
        canvas.drawRoundRect(mBodyRect!!, mRadius, mRadius, mPaint!!)
    }

    private fun drawClothes(canvas: Canvas) {
        initPaint()

        val rect = RectF()

        rect.left = (width - mBodyWidth) / 2 + mOffset
        rect.top = (height + mBodyHeight) / 2 - mRadius * 2 + mOffset
        rect.right = rect.left + mBodyWidth - mOffset * 2
        rect.bottom = rect.top + mRadius * 2 - mOffset * 2

        mPaint!!.color = mColorClothes
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = mStrokeWidth
        canvas.drawArc(rect, 0f, 180f, true, mPaint!!)

        val h = (mRadius * 0.5).toInt()
        val w = (mRadius * 0.3).toInt()

        rect.left += w.toFloat()
        rect.top = rect.top + mRadius - h
        rect.right -= w.toFloat()
        rect.bottom = rect.top + h

        canvas.drawRect(rect, mPaint!!)

        //画横线
        initPaint()
        mPaint!!.color = mColorStroke
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = mStrokeWidth
        val pts = FloatArray(20)//5条线

        pts[0] = rect.left - w
        pts[1] = rect.top + h
        pts[2] = pts[0] + w
        pts[3] = pts[1]

        pts[4] = pts[2]
        pts[5] = pts[3] + mOffset
        pts[6] = pts[4]
        pts[7] = pts[3] - h

        pts[8] = pts[6] - mOffset
        pts[9] = pts[7]
        pts[10] = pts[8] + (mRadius - w) * 2
        pts[11] = pts[9]

        pts[12] = pts[10]
        pts[13] = pts[11] - mOffset
        pts[14] = pts[12]
        pts[15] = pts[13] + h

        pts[16] = pts[14] - mOffset
        pts[17] = pts[15]
        pts[18] = pts[16] + w
        pts[19] = pts[17]
        canvas.drawLines(pts, mPaint!!)

        //画左吊带
        initPaint()
        mPaint!!.color = mColorClothes
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.FILL
        val path = Path()
        path.moveTo(rect.left - w.toFloat() - mOffset, mHandsHeight)
        path.lineTo(rect.left + h / 4, rect.top + h / 2)
        val smallW = w / 2 * Math.sin(Math.PI / 4).toFloat()
        path.lineTo(rect.left + (h / 4).toFloat() + smallW, rect.top + h / 2 - smallW)
        val smallW2 = w.toFloat() / Math.sin(Math.PI / 4).toFloat() / 2f
        path.lineTo(rect.left - w.toFloat() - mOffset, mHandsHeight - smallW2)

        canvas.drawPath(path, mPaint!!)
        initPaint()
        mPaint!!.color = mColorStroke
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.STROKE
        canvas.drawPath(path, mPaint!!)
        initPaint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(rect.left + h / 5, rect.top + h / 4, mStrokeWidth * 0.7f, mPaint!!)

        //画右吊带

        initPaint()
        mPaint!!.color = mColorClothes
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.FILL
        path.reset()
        path.moveTo(rect.left - w + 2 * mRadius - mOffset, mHandsHeight)
        path.lineTo(rect.right - h / 4, rect.top + h / 2)
        path.lineTo(rect.right - (h / 4).toFloat() - smallW, rect.top + h / 2 - smallW)
        path.lineTo(rect.left - w + 2 * mRadius - mOffset, mHandsHeight - smallW2)

        canvas.drawPath(path, mPaint!!)
        initPaint()
        mPaint!!.color = mColorStroke
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.STROKE
        canvas.drawPath(path, mPaint!!)
        initPaint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(rect.right - h / 5, rect.top + h / 4, mStrokeWidth * 0.7f, mPaint!!)

        //中间口袋
        initPaint()
        mPaint!!.color = mColorStroke
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.STROKE

        path.reset()
        val radiusBigPokect = w / 2.0f
        path.moveTo(rect.left + 1.5f * w, rect.bottom - h / 4)
        path.lineTo(rect.right - 1.5f * w, rect.bottom - h / 4)
        path.lineTo(rect.right - 1.5f * w, rect.bottom + h / 4)
        path.addArc(
            rect.right - 1.5f * w - radiusBigPokect * 2, rect.bottom + h / 4 - radiusBigPokect,
            rect.right - 1.5f * w, rect.bottom + (h / 4).toFloat() + radiusBigPokect,
            0f, 90f
        )
        path.lineTo(rect.left + 1.5f * w + radiusBigPokect, rect.bottom + (h / 4).toFloat() + radiusBigPokect)

        path.addArc(
            rect.left + 1.5f * w, rect.bottom + h / 4 - radiusBigPokect,
            rect.left + 1.5f * w + 2 * radiusBigPokect, rect.bottom + (h / 4).toFloat() + radiusBigPokect,
            90f, 90f
        )
        path.lineTo(rect.left + 1.5f * w, rect.bottom - (h / 4).toFloat() - mOffset)
        canvas.drawPath(path, mPaint!!)

        //        下边一竖，分开裤子
        canvas.drawLine(
            mBodyRect!!.left + mBodyWidth / 2,
            mBodyRect!!.bottom - h * 0.8f,
            mBodyRect!!.left + mBodyWidth / 2,
            mBodyRect!!.bottom,
            mPaint!!
        )
        //      左边的小口袋
        val radiusSamllPokect = w * 1.2f
        canvas.drawArc(
            mBodyRect!!.left - radiusSamllPokect,
            mBodyRect!!.bottom - mRadius - radiusSamllPokect,
            mBodyRect!!.left + radiusSamllPokect,
            mBodyRect!!.bottom - mRadius + radiusSamllPokect,
            80f,
            -60f,
            false,
            mPaint!!
        )
        //      右边小口袋
        canvas.drawArc(
            mBodyRect!!.right - radiusSamllPokect,
            mBodyRect!!.bottom - mRadius - radiusSamllPokect,
            mBodyRect!!.right + radiusSamllPokect,
            mBodyRect!!.bottom - mRadius + radiusSamllPokect,
            100f,
            60f,
            false,
            mPaint!!
        )
    }

    private fun drawEyesMouth(canvas: Canvas) {

        val eyesOffset = mRadius * 0.1f//眼睛中心处于上半圆直径 往上的高度偏移
        mPaint!!.strokeWidth = mStrokeWidth * 5
        //        计算眼镜带弧行的半径 分两段，以便眼睛中间有隔开的效果
        val radiusGlassesRibbon = (mRadius / Math.sin(Math.PI / 20)).toFloat()
        val rect = RectF()
        rect.left = mBodyRect!!.left + mRadius - radiusGlassesRibbon
        rect.top =
            mBodyRect!!.top + mRadius - (mRadius / Math.tan(Math.PI / 20)).toFloat() - radiusGlassesRibbon - eyesOffset
        rect.right = rect.left + radiusGlassesRibbon * 2
        rect.bottom = rect.top + radiusGlassesRibbon * 2
        canvas.drawArc(rect, 81f, 3f, false, mPaint!!)
        canvas.drawArc(rect, 99f, -3f, false, mPaint!!)

        //眼睛半径
        val radiusEyes = mRadius / 3
        initPaint()
        mPaint!!.color = Color.WHITE
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.FILL

        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 - radiusEyes - mOffset,
            mBodyRect!!.top + mRadius - eyesOffset,
            radiusEyes,
            mPaint!!
        )
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 + radiusEyes + mOffset,
            mBodyRect!!.top + mRadius - eyesOffset,
            radiusEyes,
            mPaint!!
        )

        mPaint!!.color = mColorStroke
        mPaint!!.style = Paint.Style.STROKE
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 - radiusEyes - mOffset,
            mBodyRect!!.top + mRadius - eyesOffset,
            radiusEyes,
            mPaint!!
        )
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 + radiusEyes + mOffset,
            mBodyRect!!.top + mRadius - eyesOffset,
            radiusEyes,
            mPaint!!
        )

        val radiusEyeballBlack = radiusEyes / 3
        mPaint!!.style = Paint.Style.FILL
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 - radiusEyes - mOffset,
            mBodyRect!!.top + mRadius - eyesOffset,
            radiusEyeballBlack,
            mPaint!!
        )
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 + radiusEyes + mOffset,
            mBodyRect!!.top + mRadius - eyesOffset,
            radiusEyeballBlack,
            mPaint!!
        )

        mPaint!!.color = Color.WHITE
        val radiusEyeballWhite = radiusEyeballBlack / 2
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 - radiusEyes + radiusEyeballWhite - mOffset * 2,
            mBodyRect!!.top + mRadius - radiusEyeballWhite + mOffset - eyesOffset,
            radiusEyeballWhite, mPaint!!
        )
        canvas.drawCircle(
            mBodyRect!!.left + mBodyWidth / 2 + radiusEyes + radiusEyeballWhite,
            mBodyRect!!.top + mRadius - radiusEyeballWhite + mOffset - eyesOffset,
            radiusEyeballWhite, mPaint!!
        )

        //        画嘴巴，因为位置和眼睛有相对关系，所以写在一块
        mPaint!!.color = mColorStroke
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mStrokeWidth
        val radiusMonth = mRadius
        rect.left = mBodyRect!!.left
        rect.top = mBodyRect!!.top - radiusMonth / 2.5f
        rect.right = rect.left + radiusMonth * 2
        rect.bottom = rect.top + radiusMonth * 2
        canvas.drawArc(rect, 95f, -20f, false, mPaint!!)

    }


    private fun drawFeet(canvas: Canvas) {
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.color = mColorStroke
        mPaint!!.style = Paint.Style.FILL_AND_STROKE

        val radiusFoot = mRadius / 3 * 0.4f
        val leftFootStartX = mBodyRect!!.left + mRadius - mOffset * 2
        val leftFootStartY = mBodyRect!!.bottom - mOffset
        val footWidthA = mRadius * 0.5f//脚宽度大-到半圆结束
        val footWidthB = footWidthA / 3//脚宽度-比较细的部分

        //      左脚
        val path = Path()
        path.moveTo(leftFootStartX, leftFootStartY)
        path.lineTo(leftFootStartX, leftFootStartY + mFootHeight)
        path.lineTo(leftFootStartX - footWidthA + radiusFoot, leftFootStartY + mFootHeight)

        val rectF = RectF()
        rectF.left = leftFootStartX - footWidthA
        rectF.top = leftFootStartY + mFootHeight - radiusFoot * 2
        rectF.right = rectF.left + radiusFoot * 2
        rectF.bottom = rectF.top + radiusFoot * 2
        path.addArc(rectF, 90f, 180f)
        path.lineTo(rectF.left + radiusFoot + footWidthB, rectF.top)
        path.lineTo(rectF.left + radiusFoot + footWidthB, leftFootStartY)
        path.lineTo(leftFootStartX, leftFootStartY)
        canvas.drawPath(path, mPaint!!)

        //      右脚
        val rightFootStartX = mBodyRect!!.left + mRadius + mOffset * 2
        path.reset()
        path.moveTo(rightFootStartX, leftFootStartY)
        path.lineTo(rightFootStartX, leftFootStartY + mFootHeight)
        path.lineTo(rightFootStartX + footWidthA - radiusFoot, leftFootStartY + mFootHeight)

        rectF.left = rightFootStartX + footWidthA - radiusFoot * 2
        rectF.top = leftFootStartY + mFootHeight - radiusFoot * 2
        rectF.right = rectF.left + radiusFoot * 2
        rectF.bottom = rectF.top + radiusFoot * 2
        path.addArc(rectF, 90f, -180f)
        path.lineTo(rectF.right - radiusFoot - footWidthB, rectF.top)
        path.lineTo(rectF.right - radiusFoot - footWidthB, leftFootStartY)
        path.lineTo(rightFootStartX, leftFootStartY)
        canvas.drawPath(path, mPaint!!)
    }

    private fun drawFeetShadow(canvas: Canvas) {
        mPaint!!.color = resources.getColor(android.R.color.darker_gray)
        canvas.drawOval(
            mBodyRect!!.left + mBodyWidth * 0.15f,
            mBodyRect!!.bottom - mOffset + mFootHeight,
            mBodyRect!!.right - mBodyWidth * 0.15f,
            mBodyRect!!.bottom - mOffset + mFootHeight + mStrokeWidth * 1.3f, mPaint!!
        )
    }


    private fun drawHands(canvas: Canvas) {
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.color = mColorBody

        //        左手
        val path = Path()
        val hypotenuse = mBodyRect!!.bottom - mRadius - mHandsHeight
        val radiusHand = hypotenuse / 6
        mPaint!!.pathEffect = CornerPathEffect(radiusHand)

        path.moveTo(mBodyRect!!.left, mHandsHeight)
        path.lineTo(mBodyRect!!.left - hypotenuse / 2, mHandsHeight + hypotenuse / 2)
        path.lineTo(mBodyRect!!.left + mOffset, mBodyRect!!.bottom - mRadius + mOffset)
        path.lineTo(mBodyRect!!.left, mHandsHeight)//增加兼容性,path没闭合在一起机子上会使手的下面的点没办法与裤子重合
        canvas.drawPath(path, mPaint!!)

        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = mColorStroke
        canvas.drawPath(path, mPaint!!)


        //        右手
        path.reset()
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mColorBody

        path.moveTo(mBodyRect!!.right, mHandsHeight)
        path.lineTo(mBodyRect!!.right + hypotenuse / 2, mHandsHeight + hypotenuse / 2)
        path.lineTo(mBodyRect!!.right - mOffset, mBodyRect!!.bottom - mRadius + mOffset)
        path.lineTo(mBodyRect!!.right, mHandsHeight)
        canvas.drawPath(path, mPaint!!)

        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = mColorStroke
        canvas.drawPath(path, mPaint!!)

        //        一个慢动作  - -||| 拐点内侧
        path.reset()
        mPaint!!.style = Paint.Style.FILL
        path.moveTo(mBodyRect!!.left, mHandsHeight + hypotenuse / 2 - mStrokeWidth)
        path.lineTo(mBodyRect!!.left - mStrokeWidth * 2, mHandsHeight + hypotenuse / 2 + mStrokeWidth * 2)
        path.lineTo(mBodyRect!!.left, mHandsHeight + hypotenuse / 2 + mStrokeWidth)
        path.lineTo(mBodyRect!!.left, mHandsHeight + hypotenuse / 2 - mStrokeWidth)
        canvas.drawPath(path, mPaint!!)

        path.reset()
        path.moveTo(mBodyRect!!.right, mHandsHeight + hypotenuse / 2 - mStrokeWidth)
        path.lineTo(mBodyRect!!.right + mStrokeWidth * 2, mHandsHeight + hypotenuse / 2 + mStrokeWidth * 2)
        path.lineTo(mBodyRect!!.right, mHandsHeight + hypotenuse / 2 + mStrokeWidth)
        path.lineTo(mBodyRect!!.right, mHandsHeight + hypotenuse / 2 - mStrokeWidth)
        canvas.drawPath(path, mPaint!!)

    }


    private fun initPaint() {
        if (mPaint == null) {
            mPaint = Paint()
        } else {
            mPaint!!.reset()
        }
        mPaint!!.isAntiAlias = true//边缘无锯齿
    }

    companion object {


        private const val DEFAULT_SIZE = 200 //View默认大小
        private const val BODY_SCALE = 0.6f//身体主干占整个view的比重
        private const val BODY_WIDTH_HEIGHT_SCALE = 0.6f //        身体的比例设定为 w:h = 3:5
    }

    /*public void randomBodyColor() {
        Random random = new Random();
        mColorBody = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        invalidate();
    }*/
}
