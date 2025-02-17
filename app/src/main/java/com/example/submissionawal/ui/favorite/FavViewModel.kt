package com.example.submissionawal.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawal.data.EventsRepository
import com.example.submissionawal.data.local.entity.EventsEntity
import com.example.submissionawal.data.local.entity.FavEntity
import kotlinx.coroutines.launch

class FavViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    fun getHeadlineEvents() = eventsRepository.getHeadlineEvents()

     fun getFavId(id:String): LiveData<List<EventsEntity>> {
            return eventsRepository.getFavId(id)
    }

    fun getFavoritedEvents() = eventsRepository.getFavoritedEvents()

    fun insertFavoriteEvent(events: FavEntity){
        viewModelScope.launch {
            eventsRepository.insertFavoritedEvent(events)
        }
    }
    fun deleteFavoritedEvent(title:String){
        viewModelScope.launch {
            eventsRepository.deleteFavoritedEvent(title)
        }
    }

//     fun saveEvents(events: EventsEntity) {
//         viewModelScope.launch {
//             eventsRepository.setFavoritedEvents(events, true)
//         }
//    }
//     fun deleteEvents(events: EventsEntity) {
//         viewModelScope.launch {
//             eventsRepository.setFavoritedEvents(events, false)
//         }
//    }
//
//    fun isFavorited(events: String): String {
//        return eventsRepository.isFavorited(events)
//    }
}