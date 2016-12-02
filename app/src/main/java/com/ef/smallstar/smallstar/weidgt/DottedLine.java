package com.ef.smallstar.smallstar.weidgt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ext.ezreal.cai on 2016/11/30.
 */

public class DottedLine extends View {

    private int color = Color.GRAY;


    public DottedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(color);
        p.setStrokeWidth(10);
        PathEffect effects = new DashPathEffect(new float[]{0, 10, 35, 2}, 1);

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getMeasuredHeight());
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());

        p.setPathEffect(effects);
        canvas.drawPath(path, p);
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    /**
     * 设置颜色值
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = Color.parseColor(color);
        invalidate();
    }
}
