package com.firstapp.lastatempt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream

class PdfUploadActivity : AppCompatActivity() {

    private lateinit var selectPdfButton: Button
    private lateinit var extractedTextView: TextView
    private lateinit var summarizeFromPdfButton: Button
    private lateinit var generateQuizFromPdfButton: Button

    private var extractedText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_upload)

        selectPdfButton = findViewById(R.id.selectPdfButton)
        extractedTextView = findViewById(R.id.extractedTextView)
        summarizeFromPdfButton = findViewById(R.id.summarizeFromPdfButton)
        generateQuizFromPdfButton = findViewById(R.id.generateQuizFromPdfButton)

        selectPdfButton.setOnClickListener {
            selectPdf()
        }

        summarizeFromPdfButton.setOnClickListener {
            summarizeExtractedText()
        }

        generateQuizFromPdfButton.setOnClickListener {
            generateQuizFromExtractedText()
        }
    }

    private fun selectPdf() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri? = data.data
            if (uri != null) {
                extractTextFromPdf(uri)
            }
        }
    }

    private fun extractTextFromPdf(uri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val document = PDDocument.load(inputStream)
            val pdfStripper = PDFTextStripper()
            extractedText = pdfStripper.getText(document)
            extractedTextView.text = extractedText
            document.close()
        } catch (e: Exception) {
            extractedTextView.text = "Failed to extract text: ${e.message}"
        }
    }

    private fun summarizeExtractedText() {
        if (extractedText.isNotEmpty()) {
            RetrofitClient.instance.getSummary(TextRequest(extractedText))
                .enqueue(object : retrofit2.Callback<SummarizeResponse> {
                    override fun onResponse(call: retrofit2.Call<SummarizeResponse>, response: retrofit2.Response<SummarizeResponse>) {
                        if (response.isSuccessful) {
                            extractedTextView.text = response.body()?.summary ?: "No summary found."
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<SummarizeResponse>, t: Throwable) {
                        extractedTextView.text = "Error: ${t.localizedMessage}"
                    }
                })
        }
    }

    private fun generateQuizFromExtractedText() {
        if (extractedText.isNotEmpty()) {
            RetrofitClient.instance.generateQuiz(TextRequest(extractedText))
                .enqueue(object : retrofit2.Callback<QuizResponse> {
                    override fun onResponse(call: retrofit2.Call<QuizResponse>, response: retrofit2.Response<QuizResponse>) {
                        if (response.isSuccessful) {
                            extractedTextView.text = response.body()?.questions ?: "No quiz generated."
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<QuizResponse>, t: Throwable) {
                        extractedTextView.text = "Error: ${t.localizedMessage}"
                    }
                })
        }
    }
}
