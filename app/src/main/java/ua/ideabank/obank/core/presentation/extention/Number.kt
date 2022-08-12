package ua.ideabank.obank.core.presentation.extention

import android.animation.TimeInterpolator
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

inline val Float.dp: Float get() = Resources.getSystem().displayMetrics.density * this

inline val Int.dp: Int get() = (Resources.getSystem().displayMetrics.density * this).toInt()

inline val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun Float.subFraction(
    range: ClosedFloatingPointRange<Float>,
    interpolator: TimeInterpolator? = null
): Float {
    return when {
        this in range -> {
            val innerFraction = (this - range.start) / (range.endInclusive - range.start)

            interpolator?.getInterpolation(innerFraction)?.coerceIn(0.0F..1.0F) ?: innerFraction
        }
        this < range.start -> 0.0F
        else -> 1.0F
    }
}

private val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
private val decimalFormat = numberFormat as DecimalFormat

fun Double.round(digitAfterDot: Int): Double {
    decimalFormat.minimumFractionDigits = digitAfterDot
    decimalFormat.maximumFractionDigits = digitAfterDot

    val str = decimalFormat.format(this)
    return str.toDouble()
}

fun Double.roundString(digitAfterDot: Int): String {
    decimalFormat.minimumFractionDigits = digitAfterDot
    decimalFormat.maximumFractionDigits = digitAfterDot
    decimalFormat.isGroupingUsed = false
    return decimalFormat.format(this)
}