package com.example.submissionawal.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.response.EventResponse
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.data.remote.retrofit.ApiConfig
import com.example.submissionawal.ui.event.EventDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "EventDaily"
        const val CHANNEL_NAME = "EventDaily channel"
        lateinit var event: ListEventsItem
    }
    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getUpcomingEvent()
    }

    private fun getUpcomingEvent(): Result {
        val client = ApiConfig.getApiService().dailyReminder("-1","1")
        client.enqueue(object : Callback<EventResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        event= responseBody.listEvents!![0]!!
                        val title = "Daily Reminder Event ${response.body()!!.listEvents?.get(0)?.beginTime}"
                        val message = "${response.body()!!.listEvents?.get(0)?.name}, ${response.body()!!.listEvents?.get(0)?.beginTime} "
                        showNotification(title, message)
                        resultStatus = Result.success()
                    }
                } else {
                    Log.e("notif", "onFailure: ${response.message()}")
                    showNotification("Get Event Failed", response.message())
                    resultStatus = Result.failure()
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("notif", "onFailure: ${t.message}")
                showNotification("Get Event Failed", t.message)
                resultStatus = Result.failure()
            }
        })
        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notif =true
        val intentDetail = Intent(applicationContext, EventDetail::class.java).apply {
            putExtra("key_event", (event) as Parcelable)
            putExtra("notif", notif )
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intentDetail,
            PendingIntent.FLAG_IMMUTABLE)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}
