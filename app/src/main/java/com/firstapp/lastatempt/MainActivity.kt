package com.firstapp.lastatempt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var inputText: EditText
    private lateinit var resultText: TextView
    private lateinit var summarizeButton: Button
    private lateinit var quizButton: Button
    private lateinit var openPdfUploadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.inputText)
        resultText = findViewById(R.id.resultText)
        summarizeButton = findViewById(R.id.summarizeButton)
        quizButton = findViewById(R.id.quizButton)
        openPdfUploadButton = findViewById(R.id.openPdfUploadButton)

        summarizeButton.setOnClickListener {
            summarizeText()
        }

        quizButton.setOnClickListener {
            generateQuiz()
        }

        openPdfUploadButton.setOnClickListener {
            val intent = Intent(this, PdfUploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun summarizeText() {
        val text = inputText.text.toString().trim()

        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter some text!", Toast.LENGTH_SHORT).show()
            return
        }

        resultText.text = "Summarizing..."  // Show loading

        RetrofitClient.instance.getSummary(TextRequest(text))
            .enqueue(object : Callback<SummarizeResponse> {
                override fun onResponse(
                    call: Call<SummarizeResponse>,
                    response: Response<SummarizeResponse>
                ) {
                    if (response.isSuccessful) {
                        resultText.text = response.body()?.summary ?: "No summary found"
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to summarize: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SummarizeResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun generateQuiz() {
        val text = inputText.text.toString().trim()

        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter some text!", Toast.LENGTH_SHORT).show()
            return
        }

        resultText.text = "Generating Quiz..."  // Show loading

        RetrofitClient.instance.generateQuiz(TextRequest(text))
            .enqueue(object : Callback<QuizResponse> {
                override fun onResponse(
                    call: Call<QuizResponse>,
                    response: Response<QuizResponse>
                ) {
                    if (response.isSuccessful) {
                        resultText.text = response.body()?.questions ?: "No questions generated"
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to generate quiz: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
