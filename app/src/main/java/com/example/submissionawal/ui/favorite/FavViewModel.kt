package com.example.submissionawal.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawal.data.EventsRepository
import com.example.submissionawal.data.local.entity.EventsEntity
import kotlinx.coroutines.launch

class FavViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    fun getHeadlineEvents() = eventsRepository.getHeadlineEvents()

     fun getFavId(id:String): LiveData<List<EventsEntity>> {
            return eventsRepository.getFavId(id)
    }


    fun getFavoritedEvents() = eventsRepository.getFavoritedEvents()

     fun saveEvents(events: EventsEntity) {
         viewModelScope.launch {
             eventsRepository.setFavoritedEvents(events, true)
         }
    }
     fun deleteEvents(events: EventsEntity) {
         viewModelScope.launch {
             eventsRepository.setFavoritedEvents(events, false)
         }
    }
}