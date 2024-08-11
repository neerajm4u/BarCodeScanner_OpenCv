package com.example.camera_x

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import org.opencv.core.Mat

class RectOverlay constructor(context: Context?, attributeSet: AttributeSet? ) :
    View(context, attributeSet) {
    private val rectBounds: MutableList<RectF> = mutableListOf()
    private lateinit var  mat : Mat
    private lateinit var  canvas: Canvas
    private lateinit var scale: MutableList<Float>

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context!!, android.R.color.black)
        strokeWidth = 10f
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val otherPaint = Paint()
        otherPaint.setColor(Color.WHITE);
        otherPaint.setStyle(Paint.Style.FILL);
        /* if(rectBounds.size >0) {
            canvas.drawRect(
                    (getLeft() + rectBounds[0].left).toFloat() * canvas.width,
                    (getTop() + rectBounds[0].top).toFloat()* canvas.height,
                    (getLeft() + (rectBounds[0].right)).toFloat()* canvas.width,
                    (getTop() +(rectBounds[0].bottom )).toFloat()* canvas.height, otherPaint);
        }*/
       // rectBounds.forEach { canvas.drawRect(it, paint) }

        val wallpaint = Paint()
        wallpaint.color = Color.RED
        wallpaint.style = Paint.Style.STROKE
        wallpaint.strokeWidth = 12f

        if(::mat.isInitialized && ::scale.isInitialized && scale.isNotEmpty()) {

            val wallpath: Path = Path()
            val xf =  scale[0]-0.1f// 2.25
            val yf =  scale[1]//3.39
            val xf2 =   (scale[0]+0.3f)// 2.25
            val yf2 = (scale[1] + 0.3f) //3.39

            wallpath.reset()
            wallpath.moveTo((mat[0, 0][0] * xf).toFloat(), (mat[0, 0][1].toFloat() * yf).toFloat())
            wallpath.lineTo((mat[0, 1][0].toFloat() * xf2).toFloat(), (mat[0, 1][1].toFloat() * yf).toFloat())
            wallpath.lineTo((mat[0, 2][0].toFloat() * xf2).toFloat(), (mat[0, 2][1].toFloat() * yf).toFloat())
            wallpath.lineTo((mat[0, 3][0].toFloat() * xf).toFloat(), (mat[0, 3][1].toFloat() * yf).toFloat())
            wallpath.lineTo((mat[0, 0][0].toFloat() * xf).toFloat(), (mat[0, 0][1].toFloat() * yf).toFloat())

            canvas.drawPath(wallpath, wallpaint)
        }
    }

    fun drawRect(rectBounds: List<RectF>?) {
        this.rectBounds.clear()
        if (rectBounds != null) {
            this.rectBounds.addAll(rectBounds)
        }
        invalidate()
    }

    fun drawPolygon(mat : Mat , scale:MutableList<Float>){
        this.mat = mat
        this.scale = scale
        invalidate()
    }
}