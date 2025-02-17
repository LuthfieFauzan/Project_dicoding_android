package com.example.submissionawal.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.submissionawal.data.local.entity.EventsEntity
import com.example.submissionawal.data.local.room.EventsDao
import com.example.submissionawal.data.remote.retrofit.ApiService



class EventsRepository private constructor(
    private val apiService: ApiService,
    private val eventsDao: EventsDao,
) {
//    private val result = MediatorLiveData<Result<List<EventsEntity>>>()
    fun getFavoritedEvents(): LiveData<List<EventsEntity>> {
        return eventsDao.getFavoritedEvents()
    }
     fun getFavId(id:String): LiveData<List<EventsEntity>> {
        return eventsDao.getFavId(id)
    }
    suspend fun setFavoritedEvents(events: EventsEntity, bookmarkState: Boolean) {
        events.isFavorited = bookmarkState
        eventsDao.updateEvents(events)
    }
    fun getHeadlineEvents(): LiveData<Result<List<EventsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllEvent()
            val events = response.listEvents
            val eventsList = events!!.map { event ->
                val isFavorited = eventsDao.isEventsFavorited(event?.name.toString())
                EventsEntity(
                                event?.id.toString(),
                                event?.name.toString(),
                                event?.category.toString(),
                                event?.summary.toString(),
                                event?.imageLogo,
                                event?.beginTime.toString(),
                                event?.quota.toString(),
                                event?.ownerName.toString(),
                                event?.registrants.toString(),
                                event?.mediaCover.toString(),
                                event?.description.toString(),
                                event?.link.toString(),
                                isFavorited
                            )
            }
            eventsDao.deleteAll()
                eventsDao.insertEvents(eventsList)
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventsEntity>>> = eventsDao.getEvents().map { Result.Success(it) }
        emitSource(localData)

    }

    companion object {
        @Volatile
        private var instance: EventsRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventsDao: EventsDao,
        ): EventsRepository =
            instance ?: synchronized(this) {
                instance ?: EventsRepository(apiService, eventsDao)
            }.also { instance = it }
    }
}