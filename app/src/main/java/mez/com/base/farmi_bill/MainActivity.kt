package mez.com.base.farmi_bill

import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.icu.text.CaseMap
import android.os.Bundle
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import android.text.Layout
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


class MainActivity : AppCompatActivity() {

    private val textPaint by lazy { TextPaint() }
    private val linePaint by lazy { Paint() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        generatePdfFile()
    }

    private fun generatePdfFile() {
        val billPreview = findViewById<FarmiBillLayout>(R.id.billPreview)
        val pdfDoc = PdfDocument()
//        val attr = PrintAttributes.Builder()
//            .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT)
//            .build()
//        val pdfDocument = PrintedPdfDocument(this, attr)
        val lineNumber = getProductsInfo().size
        val height = WIDTH_PAGE + 40 * lineNumber + 110 - 10 * lineNumber
        val pdfBuilder = PdfDocument.PageInfo
                .Builder(WIDTH_PAGE, height, 1)
                .create()
        pdfDoc.startPage(pdfBuilder)?.also { page ->
            drawHeader(page.canvas)
            drawCategoryLabel(page.canvas)
            drawProductsInfo(page.canvas)
            pdfDoc.finishPage(page)
        }

        writeToFile(pdfDoc)
    }

    private fun drawHeader(canvas: Canvas) {

        val companyNameText = resources.getString(R.string.company_name_header_bill)
        textPaint.paintAlignCenter(FONT_SIZE_9F, Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC)
        canvas.drawText(companyNameText, ALIGN_CENTER_X_AXIS, MARGIN_TOP_HEADER, textPaint)

        val phone = "(028) 3622 2727"
        val phoneNumber = resources.getString(R.string.phone_number_header_bill, phone)
        canvas.drawPhoneOrEmail(textPaint, phoneNumber, 2 * MARGIN_TOP_HEADER)

        val email = "support@farmi.vn"
        val emailLabel = resources.getString(R.string.email_header_bill, email)
        canvas.drawPhoneOrEmail(textPaint, emailLabel, 3 * MARGIN_TOP_HEADER)

        val invoiceNumLbl = resources.getString(R.string.bill_code_label_header_bill)
        canvas.drawInvoiceInfo(textPaint, invoiceNumLbl, 5 * MARGIN_TOP_HEADER)

        val invoiceCode = "#44555892"
        canvas.drawInvoiceInfo(textPaint, invoiceCode, 6 * MARGIN_TOP_HEADER)

        val employee = "Nguyễn Văn Bé Ba"
        val empName = resources.getString(R.string.employee_name_header_bill, employee)
        canvas.drawCreatorInfo(textPaint, empName, 7 * MARGIN_TOP_HEADER)

        val time = "11/03/2021 18:58"
        val saleDate = resources.getString(R.string.date_time_create_bill, time)
        canvas.drawCreatorInfo(textPaint, saleDate, 8 * MARGIN_TOP_HEADER)

        canvas.drawLineItem(linePaint, 9 * MARGIN_TOP_HEADER + 5f)
    }

    private fun drawCategoryLabel(canvas: Canvas) {
        val productLbl = resources.getString(R.string.product_name_label_bill)
        textPaint.paintAlignLeft(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
        canvas.drawText(productLbl, ALIGN_LEFT_X_AXIS, 11 * MARGIN_TOP_HEADER, textPaint)

        val quantityLbl = resources.getString(R.string.quantity_product_label_bill)
        textPaint.paintAlignLeft(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
        canvas.drawText(quantityLbl, ALIGN_LEFT_X_AXIS, 12 * MARGIN_TOP_HEADER, textPaint)

        val priceLbl = resources.getString(R.string.price_product_label_bill)
        textPaint.paintAlignCenter(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
        canvas.drawText(priceLbl, ALIGN_CENTER_X_AXIS, 12 * MARGIN_TOP_HEADER, textPaint)

        val toMoneyLbl = resources.getString(R.string.to_money_label_bill)
        textPaint.paintAlignRight(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
        canvas.drawText(toMoneyLbl, ALIGN_RIGHT_X_AXIS, 12 * MARGIN_TOP_HEADER, textPaint)

        canvas.drawLineItem(linePaint, 13 * MARGIN_TOP_HEADER)
    }

    private fun drawProductsInfo(canvas: Canvas) {
        var marginFromElement = MARGIN_TOP_PRODUCT_LIST
        val products = getProductsInfo()

        for (i in products.indices) {
            val prodName = products[i].name
            val heightProductName = canvas.drawProductName(textPaint, prodName, marginFromElement)

            marginFromElement += heightProductName
            val quantity = products[i].quantity.toString()
            canvas.drawProductQuantity(textPaint, quantity, marginFromElement)

            val price = products[i].originPrice
            canvas.drawOriginPrice(textPaint, price, marginFromElement)

            val toMoney = products[i].toMoneyPrice
            val heightToMoney = canvas.drawAmountPrice(textPaint, toMoney, marginFromElement)
            marginFromElement += heightToMoney

           if (products[i].discountPrice != null) {
                val discount = products[i].discountPrice
                val discountLbl = resources.getString(R.string.discount_label_bill, discount)
                val heightLblDiscount = canvas.drawDiscountPrice(textPaint, discountLbl, marginFromElement)

               marginFromElement += heightLblDiscount

               Log.d(TAG, "heightFirstProductItem: $marginFromElement")
            }
        }

        drawTotalFooter(canvas, marginFromElement)
    }

    private fun drawTotalFooter(canvas: Canvas, marginTop: Float) {
        var marginTopTotal = marginTop + MARGIN_TOP_FOOTER
        canvas.drawLine(0f, marginTopTotal, 136f, marginTopTotal, linePaint)

        marginTopTotal += MARGIN_TOP_ELEMENT_FOOTER
        val totalLbl = resources.getString(R.string.total_money_label_bill)
        canvas.drawTotalFtLbl(textPaint, totalLbl, marginTopTotal)

        val totalValue = "1,885,000,000"
        canvas.drawTotalFtValue(textPaint, totalValue, marginTopTotal)

        marginTopTotal += MARGIN_TOP_ELEMENT_FOOTER
        val promotionLbl = resources.getString(R.string.promotion_price_label_bill)
        canvas.drawTotalFtLbl(textPaint, promotionLbl, marginTopTotal)

        val promotionValue = "-1,885,000,000"
        canvas.drawTotalFtValue(textPaint, promotionValue, marginTopTotal)

        drawPaymentFooter(canvas, marginTopTotal)
    }

    private fun drawPaymentFooter(canvas: Canvas, marginTop: Float) {
        var marginTopPayment = marginTop + MARGIN_TOP_FOOTER
        canvas.drawLine(0f, marginTopPayment, 136f, marginTopPayment, linePaint)

        val paymentCashLbl = resources.getString(R.string.payment_must_label_bill)
        marginTopPayment += MARGIN_TOP_ELEMENT_FOOTER
        textPaint.paintAlignLeft(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
        canvas.drawText(paymentCashLbl, ALIGN_LEFT_X_AXIS, marginTopPayment, textPaint)

        val paymentCashValue = "1,735,000,000"
        textPaint.paintAlignRight(FONT_SIZE_8F, Typeface.DEFAULT_BOLD, Typeface.BOLD)
        canvas.drawText(paymentCashValue, ALIGN_RIGHT_X_AXIS, marginTopPayment, textPaint)

        val paymentCod = resources.getString(R.string.payment_by_cash_value_bill)
        marginTopPayment += MARGIN_TOP_ELEMENT_FOOTER
        canvas.drawPaymentTypeLbl(textPaint, paymentCod, marginTopPayment)

        val codValue = "1,234,567,000"
        textPaint.paintAlignRight(FONT_SIZE_8F, Typeface.SANS_SERIF, Typeface.NORMAL)
        canvas.drawText(codValue, ALIGN_RIGHT_X_AXIS, marginTopPayment, textPaint)

        val paymentMethodValue = "Zalo Pay"
        marginTopPayment += MARGIN_TOP_ELEMENT_FOOTER
        canvas.drawPaymentTypeLbl(textPaint, paymentMethodValue, marginTopPayment)

        val paymentExcessValue = "2,000,000,000"
        canvas.drawPaymentTypeValue(textPaint, paymentExcessValue, marginTopPayment)
    }

    private fun writeToFile(pdfDocument: PdfDocument) {
        val billCode = System.currentTimeMillis()

        val dir = File(this.filesDir, "FarmiBill")
        if (!dir.exists()) {
            dir.mkdir()
        }
        try {
            val billFile = File(dir, "bill_layout_$billCode.pdf")
            val fileIsExist = billFile.exists()
//            if (fileIsExist) {
//                billFile.delete()
//            }
            val fos = FileOutputStream(billFile)
            pdfDocument.writeTo(fos)
            pdfDocument.close()
        } catch (e: Exception) {
            Log.e(TAG, "writeToFileError: ${e.message}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty()) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] === PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] === PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}