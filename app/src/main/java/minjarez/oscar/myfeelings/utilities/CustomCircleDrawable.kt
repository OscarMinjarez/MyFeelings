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

class CustomCircleDrawable : Drawable {

    var coordinates: RectF? = null
    var sweptAngle: Float = 0.0f
    var startAngle: Float = 0.0f
    var metricWeight: Int = 0
    var backgroundWeight: Int = 0
    var context: Context? = null
    var emotions: ArrayList<Emotion> = ArrayList()

    constructor(context: Context, emotions: ArrayList<Emotion>) {
        this.context = context
        this.metricWeight = context.resources.getDimension(R.dimen.graphWith) as Int
        this.backgroundWeight = context.resources.getDimension(R.dimen.graphBackground) as Int
        this.emotions = emotions
    }

    override fun draw(p0: Canvas) {
        val background = Paint()
        background.style = Paint.Style.STROKE
        background.strokeWidth = (this.backgroundWeight).toFloat()
        background.isAntiAlias = true
        background.strokeCap = Paint.Cap.ROUND
        background.color = context?.resources?.getColor(R.color.gray) ?: R.color.gray
        val width: Float = (p0.width - 25).toFloat()
        val height: Float = (p0.height - 25).toFloat()
        this.coordinates = RectF(25.0f, 25.0f, width, height)
        p0.drawArc(this.coordinates!!, 0.0f, 360.0f, false, background)
        if (this.emotions.size != 0) {
            for (emotion in emotions) {
                this.sweptAngle = (emotion.percentage * 360) / 100
                var section = Paint()
                section.style = Paint.Style.STROKE
                section.isAntiAlias = true
                section.strokeWidth = (this.metricWeight).toFloat()
                section.strokeCap = Paint.Cap.SQUARE
                section.color = ContextCompat.getColor(this.context!!, emotion.color)
                p0.drawArc(this.coordinates!!, this.startAngle, this.sweptAngle, false, section)
                this.startAngle += this.sweptAngle
            }
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