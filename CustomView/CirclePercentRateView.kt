
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by Kimjiwon on 2018. 10. 23..
 */

class CirclePercentRateView : View {

    val START_ANGLE_LEFT: Float = -180f
    val START_ANGLE_TOP: Float = -90f

    // default setting
    private var mStrokeWidth: Float = Util.dipToPix(context, 10f)
    private var mMaxProgress: Int = 100
    private var mProgressColor: Int = Color.WHITE
    private var mProgressStartColor: Int = Color.WHITE
    private var mProgressEndColor: Int = Color.WHITE
    private var mProgress: Int = 0
    private var mBackgroundColor: Int = ContextCompat.getColor(context, R.color.gray07)

    private var mIsGradient: Boolean = false
    private var mIsStrokeCap: Boolean = false

    private val mBackgroundPaint: Paint = Paint()
    private val mForegroundPaint: Paint = Paint()

    private var mIsAnimation: Boolean = false
    private var mIsAnimationDuration: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(attrs, defStyleAttr)
    }

    fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentRateView)
        setTypeArray(typedArray)
    }

    fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentRateView, defStyleAttr, 0)
        setTypeArray(typedArray)
    }

    fun setTypeArray(typedArray: TypedArray) {
        mStrokeWidth = typedArray.getInt(R.styleable.CirclePercentRateView_strokeWidth, Util.dipToPix(context, 10)).toFloat()
        mMaxProgress = typedArray.getInt(R.styleable.CirclePercentRateView_maxProgress, 100)
        mProgress = typedArray.getInt(R.styleable.CirclePercentRateView_progress, 0)
        mProgressStartColor = typedArray.getColor(R.styleable.CirclePercentRateView_progressStartColor, ContextCompat.getColor(context, R.color.white))
        mProgressEndColor = typedArray.getColor(R.styleable.CirclePercentRateView_progressEndColor, ContextCompat.getColor(context, R.color.white))
        mProgressColor = typedArray.getColor(R.styleable.CirclePercentRateView_progressColor, ContextCompat.getColor(context, R.color.white))
        mBackgroundColor = typedArray.getColor(R.styleable.CirclePercentRateView_circleBackgroundColor, ContextCompat.getColor(context, R.color.gray07))
        mIsStrokeCap = typedArray.getBoolean(R.styleable.CirclePercentRateView_isStrokeCap, false)
        mIsGradient = typedArray.getBoolean(R.styleable.CirclePercentRateView_isGradiant, false)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        drawOutLineArc(canvas)
    }

    private fun drawOutLineArc(canvas: Canvas?) {
        // drawing area
        val rect = RectF(mStrokeWidth / 2, mStrokeWidth / 2, width - mStrokeWidth / 2, height - mStrokeWidth / 2)

        // set Paint
        mBackgroundPaint.style = Paint.Style.STROKE
        mBackgroundPaint.strokeWidth = mStrokeWidth
        mBackgroundPaint.isAntiAlias = true

        // background
        mBackgroundPaint.color = mBackgroundColor
        canvas?.drawArc(rect, 0f, 360f, false, mBackgroundPaint)

        mForegroundPaint.style = Paint.Style.STROKE
        mForegroundPaint.strokeWidth = mStrokeWidth
        mForegroundPaint.isAntiAlias = true

        // foreground
        if (mIsStrokeCap) {
            mForegroundPaint.strokeCap = Paint.Cap.ROUND
        } else {
            mForegroundPaint.strokeCap = Paint.Cap.BUTT
        }

        if (mIsGradient) {
            mForegroundPaint.shader = SweepGradient((width / 2).toFloat(), (height / 2).toFloat(), mProgressStartColor, mProgressEndColor).also { rotation = START_ANGLE_TOP }
        } else {
            mForegroundPaint.color = mProgressColor
        }

        val startAngle: Float = if (mIsGradient) {
            mStrokeWidth / 2
        } else {
            START_ANGLE_TOP - mStrokeWidth / 4
        }

        val sweepAngle: Float = when (mProgress) {
            mMaxProgress -> 360f
            0 -> 0f
            else -> (360f / mMaxProgress) * mProgress - mStrokeWidth / 3
        }

        canvas?.drawArc(rect, startAngle, sweepAngle, false, mForegroundPaint)
    }

    fun setProgress(progress: Int) {
        mProgress = progress
        invalidate()
    }
}