package com.example.shuiai.defineroundimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author shuiai@dianjia.io
 * @Company 杭州木瓜科技有限公司
 * @date 2016/10/17
 */

public class DefineRoundImage extends View {
    /**
     * 圆角半径
     */
    private int radious;
    /**
     * 图片类型
     */
    private int mType;
    /**
     * 图片引用
     */
    private Bitmap mBitamap;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * View的宽
     */
    private int width;
    /**
     * View的高
     */
    private int height;

    public DefineRoundImage(Context context) {
        this(context, null);
    }

    public DefineRoundImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefineRoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DefineRoundImage, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DefineRoundImage_radious:
                    radious = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DefineRoundImage_type:
                    mType = a.getInt(attr, 0);
                    break;
                case R.styleable.DefineRoundImage_src:
                    mBitamap = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
            }
        }
        a.recycle();
        initResource();
    }

    private void initResource() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int disWidth = getPaddingLeft() + getPaddingRight() + mBitamap.getWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(disWidth, widthSize);
            } else {
                width = disWidth;
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int disHeight = getPaddingBottom() + getPaddingTop() + mBitamap.getHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(disHeight, heightSize);
            } else {
                height = disHeight;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mType) {
            case 0://圆
                //画一个Dis图
                int min = Math.min(width, height);
                Bitmap bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
                Canvas mCanvas = new Canvas(bitmap);
                mCanvas.drawCircle(min / 2, min / 2, min / 2, mPaint);
                canvas.drawBitmap(bitmap, 0, 0, mPaint);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                mCanvas.drawBitmap(mBitamap, 0, 0, mPaint);
                break;
            case 1:
//                带圆角
                Bitmap roundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas roundCanvas = new Canvas(roundBitmap);
                roundCanvas.drawRoundRect(new RectF(0, 0, mBitamap.getWidth(), mBitamap.getHeight()), radious, radious, mPaint);
                canvas.drawBitmap(roundBitmap, 0, 0, mPaint);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                roundCanvas.drawBitmap(mBitamap, 0, 0, mPaint);
                break;
        }
    }
}
