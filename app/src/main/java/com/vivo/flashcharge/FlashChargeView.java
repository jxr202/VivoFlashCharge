package com.vivo.flashcharge;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by Jxr33 on 2016/8/23.
 */
public class FlashChargeView extends View implements ValueAnimator.AnimatorUpdateListener {

    /** 细线，粗线，圆，圆内进度，文字的画笔 */
    private Paint mPaintSmall, mPaintBig, mInCrilePaint, mInLine, mTextPaint;
    /** 控件的高宽 */
    private static float WIDTH, HEIGHT;
    /** 动画 */
    private ValueAnimator mAnimator;
    /** 圆弧起始角度 */
    private float startAngle = 0;
    /** 圆弧旋转角度 */
    private float offset = 0;
    /** 当前电量 */
    private int mCurrPower = 70;
    /** 当前电量的进度 */
    private float mProgress;


    public FlashChargeView(Context context) {
        super(context);
        init();
    }

    public FlashChargeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlashChargeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaintSmall = new Paint();
        mPaintSmall.setAntiAlias(true);
        mPaintSmall.setStrokeWidth(5);
        mPaintSmall.setStyle(Paint.Style.STROKE);
        mPaintSmall.setColor(Color.parseColor("#12ADFF"));

        mPaintBig = new Paint();
        mPaintBig.setAntiAlias(true);
        mPaintBig.setStrokeWidth(20);
        mPaintBig.setStyle(Paint.Style.STROKE);
        mPaintBig.setColor(Color.parseColor("#12ADFF"));

        mInCrilePaint = new Paint();
        mInCrilePaint.setAntiAlias(true);
        mInCrilePaint.setStrokeWidth(.5f);
        mInCrilePaint.setStyle(Paint.Style.STROKE);
        mInCrilePaint.setColor(Color.parseColor("#eeeeee"));

        mInLine = new Paint();
        mInLine.setAntiAlias(true);
        mInLine.setStrokeWidth(3);
        mInLine.setColor(Color.parseColor("#00ff00"));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(80);
        mTextPaint.setColor(Color.parseColor("#ffffff"));

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(1500);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(Animation.INFINITE);
        mAnimator.addUpdateListener(this);

        this.mProgress = mCurrPower * 72.0f / 100;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        WIDTH = w;
        HEIGHT = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutArc1(canvas);
        drawOutArc2(canvas);
        drawOutArc3(canvas);
        drawOutArc4(canvas);
        drawCircle(canvas);
        drawCircleIn(canvas);
        drawText(canvas);
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        float textSize = mTextPaint.measureText(mCurrPower + "%");
        float x = WIDTH / 2 - textSize / 2;
        float y = HEIGHT / 2 + textSize / 5;
        canvas.drawText(mCurrPower + "%", x, y, mTextPaint);
    }

    /**
     * 绘制最里面的圆
     * @param canvas
     */
    private void drawCircleIn(Canvas canvas) {
        float radius = (float) (HEIGHT - (HEIGHT * 0.3) * 2 - (WIDTH * 0.22));
        canvas.drawCircle(WIDTH / 2, HEIGHT / 2, radius, mInCrilePaint);
        canvas.save();
    }

    /**
     * 绘制内切圆和锯齿，通过canvas的旋转，画出对应的锯齿线
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        float radius = HEIGHT - (HEIGHT * 0.3f) * 2 - (WIDTH * 0.17f);
        canvas.drawCircle(WIDTH / 2, HEIGHT / 2, radius, mInCrilePaint);
        canvas.save();

        for (int i = 0; i < 72; i++) {
            if (i >= mProgress) {
                mInLine.setColor(Color.parseColor("#555555"));
            } else {
                mInLine.setColor(Color.parseColor("#00ff00"));
            }
            canvas.drawLine(WIDTH / 2, HEIGHT / 3.7f, WIDTH / 2, HEIGHT / 3.7f + HEIGHT * 0.05f , mInLine);
            canvas.rotate(5, getWidth() / 2, getHeight() / 2);
        }
    }

    /**
     * 绘制最外层弧线
     * @param canvas
     */
    private void drawOutArc1(Canvas canvas) {
        RectF mRectF = new RectF(WIDTH * 0.1f, WIDTH * 0.1f, WIDTH * 0.9f, WIDTH * 0.9f);
        canvas.drawArc(mRectF, startAngle + offset, 200, false, mPaintSmall);
    }

    /**
     * 绘制外层的第二条弧线
     * @param canvas
     */
    private void drawOutArc2(Canvas canvas) {
        RectF mRectF = new RectF(WIDTH * 0.14f, WIDTH * 0.14f, WIDTH * 0.85f, WIDTH * 0.85f);
        canvas.drawArc(mRectF, -(startAngle + offset), 150, false, mPaintBig);
    }

    /**
     * 绘制外层第三条弧线
     * @param canvas
     */
    private void drawOutArc3(Canvas canvas) {
        RectF mRectF = new RectF(WIDTH * 0.22f, WIDTH * 0.22f, WIDTH * 0.795f, WIDTH * 0.795f);
        canvas.drawArc(mRectF, startAngle + offset - 90, 110, false, mPaintSmall);
    }

    /**
     * 绘制外层第四条弧线
     * @param canvas
     */
    private void drawOutArc4(Canvas canvas) {
        RectF mRectF = new RectF(WIDTH * 0.255f, WIDTH * 0.255f, WIDTH * 0.75f, WIDTH * 0.75f);
        canvas.drawArc(mRectF, -(startAngle + offset + 150), 150, false, mPaintBig);
    }

    /**
     * 开始播放闪充动画
     */
    public void startChargeAnimator() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    /**
     * 停止闪充动画
     */
    public void endChargeAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    /**
     * 设置当前电量
     * @param power
     */
    public void setPower(int power) {
        this.mCurrPower = power;
        this.mProgress = power * 72.0f / 100;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();
        this.offset = 360 * fraction;
        this.invalidate();
    }
}
