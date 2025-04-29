package com.firstapp.lastatempt

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/summarize")
    fun getSummary(@Body request: TextRequest): Call<SummarizeResponse>

    @Headers("Content-Type: application/json")
    @POST("/generate-quiz")
    fun generateQuiz(@Body request: TextRequest): Call<QuizResponse>
}
