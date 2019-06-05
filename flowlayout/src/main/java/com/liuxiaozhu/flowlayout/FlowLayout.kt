package com.liuxiaozhu.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * Author：Created by liuxiaozhu on 2018/1/16.
 * Email: chenhuixueba@163.com
 * tag标签之流式布局
 */

class FlowLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    //用来保存每一行的View列表
    private val mViewLinesList = ArrayList<List<View>>()
    //用来保存行高的列表
    private val mLineHeights = ArrayList<Int>()


    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, "onMeasure")

        mViewLinesList.clear()
        mLineHeights.clear()

        //获取父容器的测量模式
        val iWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val iHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取父容器的宽高
        val iWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val iHeightSpecSize = MeasureSpec.getMode(heightMeasureSpec)
        //记录行宽度和高度
        var measuredWidth = 0
        var measuredHeight = 0
        //当前行的宽高
        var iCurLineW = 0
        var iCurLineH = 0
        if (iWidthMode == MeasureSpec.EXACTLY && iHeightMode == MeasureSpec.EXACTLY) {
            measuredWidth = iWidthSpecSize
            measuredHeight = iHeightSpecSize
        } else {
            //            当前子View的宽高
            var iChildWidth: Int
            var iChildHeight: Int
            val childCount = childCount
            var viewList: MutableList<View> = ArrayList()
            for (i in 0 until childCount) {
                val childView = getChildAt(i)
                childView.measure(widthMeasureSpec, heightMeasureSpec)
                val layoutParams = childView.layoutParams as MarginLayoutParams
                iChildWidth = (childView.measuredWidth + layoutParams.leftMargin
                        + layoutParams.rightMargin)
                iChildHeight = (childView.measuredHeight + layoutParams.topMargin
                        + layoutParams.bottomMargin)

                if (iCurLineW + iChildWidth > iWidthSpecSize) {
                    //换行,记录新的一行的信息
                    measuredWidth = Math.max(measuredWidth, iCurLineW)
                    //                    行高度累加
                    measuredHeight += iCurLineH
                    mViewLinesList.add(viewList)
                    mLineHeights.add(iCurLineH)
                    //重新赋值新一行的宽高
                    iCurLineW = iChildWidth
                    iCurLineH = iChildHeight

                    viewList = ArrayList()
                    viewList.add(childView)
                } else {
                    //记录某一行内的消息
                    iCurLineW += iChildWidth
                    //                    获取当前行子View高的最大值
                    iCurLineH = Math.max(iCurLineH, iChildHeight)
                    viewList.add(childView)
                }
                if (i == childCount - 1) {
                    //1、记录当前行的最大宽度，高度累加
                    measuredWidth = Math.max(measuredWidth, iCurLineW)
                    measuredHeight += iCurLineH
                    //2、将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    mViewLinesList.add(viewList)
                    mLineHeights.add(iCurLineH)

                }
            }
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.e(TAG, "onLayout")
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        var curTop = 0
        var curLeft = 0
        //行数
        val lineCount = mViewLinesList.size
        for (i in 0 until lineCount) {
            val viewList = mViewLinesList[i]
            for (j in viewList.indices) {
                val childView = viewList[j]
                val layoutParams = childView.layoutParams as MarginLayoutParams
                left = curLeft + layoutParams.leftMargin
                top = curTop + layoutParams.topMargin
                right = left + childView.measuredWidth
                bottom = top + childView.measuredHeight
                childView.layout(left, top, right, bottom)
                curLeft += childView.measuredWidth + layoutParams.rightMargin + layoutParams.leftMargin
                Log.e(TAG, "curLeft$curLeft")
                Log.e(TAG, "curTop$curTop")
            }
            curLeft = 0
            curTop += mLineHeights[i]
        }
        mViewLinesList.clear()
        mLineHeights.clear()
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, index: Int)
    }

    /**
     * 给View设置监听
     *
     * @param listener
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {

        val childCount = childCount
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.setOnClickListener { v -> listener.onItemClick(v, i) }
        }

    }

    companion object {
        private val TAG = FlowLayout::class.java.simpleName
    }
}
