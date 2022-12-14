package ua.ideabank.obank.core.presentation.extention

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer

fun PdfRenderer.Page.render(width: Int): Bitmap {
    val bitmap = createBitmap(width * 2)
    render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    close()
    return bitmap
}

private fun PdfRenderer.Page.createBitmap(bitmapWidth: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(
        bitmapWidth, (bitmapWidth.toFloat() / width * height).toInt(), Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    canvas.drawBitmap(bitmap, 0f, 0f, null)

    return bitmap
}