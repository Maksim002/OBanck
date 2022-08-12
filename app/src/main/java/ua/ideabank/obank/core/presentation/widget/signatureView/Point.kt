package ua.ideabank.obank.core.presentation.widget.signatureView

import kotlin.math.pow
import kotlin.math.sqrt

class Point(val x: Float, val y: Float, val time: Long) {
    private fun distanceTo(start: Point): Float {
        return sqrt(
            (x - start.x).toDouble().pow(2.0) + (y - start.y).toDouble().pow(2.0)
        )
            .toFloat()
    }

    fun velocityFrom(start: Point): Float {
        return distanceTo(start) / (time - start.time)
    }
}