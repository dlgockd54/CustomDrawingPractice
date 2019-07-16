package com.example.customdrawingpractice.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import com.example.customdrawingpractice.R

/**
 * Created by hclee on 2019-07-16.
 */

class CustomProgressBar : ProgressBar {
    companion object {
        val TAG: String = CustomProgressBar::class.java.simpleName
    }

    var mCurValue: Int = 0
        set(value) {
            field = value
            requestLayout() // 값이 바뀔 때마다 measure()부터 onLayout()까지 다시 불리도록해서 뷰의 크기를 재설정한다.
            invalidate() // 값이 바뀔 때마다 새로 결정된 뷰의 크기에 맞도록 다시 그려준다.
        }
    var mMaxValue: Int = 0
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }
    var mLineColor: Int = 0
        set(value) {
            field = value
            mCirclePaint.color = value
            requestLayout()
            invalidate()
        }
    private var mTextPaint: Paint = Paint()
    private var mCirclePaint: Paint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0)

        mCurValue = typedArray.getInteger(R.styleable.CustomProgressBar_curValue, 0)
        mMaxValue = typedArray.getInteger(R.styleable.CustomProgressBar_maxValue, 100)
        mLineColor = typedArray.getColor(R.styleable.CustomProgressBar_lineColor, 0xff0000)

        // onDraw()에서 drawing object를 만드는 것은 성능 저하를 일으키기 떄문에 생성자에서 미리 초기화를 해두고
        // 중간에 값이 바뀌는 경우는 setter를 이용하도록 한다.
        mTextPaint.run {
            color = Color.BLACK
            textSize = 100f
            textAlign = Paint.Align.CENTER
        }
        mCirclePaint.run {
            color = mLineColor
            strokeWidth = 10f
            isAntiAlias = false
            style = Paint.Style.STROKE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure()")

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout()")
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw()")

        canvas?.let {
            val width: Int = measuredWidth
            val height: Int = measuredHeight

            canvas.drawArc(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                -90f,
                (mCurValue.toFloat() / mMaxValue.toFloat() * 360),
                false,
                mCirclePaint
            )
            canvas.drawText(
                "$mCurValue / $mMaxValue",
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                mTextPaint
            )

            if (mCurValue < mMaxValue) {
                postDelayed({
                    mCurValue++
                }, 1000)
            }
        }
    }
}