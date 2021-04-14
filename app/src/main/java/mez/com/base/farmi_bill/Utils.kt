package mez.com.base.farmi_bill

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.withTranslation
import androidx.core.util.lruCache

/**
 * const for printer has page size 48mm
 */
const val WIDTH_PAGE = 136

const val FONT_SIZE_7F = 7f
const val FONT_SIZE_8F = 8f
const val FONT_SIZE_9F = 9f

const val ALIGN_LEFT_X_AXIS = 2f
const val ALIGN_RIGHT_X_AXIS = 135f
const val ALIGN_CENTER_X_AXIS = 68f

const val WIDTH_MATCH_PARENT = 135
const val WIDTH_QUANTITY = 15
const val WIDTH_PRICES = 60

const val MARGIN_TOP_HEADER = 10f
const val MARGIN_TOP_FOOTER = 10f
const val MARGIN_TOP_ELEMENT_FOOTER = 15f
const val MARGIN_TOP_PRODUCT_LIST = 13 * MARGIN_TOP_HEADER + 5f

fun Canvas.drawPhoneOrEmail(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignCenter(FONT_SIZE_7F, Typeface.MONOSPACE, Typeface.NORMAL)
    this.drawText(text, ALIGN_CENTER_X_AXIS,  yAxis, paint)
}

fun Canvas.drawInvoiceInfo(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignCenter(FONT_SIZE_9F, Typeface.MONOSPACE, Typeface.NORMAL)
    this.drawText(text, ALIGN_CENTER_X_AXIS, yAxis, paint)
}

fun Canvas.drawCreatorInfo(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignRight(FONT_SIZE_7F, Typeface.MONOSPACE, Typeface.NORMAL)
    this.drawMultilineText(text, paint, WIDTH_MATCH_PARENT, ALIGN_RIGHT_X_AXIS, yAxis)
}

fun Canvas.drawLineItem(paint: Paint, yAxis: Float) {
    this.drawLine(0f, yAxis, 136f, yAxis, paint)
}

fun Canvas.drawProductName(paint: TextPaint, text: String, yAxis: Float): Float {
    paint.paintAlignLeft(FONT_SIZE_7F, Typeface.SANS_SERIF, Typeface.BOLD)
    return this.drawMultilineText(text, paint, WIDTH_MATCH_PARENT, ALIGN_LEFT_X_AXIS, yAxis)
}

fun Canvas.drawProductQuantity(paint: TextPaint, text: String, yAxis: Float){
    paint.paintAlignLeft(FONT_SIZE_7F, Typeface.SANS_SERIF, Typeface.NORMAL)
    this.drawMultilineText(text, paint, WIDTH_QUANTITY, ALIGN_LEFT_X_AXIS, yAxis)
}

fun Canvas.drawOriginPrice(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignCenter(FONT_SIZE_7F, Typeface.SANS_SERIF, Typeface.NORMAL)
    this.drawMultilineText(text, paint, WIDTH_PRICES, ALIGN_CENTER_X_AXIS, yAxis)
}

fun Canvas.drawAmountPrice(paint: TextPaint, text: String, yAxis: Float) : Float {
    paint.paintAlignRight(FONT_SIZE_7F, Typeface.SANS_SERIF, Typeface.NORMAL)
    return this.drawMultilineText(text, paint, WIDTH_PRICES, ALIGN_RIGHT_X_AXIS, yAxis)
}

fun Canvas.drawDiscountPrice(paint: TextPaint, text: String, yAxis: Float): Float {
    paint.paintAlignRight(FONT_SIZE_7F, Typeface.SANS_SERIF, Typeface.NORMAL)
    return this.drawMultilineText(text, paint, WIDTH_MATCH_PARENT, ALIGN_RIGHT_X_AXIS, yAxis)
}

fun Canvas.drawTotalFtLbl(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignLeft(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
    this.drawText(text, ALIGN_LEFT_X_AXIS, yAxis, paint)
}

fun Canvas.drawTotalFtValue(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignRight(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
    this.drawText(text, ALIGN_RIGHT_X_AXIS, yAxis, paint)
}

fun Canvas.drawPaymentTypeLbl(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignLeft(FONT_SIZE_8F, Typeface.SANS_SERIF, Typeface.NORMAL)
    this.drawText(text, ALIGN_LEFT_X_AXIS, yAxis, paint)
}

fun Canvas.drawPaymentTypeValue(paint: TextPaint, text: String, yAxis: Float) {
    paint.paintAlignRight(FONT_SIZE_8F, Typeface.SANS_SERIF, Typeface.NORMAL)
    this.drawText(text, ALIGN_RIGHT_X_AXIS, yAxis, paint)
}

@SuppressLint("WrongConstant")
@RequiresApi(Build.VERSION_CODES.O)
fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    ellipsize: TextUtils.TruncateAt? = null,
    maxLines: Int = Int.MAX_VALUE,
    breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
    hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE,
    justificationMode: Int = Layout.JUSTIFICATION_MODE_NONE) {

    val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
            "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize-" +
            "$maxLines-$breakStrategy-$hyphenationFrequency-$justificationMode"

    val staticLayout = StaticLayoutCache[cacheKey] ?:
    StaticLayout.Builder.obtain(text, start, end, textPaint, width)
        .setAlignment(alignment)
        .setTextDirection(textDir)
        .setLineSpacing(spacingAdd, spacingMult)
        .setIncludePad(includePad)
        .setEllipsizedWidth(ellipsizedWidth)
        .setEllipsize(ellipsize)
        .setMaxLines(maxLines)
        .setBreakStrategy(breakStrategy)
        .setHyphenationFrequency(hyphenationFrequency)
        .setJustificationMode(justificationMode)
        .build().apply { StaticLayoutCache[cacheKey] = this }

    staticLayout.draw(this, x, y)
}

@SuppressLint("WrongConstant")
@RequiresApi(Build.VERSION_CODES.M)
fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    ellipsize: TextUtils.TruncateAt? = null,
    maxLines: Int = Int.MAX_VALUE,
    breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
    hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE) {

    val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
            "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize-" +
            "$maxLines-$breakStrategy-$hyphenationFrequency"

    val staticLayout = StaticLayoutCache[cacheKey] ?:
    StaticLayout.Builder.obtain(text, start, end, textPaint, width)
        .setAlignment(alignment)
        .setTextDirection(textDir)
        .setLineSpacing(spacingAdd, spacingMult)
        .setIncludePad(includePad)
        .setEllipsizedWidth(ellipsizedWidth)
        .setEllipsize(ellipsize)
        .setMaxLines(maxLines)
        .setBreakStrategy(breakStrategy)
        .setHyphenationFrequency(hyphenationFrequency)
        .build().apply { StaticLayoutCache[cacheKey] = this }

    staticLayout.draw(this, x, y)
}

fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    ellipsize: TextUtils.TruncateAt? = null) : Float {

    val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-" +
            "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize"

    // The public constructor was deprecated in API level 28,
    // but the builder is only available from API level 23 onwards
    val staticLayout = StaticLayoutCache[cacheKey] ?:
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(text, start, end, textPaint, width)
            .setAlignment(alignment)
            .setLineSpacing(spacingAdd, spacingMult)
            .setIncludePad(includePad)
            .setEllipsizedWidth(ellipsizedWidth)
            .setEllipsize(ellipsize)
            .build()
    } else {
        StaticLayout(text, start, end, textPaint, width, alignment,
            spacingMult, spacingAdd, includePad, ellipsize, ellipsizedWidth)
            .apply { StaticLayoutCache[cacheKey] = this }
    }

    staticLayout.draw(this, x, y)
    return staticLayout.height.toFloat()
}

private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
    canvas.withTranslation(x, y) {
        draw(this)
    }
}

fun Paint.paintAlignLeft(textSize: Float, fontFamily: Typeface, fontStyle: Int) {
    this.textSize = textSize
    this.typeface = Typeface.create(fontFamily, fontStyle)
    this.textAlign = Paint.Align.LEFT
    this.color = Color.BLACK
}

fun Paint.paintAlignCenter(textSize: Float, fontFamily: Typeface, fontStyle: Int) {
    this.textSize = textSize
    this.typeface = Typeface.create(fontFamily, fontStyle)
    this.textAlign = Paint.Align.CENTER
    this.color = Color.BLACK
}

fun Paint.paintAlignRight(textSize: Float, fontFamily: Typeface, fontStyle: Int) {
    this.textSize = textSize
    this.typeface = Typeface.create(fontFamily, fontStyle)
    this.textAlign = Paint.Align.RIGHT
    this.color = Color.BLACK
}

fun getProductsInfo(): List<OrderModel> {
    val list = arrayListOf<OrderModel>()
    list.add(OrderModel("Pizza Seafood Deluxe Pizza 4P’s Pizza Seafood Deluxe ", 3, "100,000", "300,000", "150,000"))
    list.add(OrderModel("Bí đỏ Nhật Hướng Dương - (200g) Pizza Seafood Deluxe ", 4, "100,000", "300,000", "150,000"))
    list.add(OrderModel("Organic Garden Salad - F... (1 hộp) ", 2, "100,000", "300,000", "150,000"))
    list.add(OrderModel("Frozen Minced Beef - ACE Foods", 3, "100,000", "300,000"))
    list.add(OrderModel("Pizza Seafood Deluxe Pizza 4P’s", 3, "100,000", "300,000", "150,000"))
    list.add(OrderModel("Bí đỏ Nhật Hướng Dương - (200g)", 4, "100,000", "300,000"))
    list.add(OrderModel("Organic Garden Salad - F... (1 hộp) Pizza Seafood Deluxe ", 2, "100,000", "300,000"))
    list.add(OrderModel("Club Soda - Evervess - (24 x 330 ml)", 1, "100,000", "300,000", "150,000"))
    list.add(OrderModel("Schweppes Taste of Pink Grapefruit", 4, "100,000", "300,000"))
    list.add(OrderModel("Frozen Minced Beef - ACE Foods Pizza Seafood Deluxe ", 3, "100,000", "300,000", "150,000"))
    list.add(OrderModel("Pizza Seafood Deluxe Pizza 4P’s", 3, "100,000", "300,000"))
//    list.add(OrderModel("Bí đỏ Nhật Hướng Dương - (200g)", 4, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Organic Garden Salad - F... (1 hộp)", 2, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Frozen Minced Beef - ACE Foods", 3, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Pizza Seafood Deluxe Pizza 4P’s", 3, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Bí đỏ Nhật Hướng Dương - (200g)", 4, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Organic Garden Salad - F... (1 hộp)", 2, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Club Soda - Evervess - (24 x 330 ml)", 1, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Schweppes Taste of Pink Grapefruit", 4, "100,000", "300,000", "150,000"))
//    list.add(OrderModel("Frozen Minced Beef - ACE Foods", 3, "100,000", "300,000", "150,000"))
    return list
}

private object StaticLayoutCache {

    private const val MAX_SIZE = 50 // Arbitrary max number of cached items
    private val cache = lruCache<String, StaticLayout>(MAX_SIZE)

    operator fun set(key: String, staticLayout: StaticLayout) {
        cache.put(key, staticLayout)
    }

    operator fun get(key: String): StaticLayout? {
        return cache[key]
    }
}