package com.example.submissionawal.di

import android.content.Context
import com.example.submissionawal.data.EventsRepository
import com.example.submissionawal.data.local.room.EventsDatabase
import com.example.submissionawal.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventsRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventsDatabase.getInstance(context)
        val dao = database.eventsDao()
        return EventsRepository.getInstance(apiService, dao)
    }
}
