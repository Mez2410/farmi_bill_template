package mez.com.base.farmi_bill

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View

class FarmiBillLayout : View {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    val billHeight: Int get() = this.height

    private fun Float.dpToPx() = this * resources.displayMetrics.density
    private fun Int.dpToPx() = (this * resources.displayMetrics.density).toInt()
    private fun Float.spToPx() = this * resources.displayMetrics.scaledDensity
    private fun Int.pxToDp() = (this * 160) / resources.displayMetrics.densityDpi

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawHeader(canvas)
        }
    }

    fun drawHeader(canvas: Canvas?) {
        val xMargin = 10f
        val rightMargin = width.toFloat() - xMargin.dpToPx()
        val topMargin = 10f
        val bottomMargin = 0f

        val paint = Paint()
        val text = "Farmi Service Trading Co., Ltd."
        val rect = RectF(10f, 10f, rightMargin, 60f)

        paint.textSize = 16f.spToPx()
        paint.color = Color.BLACK
        paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC)
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(text, 0, text.length, rect.centerX(), rect.centerY() + 15, paint)

        val paint2 = Paint()
        val text2 = "So dien thoai: 0339022024"
        val rect2 = RectF(20f, 20f, 0f, 0f)
//        paint2.textSize = 10f.spToPx()
//        paint2.color = Color.BLACK
//        paint2.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
//        paint2.textAlign = Paint.Align.CENTER
//        canvas?.drawText(text2, 0, text2.length, rect2.centerX(), rect2.centerY() + 15, paint2)
//
//        val paint3 = Paint()
//        val text3 = "Email: farmi.support@gmail.vn"
//        val rect3 = RectF(0f, 5f, 0f, 0f)
//        paint3.textSize = 10f.spToPx()
//        paint3.color = Color.BLACK
//        paint3.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
//        paint3.textAlign = Paint.Align.CENTER
//        canvas?.drawText(text3, 0, text3.length, rect3.centerX(), rect3.centerY() + 15, paint3)
    }
}