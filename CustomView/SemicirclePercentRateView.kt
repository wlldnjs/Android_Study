
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by jiwonKim on 2019. 5. 24..
 */

class SemicirclePercentRateView : View {

    private val START_ANGLE_LEFT: Float = -180f

    // default setting
    private var mStrokeWidth: Float = Util.dipToPix(context, 28f)
    private var mMaxProgress: Int = 100
    private var mProgressColor: Int = ContextCompat.getColor(context, R.color.cyon03)
    private var mProgress: Int = 0
    private var mBackgroundColor: Int = ContextCompat.getColor(context, R.color.gray07)
    private var mBackgroundProgress: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SemicirclePercentRateView)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SemicirclePercentRateView, defStyleAttr, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        mStrokeWidth = typedArray.getInt(R.styleable.SemicirclePercentRateView_strokeWidth, Util.dipToPix(context, 28)).toFloat()
        mMaxProgress = typedArray.getInt(R.styleable.SemicirclePercentRateView_maxProgress, 100)
        mProgress = typedArray.getInt(R.styleable.SemicirclePercentRateView_progress, 0)
        mBackgroundProgress = typedArray.getInt(R.styleable.SemicirclePercentRateView_backgroundProgress, 0)
        mProgressColor = typedArray.getColor(R.styleable.SemicirclePercentRateView_progressColor, ContextCompat.getColor(context, R.color.cyon03))
        mBackgroundColor = typedArray.getColor(R.styleable.SemicirclePercentRateView_circleBackgroundColor, ContextCompat.getColor(context, R.color.bluegray00))
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        drawOutLineArc(canvas)
    }

    private fun drawOutLineArc(canvas: Canvas?) {
        // drawing area
        val rect = RectF(mStrokeWidth / 2 + 5f, mStrokeWidth / 2 + 5f, width - mStrokeWidth / 2 - 5f, height * 2 - mStrokeWidth / 2 - 5f)

        // outLine
        val outLineRect = RectF(1f, 1f, width - 1f, height * 2 - 1f)
        val outLinePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = Util.dipToPix(context, 3f)
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.bluegray00)
        }
        canvas?.drawArc(outLineRect, START_ANGLE_LEFT, 180f, false, outLinePaint)

        // inLine
        val inLineRect = RectF(mStrokeWidth + 9f, mStrokeWidth + 9f, width - mStrokeWidth - 9f, height * 2 - mStrokeWidth - 9f)
        val inLinePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = Util.dipToPix(context, 3f)
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.bluegray00)
        }
        canvas?.drawArc(inLineRect, START_ANGLE_LEFT, 180f, false, inLinePaint)

        // background
        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = mStrokeWidth
        backgroundPaint.isAntiAlias = true
        backgroundPaint.strokeCap = Paint.Cap.ROUND
        backgroundPaint.color = mBackgroundColor
        canvas?.drawArc(rect, START_ANGLE_LEFT, (180f / mMaxProgress) * mBackgroundProgress, false, backgroundPaint)

        // foreground
        val foregroundPaint = Paint()
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = mStrokeWidth
        foregroundPaint.isAntiAlias = true
        foregroundPaint.strokeCap = Paint.Cap.ROUND
        foregroundPaint.color = mProgressColor
        canvas?.drawArc(rect, START_ANGLE_LEFT, (180f / mMaxProgress) * mProgress, false, foregroundPaint)

        // dotLine
        val dotLinePaint = Paint()
        dotLinePaint.style = Paint.Style.STROKE
//        dotLinePaint.pathEffect = ComposePathEffect(CornerPathEffect(20f),DashPathEffect(floatArrayOf(3f, 2f),0f))
        // 첫번째 들어가는 float 배열은 점선의 크기를 정하는 값. 첫번째는 점선의 길이값이며 두번째는 점선사이의 간격 값. phase는 화면에서 첫번째 Dash가 얼만큼 잘려서 그려져도 되는지 시작값
        dotLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 3f), 0f)
        dotLinePaint.strokeWidth = Util.dipToPix(context, 1f)
        dotLinePaint.isAntiAlias = true
        dotLinePaint.color = ContextCompat.getColor(context, R.color.bluegray01)
        canvas?.drawArc(rect, START_ANGLE_LEFT, 180f, false, dotLinePaint)
    }

    fun setProgress(progress: Int?) {
        mProgress = progress ?: 0
        invalidate()
    }

    fun setBackgroundProgress(backgroundProgress: Int?) {
        mBackgroundProgress = backgroundProgress ?: 0
        invalidate()
    }
}