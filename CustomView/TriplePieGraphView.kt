
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by jiwonKim on 2019. 6. 3..
 */

class TriplePieGraphView : View {

    private val START_ANGLE_TOP: Float = -90f

    /**
     * 바깥쪽 원부터 first, second, third
     */
    // default setting
    private var mFirstCircleProgress: Int = 0
    private var mSecondCircleProgress: Int = 0
    private var mThirdCircleProgress: Int = 0
    private var mStrokeWidth: Float = Util.dipToPix(context,25f)

    // paint object
    private val mFirstBackgroundPaint = Paint().apply {
        setPaintInit(this, ContextCompat.getColor(context, R.color.bluegraylight00))
    }
    private val mSecondBackgroundPaint = Paint().apply {
        setPaintInit(this, ContextCompat.getColor(context, R.color.sky_blue07))
    }
    private val mThirdBackgroundPaint = Paint().apply {
        setPaintInit(this, ContextCompat.getColor(context, R.color.yellowgreen05))
    }
    private val mFirstForegroundPaint = Paint().apply {
        setPaintInit(this, ContextCompat.getColor(context, R.color.bluegray00))
    }
    private val mSecondForegroundPaint = Paint().apply {
        setPaintInit(this, ContextCompat.getColor(context, R.color.cyon03), ContextCompat.getColor(context, R.color.cobaltblue05))
    }
    private val mThirdForegroundPaint = Paint().apply {
        setPaintInit(this, ContextCompat.getColor(context, R.color.yellowgreen04), ContextCompat.getColor(context, R.color.yellowgreen03))
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TriplePieGraphView)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TriplePieGraphView, defStyleAttr, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        mFirstCircleProgress = typedArray.getInt(R.styleable.TriplePieGraphView_firstProgress, 0)
        mSecondCircleProgress = typedArray.getInt(R.styleable.TriplePieGraphView_secondProgress, 0)
        mThirdCircleProgress = typedArray.getInt(R.styleable.TriplePieGraphView_thirdProgress, 0)
        typedArray.recycle()
    }

    private fun setPaintInit(paint: Paint, color: Int?) {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = mStrokeWidth
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            color?.let {
                this.color = it
            }
        }
    }

    private fun setPaintInit(paint: Paint, startColor: Int, endColor: Int) {
        paint.shader = SweepGradient((width / 2).toFloat(), (height / 2).toFloat(), startColor, endColor)
        setPaintInit(paint, null)
    }

    override fun onDraw(canvas: Canvas?) {
        drawOutLineArc(canvas)
    }

    private fun drawOutLineArc(canvas: Canvas?) {
        // init Rect
        val firstRect = RectF(mStrokeWidth / 2, mStrokeWidth / 2, width - mStrokeWidth / 2, height - mStrokeWidth / 2)
        val secondRect = RectF(mStrokeWidth * 1.5f + 5, mStrokeWidth * 1.5f + 5, width - mStrokeWidth * 1.5f - 5, height - mStrokeWidth * 1.5f - 5)
        val thirdRect = RectF(mStrokeWidth * 2.5f + 10, mStrokeWidth * 2.5f + 10, width - mStrokeWidth * 2.5f - 10, height - mStrokeWidth * 2.5f - 10)

        // background area
        canvas?.drawArc(firstRect, 0f, 360f, false, mFirstBackgroundPaint)
        canvas?.drawArc(secondRect, 0f, 360f, false, mSecondBackgroundPaint)
        canvas?.drawArc(thirdRect, 0f, 360f, false, mThirdBackgroundPaint)

        // foreground area
        canvas?.drawArc(firstRect, START_ANGLE_TOP, 3.6f * mFirstCircleProgress, false, mFirstForegroundPaint)
        canvas?.drawArc(secondRect, START_ANGLE_TOP, 3.6f * mSecondCircleProgress, false, mSecondForegroundPaint)
        canvas?.drawArc(thirdRect, START_ANGLE_TOP, 3.6f * mThirdCircleProgress, false, mThirdForegroundPaint)

        invalidate()
    }

    fun setFirstProgress(progress: Int) {
        mFirstCircleProgress = progress
    }

    fun setSecondProgress(progress: Int) {
        mSecondCircleProgress = progress
    }

    fun setThirdProgress(progress: Int) {
        mThirdCircleProgress = progress
    }

}