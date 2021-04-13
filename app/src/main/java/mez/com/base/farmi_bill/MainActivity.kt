package mez.com.base.farmi_bill

import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.icu.text.CaseMap
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        generatePdfFile()
    }

    private fun generatePdfFile() {
        val billPreview = findViewById<FarmiBillLayout>(R.id.billPreview)
        val pdfDoc = PdfDocument()
        val pdfBuilder = PdfDocument.PageInfo
                .Builder(136, 270, 1)
                .create()
        val page = pdfDoc.startPage(pdfBuilder)
//        billPreview.drawHeader(page.canvas)
        drawHeader(page.canvas)
        drawBody(page.canvas)
        pdfDoc.finishPage(page)
        writeToFile(pdfDoc)


    }

    private fun drawHeader(canvas: Canvas) {
        val companyNameText = "Farmi Service Trading Co., Ltd."
        val xPosition = canvas.width / 2f
        val companyNamePaint = Paint()
        val yPos = companyNamePaint.yPosition()

        companyNamePaint.textSize = 9f
        companyNamePaint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC)
        companyNamePaint.textAlign = Paint.Align.CENTER
        companyNamePaint.color = Color.BLACK
        canvas.drawText(companyNameText, xPosition, 10f, companyNamePaint)

        val phoneNumberTxt = "Số điện thoại: (028) 3622 2727"
        val phoneNumberPaint = Paint()
        val yPhonePos = phoneNumberPaint.yPosition()

        phoneNumberPaint.textSize = 7f
        phoneNumberPaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        phoneNumberPaint.textAlign = Paint.Align.CENTER
        phoneNumberPaint.color = Color.BLACK
        canvas.drawText(phoneNumberTxt, xPosition, 20f, phoneNumberPaint)

        val emailTxt = "Email: support@farmi.vn"
        val emailPaint = Paint()
        val yEmailPos = emailPaint.yPosition()

        emailPaint.textSize = 7f
        emailPaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        emailPaint.textAlign = Paint.Align.CENTER
        emailPaint.color = Color.BLACK
        canvas.drawText(emailTxt, xPosition, 30f, emailPaint)

        val billCodeTitle = "Hóa đơn số"
        val billTitlePaint = Paint()

        billTitlePaint.textSize = 9f
        billTitlePaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        billTitlePaint.textAlign = Paint.Align.CENTER
        billTitlePaint.color = Color.BLACK
        canvas.drawText(billCodeTitle, xPosition, 50f, billTitlePaint)

        val billCodeNumber = "#44555892"
        val billCodePaint = Paint()

        billCodePaint.textSize = 9f
        billCodePaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        billCodePaint.textAlign = Paint.Align.CENTER
        billCodePaint.color = Color.BLACK
        canvas.drawText(billCodeNumber, xPosition, 60f, billCodePaint)

        val nameEmployee = "Nhân viên: Nguyễn Văn Bé Ba"
        val nameEmpPaint = TextPaint()
        nameEmpPaint.textSize = 7f
        nameEmpPaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        nameEmpPaint.textAlign = Paint.Align.RIGHT
        nameEmpPaint.color = Color.BLACK
        canvas.drawMultilineText(nameEmployee, nameEmpPaint, 136, 135f, 80f)

        val dateTimeLbl = "Ngày bán: 11/03/2021  18:58"
        val dateTimePaint = TextPaint()
        dateTimePaint.textSize = 7f
        dateTimePaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        dateTimePaint.textAlign = Paint.Align.RIGHT
        dateTimePaint.color = Color.BLACK
        canvas.drawMultilineText(dateTimeLbl, dateTimePaint, 136, 135f, 90f)

        val lineOfHeadPaint = Paint()
        canvas.drawLine(0f, 105f, 136f, 105f, lineOfHeadPaint)

    }

    private fun drawBody(canvas: Canvas) {
        val xPosition = canvas.width / 2f
        val productLblPaint = TextPaint()
        val productTxt = "Sản phẩm"
        productLblPaint.textSize = 8f
        productLblPaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        productLblPaint.textAlign = Paint.Align.LEFT
        productLblPaint.color = Color.BLACK
        canvas.drawText(productTxt, 1f, 120f, productLblPaint)

        val qtyLblPaint = TextPaint()
        val qtyTxt = "SL"
        qtyLblPaint.textSize = 8f
        qtyLblPaint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        qtyLblPaint.textAlign = Paint.Align.LEFT
        qtyLblPaint.color = Color.BLACK
        canvas.drawText(qtyTxt, 1f, 130f, qtyLblPaint)

        val priceLblPaint = TextPaint()
        val priceTxt = "Đ.Giá"
        priceLblPaint.textSize = 8f
        priceLblPaint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        priceLblPaint.textAlign = Paint.Align.CENTER
        priceLblPaint.color = Color.BLACK
        canvas.drawText(priceTxt, xPosition, 130f, priceLblPaint)

        val toMoneyPaint = TextPaint()
        val toMoneyTxt = "T.Tiền"
        toMoneyPaint.textSize = 8f
        toMoneyPaint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        toMoneyPaint.textAlign = Paint.Align.RIGHT
        toMoneyPaint.color = Color.BLACK
        canvas.drawText(toMoneyTxt, 135f, 130f, toMoneyPaint)

        val productsLinePaint = Paint()
        canvas.drawLine(0f, 145f, 136f, 145f, productsLinePaint)

        val productNamePaint = TextPaint()
        val productNameTxt = "Organic Green Onion - Nico nico Yasai (100g)"
        productNamePaint.textSize = 7f
        productNamePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        productNamePaint.textAlign = Paint.Align.LEFT
        productNamePaint.color = Color.BLACK
        val productNameHeight = canvas.drawMultilineText(productNameTxt, productNamePaint, 135, xPosition, 150f)

        val qtyValuePaint = TextPaint()
        val qtyValueTxt = "100"
        qtyValuePaint.textSize = 7f
        qtyValuePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        qtyValuePaint.textAlign = Paint.Align.LEFT
        qtyValuePaint.color = Color.BLACK
//        canvas.drawMultilineText(qtyValueTxt, qtyValuePaint, 5, 1f, 160f + productNameHeight)
        canvas.drawText(qtyValueTxt, 1f, 162f + productNameHeight, qtyValuePaint)

        val priceValuePaint = TextPaint()
        val priceValueTxt = "10,000,000"
        priceValuePaint.textSize = 7f
        priceValuePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        priceValuePaint.textAlign = Paint.Align.CENTER
        priceValuePaint.color = Color.BLACK
//        canvas.drawMultilineText(priceValueTxt, qtyValuePaint, 62, xPosition, 160f + productNameHeight)
        canvas.drawText(priceValueTxt, xPosition, 162f + productNameHeight, priceValuePaint)

        val toMoneyValuePaint = TextPaint()
        val toMoneyValueTxt = "10,000,000"
        toMoneyValuePaint.textSize = 7f
        toMoneyValuePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        toMoneyValuePaint.textAlign = Paint.Align.RIGHT
        toMoneyValuePaint.color = Color.BLACK
        canvas.drawText(toMoneyValueTxt, 135f, 162f + productNameHeight, toMoneyValuePaint)
//        val heightTotalText = canvas.height
//        val toMoneyHeight = canvas.drawMultilineText(toMoneyValueTxt, toMoneyValuePaint, 62, 135f, 160f + productNameHeight)

        val discountPaint = TextPaint()
        val discountTxt = "Giảm giá: 150,000,000"
        discountPaint.textSize = 7f
        discountPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        discountPaint.textAlign = Paint.Align.RIGHT
        discountPaint.color = Color.BLACK
        canvas.drawMultilineText(discountTxt, discountPaint, 135, 135f, 162f + productNameHeight + 2f)
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
        const val FONT_SIZE_9F = 9f
        const val FONT_SIZE_7F = 7f
        const val FONT_SIZE_8F = 8f
        private val TAG = MainActivity::class.java.simpleName
    }
}