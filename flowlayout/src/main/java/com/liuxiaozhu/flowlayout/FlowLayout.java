package com.liuxiaozhu.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Created by liuxiaozhu on 2018/1/16.
 * Email: chenhuixueba@163.com
 * tag标签之流式布局
 */

public class FlowLayout extends ViewGroup {
    private static final String TAG = FlowLayout.class.getSimpleName();
    //用来保存每一行的View列表
    private List<List<View>> mViewLinesList = new ArrayList<>();
    //用来保存行高的列表
    private List<Integer> mLineHeights = new ArrayList<>();

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure");
        //获取父容器的测量模式
        int iWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int iHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取父容器的宽高
        int iWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int iHeightSpecSize = MeasureSpec.getMode(heightMeasureSpec);
        //记录行宽度和高度
        int measuredWidth = 0;
        int measuredHeight = 0;
        //当前行的宽高
        int iCurLineW = 0;
        int iCurLineH = 0;
        if (iWidthMode == MeasureSpec.EXACTLY && iHeightMode == MeasureSpec.EXACTLY) {
            measuredWidth = iWidthSpecSize;
            measuredHeight = iHeightSpecSize;
        } else {
//            当前子View的宽高
            int iChildWidth;
            int iChildHeight;
            int childCount = getChildCount();
            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                iChildWidth = childView.getMeasuredWidth() + layoutParams.leftMargin
                        + layoutParams.rightMargin;
                iChildHeight = childView.getMeasuredHeight() + layoutParams.topMargin
                        + layoutParams.bottomMargin;
                if (iCurLineW + iChildWidth > iWidthSpecSize) {
                    //换行,记录新的一行的信息
                    measuredWidth = Math.max(measuredWidth, iCurLineW);
//                    行高度累加
                    measuredHeight += iCurLineH;
                    mViewLinesList.add(viewList);
                    mLineHeights.add(iCurLineH);
                    //重新赋值新一行的宽高
                    iCurLineW = iChildWidth;
                    iCurLineH = iChildHeight;

                    viewList = new ArrayList<>();
                    viewList.add(childView);
                } else {
                    //记录某一行内的消息
                    iCurLineW += iChildWidth;
//                    获取当前行子View高的最大值
                    iCurLineH = Math.max(iCurLineH, iChildHeight);
                    viewList.add(childView);
                }
                if(i == childCount - 1){
                    //1、记录当前行的最大宽度，高度累加
                    measuredWidth = Math.max(measuredWidth,iCurLineW);
                    measuredHeight += iCurLineH;

                    //2、将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    mViewLinesList.add(viewList);
                    mLineHeights.add(iCurLineH);

                }
            }
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout");
        int left, top, right, bottom;
        int curTop = 0;
        int curLeft = 0;
        //行数
        int lineCount = mViewLinesList.size();
        for (int i = 0; i < lineCount; i++) {
            List<View> viewList = mViewLinesList.get(i);
            int lineViewSize = viewList.size();
            for (int j = 0; j < lineViewSize; j++) {
                View childView = viewList.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                curLeft += childView.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
                Log.e(TAG, "curLeft" + curLeft);
                Log.e(TAG, "curTop" + curTop);
            }
            curLeft = 0;
            curTop += mLineHeights.get(i);
        }
        mViewLinesList.clear();
        mLineHeights.clear();
    }

    public interface OnItemClickListener{
        void onItemClick (View v, int index);
    }

    /**
     * 给View设置监听
     * @param listener
     */
    public void setOnItemClickListener(final OnItemClickListener listener){

        int childCount = getChildCount();
        for(int i = 0 ; i < childCount ; i++){
            View childView = getChildAt(i);
            final int finalI = i;
            childView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, finalI);
                }
            });
        }

    }
}
