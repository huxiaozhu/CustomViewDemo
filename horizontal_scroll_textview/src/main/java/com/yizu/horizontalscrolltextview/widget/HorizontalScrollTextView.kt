package com.yizu.horizontalscrolltextview.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.yizu.horizontalscrolltextview.R

import java.util.ArrayList

/**
 * Created by XiaoZhu on 2019-5-5 17:23:25.
 */
class HorizontalScrollTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mGestureDetector: GestureDetector? = null
    private val textList = ArrayList<String>()

    private val interpolator = FastOutSlowInInterpolator()
    private var switched = false
    private var animX = 0f
    private var currentPosition = 0
    private var animDuration = 400
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mText: CharSequence? = null
    private var pagerNum: Int = 0
    private var proPaint: Paint? = null
    private var proBackgroundPaint: Paint? = null

    private var startProX: Float = 0.toFloat()
    private var endProX: Float = 0.toFloat()

    private var proWidth: Float = 0.toFloat()
    private var proY: Float = 0.toFloat()

    /**
     * 获取当前页总行数
     */
    private val lineNum: Int
        get() {
            val topOfLastLine = height - paddingTop - paddingBottom
            return layout.getLineForVertical(topOfLastLine)
        }

    init {
        mGestureDetector = GestureDetector(context, TouchLisiner())
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollTextView)
        val proColor = typedArray.getColor(R.styleable.HorizontalScrollTextView_proColor, Color.RED)
        val proBackgroundColor =
            typedArray.getColor(R.styleable.HorizontalScrollTextView_probackgroundColor, Color.WHITE)
        val proWidths = typedArray.getDimension(R.styleable.HorizontalScrollTextView_proWidth, 8f)
        val proBackgroundWidth = typedArray.getDimension(R.styleable.HorizontalScrollTextView_proBackgroundWidth, 4f)
        typedArray.recycle()
        proPaint = Paint()
        proPaint?.isAntiAlias = true
        proPaint?.color = proColor
        proPaint?.strokeWidth = proWidths
        proPaint?.style = Paint.Style.STROKE

        proBackgroundPaint = Paint()
        proBackgroundPaint?.isAntiAlias = true
        proBackgroundPaint?.color = proBackgroundColor
        proBackgroundPaint?.strokeWidth = proBackgroundWidth
        proBackgroundPaint?.style = Paint.Style.STROKE
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width
        mHeight = height
        if (proWidth == 0f) {
            proWidth = mWidth.toFloat()
            startProX = 0f
            endProX = mWidth.toFloat()
            proY = mHeight - proPaint!!.strokeWidth
        }
        if (textList.size != 0 && textList[0] == text) {
            return
        }
        mText = text
        textList.clear()
        mText = text
        if (textList.size == 0) {
            if (!TextUtils.isEmpty(mText)) {
                //总行数
                val totalLineNum = lineCount
                //				当前页的行数
                val lineNum = lineNum
                pagerNum = if (lineNum + 1 >= totalLineNum) {
                    1
                } else {
                    totalLineNum / lineNum + if (totalLineNum % lineNum == 0) 0 else 1
                }
                var num = 0
                for (i in 0 until pagerNum) {
                    var end = lineNum * (i + 1) - 1
                    if (pagerNum - 1 == i) end = totalLineNum - 1
                    val charNum = getCharNum(end)
                    textList.add(mText!!.substring(num, charNum))
                    num = charNum
                }
                text = textList[0]
                if (pagerNum < 1) pagerNum = 1
                proWidth = (width / pagerNum).toFloat()
                endProX = proWidth
            }
        }
    }

    /**
     * 某一行的最后一个字符的位置
     */
    private fun getCharNum(num: Int): Int {
        return layout.getLineEnd(num)
    }


    override fun onDraw(canvas: Canvas) {
        //绘制背景
        canvas.drawLine(0f, proY, mWidth.toFloat(), proY, proBackgroundPaint!!)
        //绘制进度条
        canvas.drawLine(startProX, proY, endProX, proY, proPaint!!)

        canvas.translate(animX, 0f)
        super.onDraw(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector!!.onTouchEvent(event)
    }

    private fun start(isLeft: Boolean) {
        animX = 0f
        val p = TextPaint(Paint.ANTI_ALIAS_FLAG)
        val textWidth = p.measureText(textList[currentPosition])
        val end = Math.min((width - paddingRight).toFloat(), textWidth + paddingLeft)
        val anim = ValueAnimator.ofFloat(animX, 0f, end + width - paddingRight)
        anim.interpolator = interpolator
        anim.duration = animDuration.toLong()
        anim.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            if (value > end) {
                if (!switched) {
                    text = textList[currentPosition]
                    switched = true
                }
                animX = width.toFloat() - paddingRight.toFloat() - (value - end)
            } else {
                animX = -value
            }
            if (!isLeft) animX = -animX
            invalidate()
        }

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                switched = false
                startProX = proWidth * currentPosition
                endProX = startProX + proWidth
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        anim.start()
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    fun setAnimDuration(duration: Int) {
        animDuration = duration
    }

    /**
     * 手势处理
     */
    internal inner class TouchLisiner : GestureDetector.SimpleOnGestureListener() {
        // 触发条件 ：
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
        private val FLING_MIN_DISTANCE = 100
        private val FLING_MIN_VELOCITY = 200

        /**
         * @param e1        第1个ACTION_DOWN MotionEvent
         * @param e2        最后一个ACTION_MOVE MotionEvent
         * @param velocityX X轴上的移动速度，像素/秒
         * @param velocityY Y轴上的移动速度，像素/秒
         * @return
         */
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (switched || textList.size <= 1) return true
            if (e1.x - e2.x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                //左滑
                if (currentPosition + 1 < textList.size) {
                    currentPosition++
                    start(true)
                }
            } else if (e2.x - e1.x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                //右滑
                if (currentPosition - 1 >= 0) {
                    currentPosition--
                    start(false)
                }
            }
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

}
