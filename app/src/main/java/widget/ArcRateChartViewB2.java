package widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.lahphim.mamoon.R;

/**
 * 圆弧比率图，用于展示钱宝宝和定期理财项目
 */
public class ArcRateChartViewB2 extends View {
    private static final int DEFAULT_BG_CIRCLE_COLOR = 0xffe6e6e6;
    private static final int DEFAULT_RATE_ARC_WIDTH = 25;
    private static final int DEFAULT_DRAW_ARC_TIME = 10000;
    private static final int DEFAULT_ROTATE_ANGLE = 270;
    private static final int DEFAULT_ROTATE_TIME = 600;
    private static final int CIRCLE_ANGLE = 360;
    private static final String TAG = ArcRateChartView.class.getSimpleName();

    private Context mContext;
    private Paint mArcRatePaint; // 画圆弧的画笔
    private Paint mBgCirclePaint; // 画"圆弧"中的那个圆形
    private RectF mArcRateRect; // 圆弧的边界

    private double mOriginTotalRateSum;
    private int mGapAngle = 1; // 空隙角度大小
    private int mDrawArcTime = 0; // 绘制"大小圆弧"的时间
    private int mRotateTime = 0; // 旋转整个圆环的时间

    /**
     * 背景圆的颜色
     */
    private int mBgCircleColor = DEFAULT_BG_CIRCLE_COLOR;
    /**
     * 大圆弧的宽度
     */
    private float mBigRateArcWidth;
    /**
     * 小圆弧的宽度
     */
    private float mSmallRateArcWidth;
    /**
     * 圆弧的宽度
     */
    private float mArcWidth;
    /**
     * 当前进度
     */
    private int progress;
    /**
     * 当前是否在执行动画
     */
    private boolean mIsAnimating = false;
    private List<ArcInfo> mArcInfoList = new ArrayList<>();
    private List<Float> mRateAngleList = new ArrayList<>();
    private List<Float> mSumRateAngleList = new ArrayList<>();
    private int mCurrentIndex = 0;

    public ArcRateChartViewB2(Context context) {
        this(context, null);
    }

    public ArcRateChartViewB2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcRateChartViewB2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ArcRateChart);
        mSmallRateArcWidth = mTypedArray.getDimension(
                R.styleable.ArcRateChart_smallRateArcWidth, DensityUtil.dip2px(mContext, DEFAULT_RATE_ARC_WIDTH));

        mBigRateArcWidth = mTypedArray.getDimension(
                R.styleable.ArcRateChart_bigRateArcWidth, DensityUtil.dip2px(mContext, DEFAULT_RATE_ARC_WIDTH));

        mBgCircleColor = mTypedArray.getColor(
                R.styleable.ArcRateChart_bgCircleColor, DEFAULT_BG_CIRCLE_COLOR);

        // 绘制完成圆弧的时间
        mDrawArcTime = mTypedArray.getInt(R.styleable.ArcRateChart_drawArcTime, DEFAULT_DRAW_ARC_TIME);
        // 将圆弧位置摆正所需时间
        mRotateTime = mTypedArray.getInt(R.styleable.ArcRateChart_rotateTime, DEFAULT_ROTATE_TIME);
        mTypedArray.recycle();

        // 计算出圆弧的宽度
        mArcWidth = Math.min(mSmallRateArcWidth, mBigRateArcWidth);

        initPaint();
    }

    private void initPaint() {
        mArcRatePaint = new Paint();
        setPaintAttrs(mArcRatePaint, Style.FILL);

        // 背景圆
        mBgCirclePaint = new Paint();
        setPaintAttrs(mBgCirclePaint, mBgCircleColor, Style.FILL);
    }

    /**
     * 动画要求，"小比率"圆弧一直存在，而"大比率"圆弧慢慢的增加。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画圆弧 ，画圆环弧进度
         */
        int centerX = getWidth() / 2; // 获取圆心的x坐标
        int centerY = getHeight() / 2; // 获取圆心的y坐标
        canvas.rotate(-90, centerX, centerY);

        int center = Math.min(centerX, centerY);
        int radius = center;

        int left = center - radius;
        int top = center - radius;
        int right = center + radius;
        int bottom = center + radius;

        if (centerX >= centerY) {
            left = centerX - radius;
            right = centerX + radius;
        } else {
            top = centerY - radius;
            bottom = centerY + radius;
        }

        // 背景圆的半径
        int bgCircleRadius = (int) (radius - mArcWidth);

        if (mArcRateRect == null) {
            mArcRateRect = new RectF(left, top, right, bottom);
        }

        if (mRateAngleList.size() <= 0) {
            return;
        }

        int totalCnt = mRateAngleList.size();

        float hadDrawTotalAngle = 0;
        for (int i = 0; i <= mCurrentIndex; i++) {
            if (mCurrentIndex == 0) {
                hadDrawTotalAngle += mRateAngleList.get(totalCnt - 1 - mCurrentIndex);
            } else {
                hadDrawTotalAngle += mRateAngleList.get(totalCnt - 1 - mCurrentIndex) + 1;
            }
        }

        if (progress > hadDrawTotalAngle) {
            mCurrentIndex++;
        }

        for (int i = 0; i <= mCurrentIndex; i++) {
            if (mCurrentIndex == 0) {
                mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - mCurrentIndex).mColorId));
                canvas.drawArc(mArcRateRect, 0, progress, true, mArcRatePaint);
            } else {
                mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - mCurrentIndex).mColorId));
                float xxx = 0;
                if (mCurrentIndex == 1) {
                    xxx = mRateAngleList.get(totalCnt - 1);
                } else if (mCurrentIndex == 2) {
                    xxx = mRateAngleList.get(totalCnt - 1) + mRateAngleList.get(totalCnt - 2) + 1;
                } else if (mCurrentIndex == 3) {
                    xxx = mRateAngleList.get(totalCnt - 1) + mRateAngleList.get(totalCnt - 2) + mRateAngleList.get(totalCnt - 3) + 1;
                }
                canvas.drawArc(mArcRateRect, 0, progress - xxx, true, mArcRatePaint);

//                for (int j = i - 1; j >= 0; j--) {
//                    float aa = mRateAngleList.get(totalCnt - 1 - j);
//                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - j).mColorId));
//                    canvas.drawArc(mArcRateRect, progress - xxx + 1, progress - xxx + 1 + aa, true, mArcRatePaint);
//                }

                if (mCurrentIndex == 1) {
                    float aa = mRateAngleList.get(totalCnt - 1 - 0);
                    float fff = 0;
                    fff = mRateAngleList.get(totalCnt - 1);
                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - 0).mColorId));
                    canvas.drawArc(mArcRateRect, progress - fff + 1, aa, true, mArcRatePaint);

                    Log.e(TAG, String.valueOf(progress - fff + 1) + "~~~" + String.valueOf(progress - fff + 1 + aa));
                } else if (mCurrentIndex == 2) {
                    float aa1 = mRateAngleList.get(totalCnt - 1 - 1);
                    float fff1 = 0;
                    fff1 = mRateAngleList.get(totalCnt - 1) + mRateAngleList.get(totalCnt - 2) + 1;
                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - 1).mColorId));
                    canvas.drawArc(mArcRateRect, progress - fff1 + 1, aa1, true, mArcRatePaint);

                    float aa = mRateAngleList.get(totalCnt - 1 - 0);
                    float fff = 0;
                    fff = mRateAngleList.get(totalCnt - 1);
                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - 0).mColorId));
                    canvas.drawArc(mArcRateRect, progress - fff + 1, aa, true, mArcRatePaint);
                } else if (mCurrentIndex == 3) {
                    float aa2 = mRateAngleList.get(totalCnt - 1 - 2);
                    float fff2 = 0;
                    fff2 = mRateAngleList.get(totalCnt - 1) + mRateAngleList.get(totalCnt - 2) + mRateAngleList.get(totalCnt - 3) + 1 + 1;
                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - 2).mColorId));
                    canvas.drawArc(mArcRateRect, progress - fff2 + 1, aa2, true, mArcRatePaint);

                    float aa1 = mRateAngleList.get(totalCnt - 1 - 1);
                    float fff1 = 0;
                    fff1 = mRateAngleList.get(totalCnt - 1) + mRateAngleList.get(totalCnt - 2) + 1;
                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - 1).mColorId));
                    canvas.drawArc(mArcRateRect, progress - fff1 + 1, aa1, true, mArcRatePaint);

                    float aa = mRateAngleList.get(totalCnt - 1 - 0);
                    float fff = 0;
                    fff = mRateAngleList.get(totalCnt - 1);
                    mArcRatePaint.setColor(getResources().getColor(mArcInfoList.get(totalCnt - 1 - 0).mColorId));
                    canvas.drawArc(mArcRateRect, progress - fff + 1, aa, true, mArcRatePaint);
                }
            }
        }

        // 绘制背景圆形
        canvas.drawCircle(centerX, centerY, bgCircleRadius, mBgCirclePaint);
    }

    /**
     * 设置画笔属性
     *
     * @param paint
     * @param style
     */
    private void setPaintAttrs(Paint paint, Style style) {
        paint.setAntiAlias(true);
        paint.setStyle(style);
    }

    /**
     * 设置画笔属性
     *
     * @param paint
     * @param color
     * @param style
     */
    private void setPaintAttrs(Paint paint, int color, Style style) {
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(style);
    }

    /**
     * 属性动画调用，不断改变progress，从而重绘
     *
     * @param progress
     */
    private void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > CIRCLE_ANGLE) {
            progress = CIRCLE_ANGLE;
        }
        if (progress <= CIRCLE_ANGLE) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public float smallRateArcWidth() {
        return mSmallRateArcWidth;
    }

    public void setSmallRateArcWidth(float smallRateArcWidth) {
        this.mSmallRateArcWidth = smallRateArcWidth;
    }

    public float getBigRateArcWidth() {
        return mBigRateArcWidth;
    }

    public void setBigRateArcWidth(float bigRateArcWidth) {
        this.mSmallRateArcWidth = bigRateArcWidth;
    }

    public int getDrawArcTime() {
        return mDrawArcTime;
    }

    public void setDrawArcTime(int drawArcTime) {
        this.mDrawArcTime = drawArcTime;
    }

    public int getRotateTime() {
        return mRotateTime;
    }

    public void setRotateTime(int rotateTime) {
        this.mRotateTime = rotateTime;
    }

    /**
     * 设置动画效果
     */
    public void startAnimation(List<ArcInfo> arcInfoList) {
        if (arcInfoList == null && arcInfoList.size() == 0) {
            return;
        }

        if (mIsAnimating) {
            return;
        }

        mCurrentIndex = 0;
        mOriginTotalRateSum = 0;
        mArcInfoList.clear();
        mRateAngleList.clear();
        mSumRateAngleList.clear();
        for (ArcInfo arcInfo : arcInfoList) {
            if (arcInfo.mRate <= 0) {
                throw new IllegalArgumentException("Input ArcInfo.mRate must > 0!");
            }
            mArcInfoList.add((ArcInfo) arcInfo.clone());
        }

        initRateValues(mArcInfoList);
        calcAngles();

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "progress",
                0, CIRCLE_ANGLE);
        objectAnimator.setDuration(mDrawArcTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimating = false;
            }
        });
        objectAnimator.start();
    }

    private void initRateValues(List<ArcInfo> arcInfoList) {
        for (ArcInfo arcInfo : arcInfoList) {
            mOriginTotalRateSum += arcInfo.mRate;
        }
    }

    /**
     * 计算不同"比率"所占的角度和旋转角度
     */
    private void calcAngles() {
        // 实际总共角度=360减去所有空隙角度
        int gapAngleCnt = 0;
        if (mArcInfoList.size() == 1) {
            gapAngleCnt = 0;
        } else {
            gapAngleCnt = mArcInfoList.size();
        }
        int realTotalAngle = (CIRCLE_ANGLE - gapAngleCnt * mGapAngle);

        for (int i = 0; i < mArcInfoList.size(); i++) {
            ArcInfo arcInfo = mArcInfoList.get(i);

            double smallValue = (arcInfo.mRate / mOriginTotalRateSum) * realTotalAngle;
            float rateAngle = 0;
            if (smallValue < 1) { // 值小于1，则当做1处理
                rateAngle = 1;
            } else {
                rateAngle = getRateAngle(smallValue);
            }
            mRateAngleList.add(rateAngle);
        }

        adjustRateAngle();
        initSumRateAngle();
    }

    /**
     * 从后往前加
     */
    private void initSumRateAngle() {
        float sum = 0;
        for (int i = mRateAngleList.size() - 1; i >= 0 ; i--) {
            if (mRateAngleList.size() == 1) {
                sum += mRateAngleList.get(i);
                mSumRateAngleList.add(sum);
            } else {
                if (i == (mRateAngleList.size() - 1)) {
                    sum += mRateAngleList.get(i);
                } else {
                    sum += mRateAngleList.get(i) + 1;
                }
                mSumRateAngleList.add(sum);
            }
        }
    }

    /**
     * 微调角度
     */
    private void adjustRateAngle() {
        int maxAngleIndex = 0;
        float maxAngle = 0;
        float sumAngle = 0;
        for (int i = 0; i < mRateAngleList.size(); i++) {
            float currentAngle = mRateAngleList.get(i);
            sumAngle += currentAngle;

            if (currentAngle >= maxAngle) {
                maxAngle = currentAngle;
                maxAngleIndex = i;
            }
        }

        int gapAngleCnt = 0;
        if (mArcInfoList.size() == 1) {
            gapAngleCnt = 0;
        } else {
            gapAngleCnt = mArcInfoList.size();
        }

        int realTotalAngle = (CIRCLE_ANGLE - gapAngleCnt * mGapAngle);
        float sum = sumAngle - maxAngle;
        float newMaxAngle = realTotalAngle - sum;
        mRateAngleList.set(maxAngleIndex, newMaxAngle);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    private float getRateAngle(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        double angle = Double.parseDouble(df.format(value));
        return (float) angle;
    }

    public static class ArcInfo implements Cloneable{
        public double mRate;  // "圆弧"比率
        public int mColorId;  // "圆弧"颜色

        public ArcInfo(double rate, int colorId) {
            this.mRate = rate;
            this.mColorId = colorId;
        }

        @Override
        public Object clone() {
            ArcInfo arcInfo = null;
            try {
                arcInfo = (ArcInfo) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return arcInfo;
        }
    }
}