package com.example.submissionawal.ui.event

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionawal.data.remote.response.EventResponse
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel : ViewModel() {
    var kuota = 0
    fun calculate(quota: String, registrants: String) {
        kuota = quota.toInt() - registrants.toInt()
    }

    private val _event = MutableLiveData<EventResponse>()
    val event: LiveData<EventResponse> = _event

    private val _listEventFinish = MutableLiveData<List<ListEventsItem>>()
    val listEventFinish: LiveData<List<ListEventsItem>> = _listEventFinish

    private val _listEventUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listEventUpcoming: LiveData<List<ListEventsItem>> = _listEventUpcoming

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object {
        private const val TAG = "Home"
        private const val AKTIF = "1"
        private const val FINISH = "0"
    }
    init {
        findEvent("")
    }
     fun findEvent(search:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(FINISH,search)
        showdata(client, FINISH)
        val client2 = ApiConfig.getApiService().getEvent(AKTIF,search)
        showdata(client2,AKTIF)
    }


    private fun showdata(client: Call<EventResponse>, aktif: String) {
        client.enqueue(object : Callback<EventResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isLoading.value = false
                        if (aktif=="0"){
                            _listEventFinish.value = response.body()?.listEvents as List<ListEventsItem>?
                        }else if (aktif=="1"){
                            _listEventUpcoming.value = response.body()?.listEvents as List<ListEventsItem>?
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = true
            }
        })
    }
}