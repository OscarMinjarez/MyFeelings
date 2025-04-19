package minjarez.oscar.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import minjarez.oscar.myfeelings.R

class CustomBarDrawable : Drawable {

    var coorditanes: RectF? = null
    var context: Context? = null
    var emotion: Emotion? = null

    constructor(context: Context, emotion: Emotion) {
        this.context = context
        this.emotion = emotion
    }

    override fun draw(canvas: Canvas) {
        val background = Paint()
        background.style = Paint.Style.FILL
        background.isAntiAlias = true
        background.color = this.context?.resources?.getColor(R.color.gray) ?: R.color.gray
        val width: Float = (canvas.width - 10).toFloat()
        val height: Float = (canvas.height - 10).toFloat()
        canvas.drawRect(coorditanes!!, background)
        if (this.emotion != null) {
            val percentage: Float = this.emotion!!.percentage * (canvas.width - 10) / 100
            val coordinatesTwo = RectF(0.0f, 0.0f, percentage, height)
            var section = Paint()
            section.style = Paint.Style.FILL
            section.isAntiAlias = true
            section.color = ContextCompat.getColor(this.context!!, emotion!!.color)
            canvas.drawRect(coordinatesTwo!!, section)
        }
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }
}