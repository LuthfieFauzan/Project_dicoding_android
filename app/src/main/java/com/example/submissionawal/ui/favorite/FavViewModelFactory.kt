package com.example.submissionawal.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionawal.data.EventsRepository
import com.example.submissionawal.di.Injection

class FavViewModelFactory private constructor(private val eventsRepository: EventsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            return FavViewModel(eventsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavViewModelFactory? = null
        fun getInstance(context: Context): FavViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}