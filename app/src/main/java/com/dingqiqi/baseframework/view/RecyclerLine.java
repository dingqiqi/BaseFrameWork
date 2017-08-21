package com.dingqiqi.baseframework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dingqiqi.baseframework.util.DisplayUtil;

/**
 * RecyclerView画间隔线类
 * Created by dingqiqi on 2016/12/27.
 */
public class RecyclerLine extends RecyclerView.ItemDecoration {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 水平间隔线
     */
    private int mOrien = 0;
    /**
     * 间隔线高度
     */
    private int mHeight;
    /**
     * 间隔线画笔
     */
    private Paint mPaint;

    public RecyclerLine(Context mContext) {
        this.mContext = mContext;
        mHeight = DisplayUtil.dp2px(mContext, 1);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    public void setOrien(int orien) {
        this.mOrien = orien;
    }

    //画最上面一条
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    //画中间的间隔线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (mOrien == 0) {
            int left = parent.getPaddingLeft();
            int right = left + parent.getMeasuredWidth();

            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                int top = parent.getChildAt(i).getBottom();
                int bottom = top + mHeight;

                c.drawRect(left, top, right, bottom, mPaint);
            }
        } else {
            int top = parent.getPaddingTop();
            int bottom = top + parent.getMeasuredHeight();

            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                int left = parent.getChildAt(i).getRight();
                int right = left + mHeight;

                c.drawRect(left, top, right, bottom, mPaint);
            }
        }

    }

    //设置间隔线高度
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrien == 0) {
            outRect.set(0, 0, 0, mHeight);
        } else {
            outRect.set(0, 0, mHeight, 0);
        }
    }
}
