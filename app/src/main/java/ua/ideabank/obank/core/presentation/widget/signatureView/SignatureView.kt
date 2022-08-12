package ua.ideabank.obank.core.presentation.widget.signatureView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import ua.ideabank.obank.R
import ua.ideabank.obank.core.presentation.extention.dp
import java.io.ByteArrayOutputStream
import kotlin.math.max

class SignatureView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var canvasBmp: Canvas? = null
    private var ignoreTouch = false
    private var previousPoint: Point? = null
    private var startPoint: Point? = null
    private var currentPoint: Point? = null
    private var lastVelocity = 0f
    private var lastWidth = 0f
    private val paint: Paint
    private val paintBm: Paint
    private var bmp: Bitmap? = null
    private var layoutLeft = 0
    private var layoutTop = 0
    private var layoutRight = 0
    private var layoutBottom = 0
    private var drawViewRect: Rect? = null
    private var penColor = ContextCompat.getColor(context, R.color.signature_pen_color)
    private var backgroundColor = ContextCompat.getColor(context, R.color.white)
    private var penSize = 3.dp.toFloat()

    private var isEnableSignature = true

    private var mOnSignedListener: OnSignedListener? = null

    fun getPenColor(): Int {
        return penColor
    }

    fun setPenColor(penColor: Int) {
        this.penColor = penColor
        paint.color = penColor
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }

    fun clearCanvas() {
        previousPoint = null
        startPoint = null
        currentPoint = null
        lastVelocity = 0f
        lastWidth = 0f
        newBitmapCanvas(layoutLeft, layoutTop, layoutRight, layoutBottom)
        postInvalidate()
        mOnSignedListener?.onClear()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutLeft = left
        layoutTop = top
        layoutRight = right
        layoutBottom = bottom
        if (bmp == null) {
            newBitmapCanvas(layoutLeft, layoutTop, layoutRight, layoutBottom)
        } else if (changed) {
            resizeBitmapCanvas(bmp!!, layoutLeft, layoutTop, layoutRight, layoutBottom)
        }
    }

    private fun newBitmapCanvas(left: Int, top: Int, right: Int, bottom: Int) {
        bmp = null
        canvasBmp = null
        if (right - left > 0 && bottom - top > 0) {
            bmp = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888)
            canvasBmp = Canvas(bmp!!)
            canvasBmp!!.drawColor(backgroundColor)
        }
    }

    private fun resizeBitmapCanvas(bmp: Bitmap, left: Int, top: Int, right: Int, bottom: Int) {
        val newBottom = max(bottom, bmp.height)
        val newRight = max(right, bmp.width)
        newBitmapCanvas(left, top, newRight, newBottom)
        canvasBmp!!.drawBitmap(bmp, 0f, 0f, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnableSignature) {
            return false
        }
        if (event.pointerCount > 1) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                ignoreTouch = false
                drawViewRect = Rect(
                    this.left, this.top, this.right,
                    this.bottom
                )
                onTouchDownEvent(event.x, event.y)
                mOnSignedListener?.onStartSigning()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!drawViewRect!!.contains(
                        left + event.x.toInt(),
                        this.top + event.y.toInt()
                    )
                ) {
                    if (!ignoreTouch) {
                        ignoreTouch = true
                        onTouchUpEvent(event.x, event.y)
                    }
                } else {
                    if (ignoreTouch) {
                        ignoreTouch = false
                        onTouchDownEvent(event.x, event.y)
                    } else {
                        onTouchMoveEvent(event.x, event.y)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                onTouchUpEvent(event.x, event.y)
                mOnSignedListener?.onSigned()
            }
            else -> {
            }
        }
        return true
    }

    private fun onTouchDownEvent(x: Float, y: Float) {
        previousPoint = null
        startPoint = null
        currentPoint = null
        lastVelocity = 0f
        lastWidth = penSize
        currentPoint = Point(x, y, System.currentTimeMillis())
        previousPoint = currentPoint
        startPoint = previousPoint
        postInvalidate()
    }

    private fun onTouchMoveEvent(x: Float, y: Float) {
        if (previousPoint == null) {
            return
        }
        startPoint = previousPoint
        previousPoint = currentPoint
        currentPoint = Point(x, y, System.currentTimeMillis())
        var velocity = currentPoint!!.velocityFrom(previousPoint!!)
        velocity = VELOCITY_FILTER_WEIGHT * velocity + (1 - VELOCITY_FILTER_WEIGHT) * lastVelocity
        val strokeWidth = getStrokeWidth(velocity)
        drawLine(lastWidth, strokeWidth, velocity)
        lastVelocity = velocity
        lastWidth = strokeWidth
        postInvalidate()
    }

    private fun onTouchUpEvent(x: Float, y: Float) {
        if (previousPoint == null) {
            return
        }
        startPoint = previousPoint
        previousPoint = currentPoint
        currentPoint = Point(x, y, System.currentTimeMillis())
        drawLine(lastWidth, 0f, lastVelocity)
        postInvalidate()
    }

    private fun getStrokeWidth(velocity: Float): Float {
        return penSize - velocity * STROKE_DES_VELOCITY
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bmp!!, 0f, 0f, paintBm)
    }

    private fun drawLine(
        lastWidth: Float, currentWidth: Float,
        velocity: Float
    ) {
        val mid1 = midPoint(previousPoint, startPoint)
        val mid2 = midPoint(currentPoint, previousPoint)
        draw(
            mid1, previousPoint, mid2, lastWidth,
            currentWidth, velocity
        )
    }

    private fun getPt(n1: Float, n2: Float, perc: Float): Float {
        val diff = n2 - n1
        return n1 + diff * perc
    }

    private fun draw(
        p0: Point, p1: Point?, p2: Point, lastWidth: Float,
        currentWidth: Float, velocity: Float
    ) {
        if (canvasBmp != null) {
            var xa: Float
            var xb: Float
            var ya: Float
            var yb: Float
            var x: Float
            var y: Float
            val increment: Float =
                if (velocity > MIN_VELOCITY_BOUND && velocity < MAX_VELOCITY_BOUND) {
                DRAWING_CONSTANT - velocity * INCREMENT_CONSTANT
            } else {
                MIN_INCREMENT
            }
            var i = 0f
            while (i < 1f) {
                xa = getPt(p0.x, p1!!.x, i)
                ya = getPt(p0.y, p1.y, i)
                xb = getPt(p1.x, p2.x, i)
                yb = getPt(p1.y, p2.y, i)
                x = getPt(xa, xb, i)
                y = getPt(ya, yb, i)
                val strokeVal = lastWidth + (currentWidth - lastWidth) * i
                paint.strokeWidth = if (strokeVal < MIN_PEN_SIZE) MIN_PEN_SIZE else strokeVal
                canvasBmp!!.drawPoint(x, y, paint)
                i += increment
            }
        }
    }

    private fun midPoint(p1: Point?, p2: Point?): Point {
        return Point((p1!!.x + p2!!.x) / 2.0f, (p1.y + p2.y) / 2, (p1.time + p2.time) / 2)
    }

    val signatureBitmap: Bitmap?
        get() = if (bmp != null) {
            Bitmap.createScaledBitmap(bmp!!, bmp!!.width, bmp!!.height, true)
        } else {
            null
        }

    private fun getSignatureBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
    }

    fun setBitmap(bitmap: Bitmap?) {
        if (bitmap != null) {
            bmp = bitmap
            canvasBmp = Canvas(bitmap)
            postInvalidate()
        }
    }

    val isBitmapEmpty: Boolean
        get() {
            if (bmp != null) {
                val emptyBitmap = Bitmap.createBitmap(
                    bmp!!.width, bmp!!.height,
                    bmp!!.config
                )
                val canvasBmp = Canvas(emptyBitmap)
                canvasBmp.drawColor(backgroundColor)
                if (bmp!!.sameAs(emptyBitmap)) {
                    return true
                }
            }
            return false
        }

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, bmp)
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        setBitmap(state.bitmap)
    }

    internal class SavedState(superState: Parcelable?, var bitmap: Bitmap?) :
        BaseSavedState(superState) {

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeByteArray(compress(bitmap))
        }

        companion object {
            private fun compress(bitmap: Bitmap?): ByteArray {
                val stream = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                return stream.toByteArray()
            }

            private fun deCompress(byteArray: ByteArray?): Bitmap {
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            }
        }
    }

    companion object {
        const val MIN_PEN_SIZE = 1f
        private const val MIN_INCREMENT = 0.01f
        private const val INCREMENT_CONSTANT = 0.0005f
        private const val DRAWING_CONSTANT = 0.0085f
        const val MAX_VELOCITY_BOUND = 15f
        private const val MIN_VELOCITY_BOUND = 1.6f
        private const val STROKE_DES_VELOCITY = 1.0f
        private const val VELOCITY_FILTER_WEIGHT = 0.2f
    }

    init {
        setWillNotDraw(false)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = penColor
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = penSize
        paintBm = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBm.isAntiAlias = true
        paintBm.style = Paint.Style.STROKE
        paintBm.strokeJoin = Paint.Join.ROUND
        paintBm.strokeCap = Paint.Cap.ROUND
        paintBm.color = Color.BLACK
    }

    fun setOnSignedListener(listener: OnSignedListener) {
        mOnSignedListener = listener
    }

    interface OnSignedListener {
        fun onStartSigning()
        fun onSigned()
        fun onClear()
    }
}