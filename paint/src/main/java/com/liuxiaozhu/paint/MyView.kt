package com.liuxiaozhu.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.SumPathEffect
import android.util.AttributeSet
import android.view.View

/**
 * Author：Created by liuxiaozhu on 2018/1/16.
 * Email: chenhuixueba@163.com
 */

class MyView : View {


    private// 定义路径的起点
    // 定义路径的各个点
    val path: Path
        get() {
            val path = Path()
            path.moveTo(0f, 0f)
            for (i in 0..40) {
                path.lineTo((i * 35).toFloat(), (Math.random() * 150).toFloat())
            }
            return path
        }

    private val paint: Paint
        get() {
            val paint = Paint()
            paint.strokeWidth = 4f
            paint.color = Color.GREEN
            paint.style = Paint.Style.STROKE
            paint.isAntiAlias = true
            return paint
        }


    private val stampPath: Path
        get() {
            val path = Path()
            path.moveTo(0f, 20f)
            path.lineTo(10f, 0f)
            path.lineTo(20f, 20f)
            path.close()

            path.addCircle(0f, 0f, 3f, Path.Direction.CCW)

            return path
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        Paint mPaint = new Paint();
        //        mPaint.setMaskFilter()


        //color 0xFF6BB7ED
        /**
         * StrokeCap示例
         */
        //        drawStrokeCap(canvas);

        /**
         * stokeJoin示例
         */
        //        drawStrokeJoin(canvas);

        /**
         * CornerPathEffect示例
         */
        //        drawCornerPathEffect(canvas);

        /**
         * CornerPathEffect DEMO曲线
         */
        //        drawCornerPathEffectDemo(canvas);

        /**
         * DashPathEffect DEMO 效果
         */
        //        drawDashPathEffectDemo(canvas);

        /**
         * DiscretePathEffect DEMO效果
         */
        //        drawDiscretePathEffectDemo(canvas);

        /**
         * PathDashPathEffect效果
         */
        //        drawPathDashPathEffect(canvas);

        /**
         * PathDashPathEffect DEMO效果
         */
        //        drawPathDashPathEffectDemo(canvas);
        //
        /**
         * ComposePathEffect与SumPathEffect
         */
        //        drawComposePathEffectDemo(canvas);

        /**
         * SubpixelText Demo
         */
        drawSubpixelText(canvas)

    }

    private fun drawStrokeCap(canvas: Canvas) {
        val paint = Paint()
        paint.strokeWidth = 80f
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        //paint.setStrokeMiter(); //画笔的倾斜度
        //paint.setDither();//设置图像是否使用抖动处理,会使得绘制出来的图片更加平滑和饱满，图像更加清晰

        paint.strokeCap = Paint.Cap.BUTT// 无线帽
        canvas.drawLine(100f, 200f, 400f, 200f, paint)

        paint.strokeCap = Paint.Cap.SQUARE
        canvas.drawLine(100f, 400f, 400f, 400f, paint)// 方形线帽

        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(100f, 600f, 400f, 600f, paint)// 圆形线帽

    }


    private fun drawStrokeJoin(canvas: Canvas) {
        val paint = Paint()
        paint.strokeWidth = 80f
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true

        val path = Path()
        path.moveTo(100f, 100f)
        path.lineTo(450f, 100f)
        path.lineTo(100f, 300f)
        paint.strokeJoin = Paint.Join.MITER
        canvas.drawPath(path, paint)

        path.moveTo(100f, 400f)
        path.lineTo(450f, 400f)
        path.lineTo(100f, 600f)
        paint.strokeJoin = Paint.Join.BEVEL
        canvas.drawPath(path, paint)

        path.moveTo(100f, 700f)
        path.lineTo(450f, 700f)
        path.lineTo(100f, 900f)
        paint.strokeJoin = Paint.Join.ROUND
        canvas.drawPath(path, paint)
    }


    /**
     * CornerPathEffect
     *
     * @param canvas
     */
    private fun drawCornerPathEffectDemo(canvas: Canvas) {
        val paint = paint
        val path = path
        canvas.drawPath(path, paint)

        paint.pathEffect = CornerPathEffect(200f)
        canvas.save()
        canvas.translate(0f, 150f)
        canvas.drawPath(path, paint)
    }


    private fun drawCornerPathEffect(canvas: Canvas) {
        val paint = paint
        val path = Path()
        path.moveTo(100f, 600f)
        path.lineTo(400f, 100f)
        path.lineTo(700f, 900f)

        canvas.drawPath(path, paint)
        paint.color = Color.RED
        paint.pathEffect = CornerPathEffect(100f)
        canvas.drawPath(path, paint)

        paint.pathEffect = CornerPathEffect(200f)
        paint.color = Color.YELLOW
        canvas.drawPath(path, paint)
    }


    private fun drawDashPathEffectDemo(canvas: Canvas) {
        val paint = paint
        val path = path

        canvas.translate(0f, 100f)
        paint.pathEffect = DashPathEffect(floatArrayOf(15f, 20f, 15f, 15f), 0f)
        canvas.drawPath(path, paint)
    }


    private fun drawDiscretePathEffectDemo(canvas: Canvas) {
        val paint = paint
        val path = path

        canvas.drawPath(path, paint)
        /**
         * 把原有的路线,在指定的间距处插入一个突刺
         * 第一个这些突出的“杂点”的间距,值越小间距越短,越密集
         * 第二个是突出距离
         */
        canvas.translate(0f, 200f)
        paint.pathEffect = DiscretePathEffect(2f, 5f)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 200f)
        paint.pathEffect = DiscretePathEffect(6f, 5f)
        canvas.drawPath(path, paint)


        canvas.translate(0f, 200f)
        paint.pathEffect = DiscretePathEffect(6f, 15f)
        canvas.drawPath(path, paint)
    }

    private fun drawPathDashPathEffectDemo(canvas: Canvas) {
        val paint = paint

        val path = path
        canvas.drawPath(path, paint)

        canvas.translate(0f, 200f)

        paint.pathEffect = PathDashPathEffect(stampPath, 35f, 0f, PathDashPathEffect.Style.MORPH)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 200f)
        paint.pathEffect = PathDashPathEffect(stampPath, 35f, 0f, PathDashPathEffect.Style.ROTATE)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 200f)
        paint.pathEffect = PathDashPathEffect(stampPath, 35f, 0f, PathDashPathEffect.Style.TRANSLATE)
        canvas.drawPath(path, paint)
    }


    private fun drawPathDashPathEffect(canvas: Canvas) {
        val paint = paint

        val path = Path()
        path.moveTo(100f, 600f)
        path.lineTo(400f, 150f)
        path.lineTo(700f, 900f)
        canvas.drawPath(path, paint)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 200f)

        /**
         * 利用以另一个路径为单位,延着路径盖章.相当于PS的印章工具
         */
        paint.pathEffect = PathDashPathEffect(stampPath, 35f, 0f, PathDashPathEffect.Style.MORPH)
        canvas.drawPath(path, paint)
    }


    private fun drawComposePathEffectDemo(canvas: Canvas) {
        //画原始路径
        val paint = paint
        val path = path
        canvas.drawPath(path, paint)

        //仅应用圆角特效的路径
        canvas.translate(0f, 300f)
        val cornerPathEffect = CornerPathEffect(100f)
        paint.pathEffect = cornerPathEffect
        canvas.drawPath(path, paint)

        //仅应用虚线特效的路径
        canvas.translate(0f, 300f)
        val dashPathEffect = DashPathEffect(floatArrayOf(2f, 5f, 10f, 10f), 0f)
        paint.pathEffect = dashPathEffect
        canvas.drawPath(path, paint)

        //利用ComposePathEffect先应用圆角特效,再应用虚线特效
        canvas.translate(0f, 300f)
        val composePathEffect = ComposePathEffect(dashPathEffect, cornerPathEffect)
        paint.pathEffect = composePathEffect
        canvas.drawPath(path, paint)

        //利用SumPathEffect,分别将圆角特效应用于原始路径,然后将生成的两条特效路径合并
        canvas.translate(0f, 300f)
        paint.style = Paint.Style.STROKE
        val sumPathEffect = SumPathEffect(cornerPathEffect, dashPathEffect)
        paint.pathEffect = sumPathEffect
        canvas.drawPath(path, paint)

    }


    private fun drawSubpixelText(canvas: Canvas) {

        val paint = Paint()
        paint.color = Color.GREEN
        val text = "动脑学院高级UI"
        paint.textSize = 100f

        paint.isSubpixelText = false
        canvas.drawText(text, 0f, 200f, paint)

        canvas.translate(0f, 300f)
        paint.isSubpixelText = true
        canvas.drawText(text, 0f, 200f, paint)
    }

}
