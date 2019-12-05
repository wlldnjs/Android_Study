
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by jiwonKim on 2019. 6. 11..
 */

class PentagonGraphView : View {

    /**
     * 다섯개 점의 값의 배열을 받는다.
     */
    // default setting
    lateinit var pointArray1: FloatArray
    lateinit var pointArray2: FloatArray

    private var centerX = 0f
    private var centerY = 0f
    private var correctionValue = 0f

    private var pointX = 0f
    private var pointY = 0f

    private val radius = 72f

    private val mPath = Path()

    // paint object
    private val yellowGreenPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.yellowgreen01_70per)
    }

    private val cyonPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.cyon03_70per)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        background = ContextCompat.getDrawable(context, R.drawable.roadmap_bg_allsubject_circlegraph)
        drawOutLineArc(canvas)
    }

    private fun drawOutLineArc(canvas: Canvas?) {
        centerX = width / 2f
        centerY = height / 2f
        correctionValue = centerX / 100f

        val maxValue = if (pointArray1.max()!! > pointArray2.max()!!) {
            pointArray1.max()!!
        } else {
            pointArray2.max()!!
        }

        val calibrateValue = if (maxValue.toInt() % 5 == 0) {
            if (maxValue.toInt() != 0) {
                100 / maxValue
            } else 0f
        } else {
            100 / (maxValue + 5)
        }

        val calibratePointArray1 = ArrayList<Float>()
        pointArray1.iterator().forEach {
            calibratePointArray1.add(it * calibrateValue)
        }

        val calibratePointArray2 = ArrayList<Float>()
        pointArray2.iterator().forEach {
            calibratePointArray2.add(it * calibrateValue)
        }

        drawPentagonGraph(calibratePointArray1, yellowGreenPaint, canvas)
        drawPentagonGraph(calibratePointArray2, cyonPaint, canvas)
    }

    private fun rotateBy(d: Float, y0: Float) {
        val dX = 0f
        val dY = -y0
        val rad = abs(Math.toRadians(d.toDouble()))
        val cosD = cos(rad).toFloat()
        val sinD = sin(rad).toFloat()

        pointX = (dX * cosD - dY * sinD) + centerX
        pointY = (dY * cosD + dX * sinD) + centerY
    }

    private fun drawPentagonGraph(pointArray: ArrayList<Float>, paint: Paint, canvas: Canvas?) {
        var radiusRotateValue = radius

        mPath.moveTo(centerX, centerY - (pointArray[0] * correctionValue))
        for (i in 1 until pointArray.size) {
            rotateBy(radiusRotateValue, (pointArray[i] * correctionValue))
            mPath.lineTo(pointX, pointY)
            radiusRotateValue += radius
        }
        mPath.lineTo(centerX, centerY - (pointArray[0] * correctionValue))

        canvas?.drawPath(mPath, paint)
        mPath.reset()
    }

    fun setPointArrayMe(abilityData: MyRoadMapSchoolingLearningEvaluationAbilityModel.AbilityData?) {
        pointArray2 = if (abilityData != null) {
            floatArrayOf(abilityData.understandingPoint, abilityData.analyticalPoint, abilityData.logicalPoint, abilityData.inferencePoint, abilityData.solvingPoint)
        } else {
            floatArrayOf(0f,0f,0f,0f,0f)
        }
    }

    fun setPointArrayFriends(abilityData: MyRoadMapSchoolingLearningEvaluationAbilityModel.AbilityData?) {
        pointArray1 = if (abilityData != null) {
            floatArrayOf(abilityData.understandingPoint, abilityData.analyticalPoint, abilityData.logicalPoint, abilityData.inferencePoint, abilityData.solvingPoint)
        } else {
            floatArrayOf(0f, 0f, 0f, 0f, 0f)
        }
    }
}