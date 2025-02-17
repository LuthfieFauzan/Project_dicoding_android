package com.example.submissionawal.data.remote.retrofit
import com.example.submissionawal.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvent(@Query("active") active: String,@Query("q") search: String,
    ): Call<EventResponse>

    @GET("events")
    fun dailyReminder(@Query("active") active: String,@Query("limit") limit: String,
    ): Call<EventResponse>

    @GET("events")
    suspend fun getAllEvent(): EventResponse
}