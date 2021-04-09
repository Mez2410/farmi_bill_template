package mez.com.base.farmi_bill

import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generatePdfFile()
    }

    private fun generatePdfFile() {
        val billPreview = findViewById<FarmiBillLayout>(R.id.billPreview)
        val pdfDoc = PdfDocument()
        val pdfBuilder = PdfDocument.PageInfo
                .Builder(135, 270, 1)
                .create()
        val page = pdfDoc.startPage(pdfBuilder)
        billPreview.drawHeader(page.canvas)
        writeToFile(pdfDoc)
    }

    private fun writeToFile(pdfDocument: PdfDocument) {
        val billCode = System.currentTimeMillis()

        val dir = File(this.filesDir, "FarmiBill")
        if (!dir.exists()) {
            dir.mkdir()
        }
        try {
            val billFile = File(dir, "bill_$billCode")
            val fos = FileOutputStream(billFile)
            pdfDocument.writeTo(fos)
            pdfDocument.close()
        } catch (e: Exception) {
            Log.e(TAG, "writeToFileError: ${e.message}")
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}