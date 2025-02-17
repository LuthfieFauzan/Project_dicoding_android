package com.example.submissionawal.ui.event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.submissionawal.MainActivity
import com.example.submissionawal.MainViewModel
import com.example.submissionawal.R
import com.example.submissionawal.data.local.entity.EventsEntity
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.databinding.ActivityEventDetailBinding
import com.example.submissionawal.ui.favorite.FavViewModel
import com.example.submissionawal.ui.favorite.FavViewModelFactory
import com.example.submissionawal.ui.setting.SettingPreferences
import com.example.submissionawal.ui.setting.SettingViewModelFactory
import com.example.submissionawal.ui.setting.dataStore
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates


class EventDetail : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val KEY_EVENT = "key_event"
    }

    private var dataEvent: ListEventsItem? = null
    private var notif: Boolean = false
    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var viewModel: EventViewModel
    private lateinit var favoriteViewModel: FavViewModel
    private var favorited by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        supportActionBar?.hide()
        getParcle()
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getappTheme()

        getfavorite()

        val register: Button= binding.registerButton
        register.setOnClickListener(this)

        val fav: ImageView =binding.ivFavorite
        fav.setOnClickListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getappTheme() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun getfavorite() {
        favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getFavId(dataEvent?.id.toString()).observe(this){event->
            favorited= event[0].isFavorited
            if (event[0].isFavorited){
                binding.ivFavorite.setImageDrawable(ContextCompat.getDrawable(binding.ivFavorite.context, R.drawable.baseline_favorite_24))
            }else{
                binding.ivFavorite.setImageDrawable(ContextCompat.getDrawable(binding.ivFavorite.context, R.drawable.baseline_favorite_border_24))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        val cover:ImageView = binding.mediacover
        val nama:TextView = binding.namaEvent
        val namaowner:TextView = binding.namaOwner
        val waktu:TextView = binding.waktuAcara

        val descripsi:TextView = binding.descripsiEvent

        viewModel.calculate(dataEvent?.quota.toString(), dataEvent?.registrants.toString())
        binding.sisaKuota.text = "sisa Kuota: "+viewModel.kuota.toString()

        Picasso.get()
            .load(dataEvent?.mediaCover)
            .placeholder(android.R.drawable.sym_def_app_icon)
            .error(android.R.drawable.sym_def_app_icon)
            .into(cover)
        supportActionBar?.title = dataEvent?.name

        nama.text= dataEvent?.name
        namaowner.text= dataEvent?.ownerName
        waktu.text= dataEvent?.beginTime.toString().take(16)
        descripsi.text = Html.fromHtml(dataEvent?.description)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavViewModel {
        val factory = FavViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavViewModel::class.java]
    }


    private fun getParcle() {
        dataEvent = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_EVENT, ListEventsItem::class.java)
        }else {
            intent.getParcelableExtra(KEY_EVENT)
        }
        notif = intent.getBooleanExtra("notif",false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(notif){
                    val intentDetail = Intent(application, MainActivity::class.java)
                    startActivity(intentDetail)
                }else{
                    finish()
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.register_button -> {
                val registerIntent = Intent(Intent.ACTION_VIEW, Uri.parse(dataEvent?.link ?: "") )
                startActivity(registerIntent)
            }
            R.id.iv_favorite -> {
                Log.e("testinga",favorited.toString())
                val eventsEntity = EventsEntity(
                dataEvent?.id.toString(),
                dataEvent?.name.toString(),
                dataEvent?.category.toString(),
                dataEvent?.summary.toString(),
                dataEvent?.imageLogo,
                dataEvent?.beginTime.toString(),
                dataEvent?.quota.toString(),
                dataEvent?.ownerName.toString(),
                dataEvent?.registrants.toString(),
                dataEvent?.mediaCover.toString(),
                dataEvent?.description.toString(),
                dataEvent?.link.toString(),
                    favorited
                )
                if (favorited){
                    favoriteViewModel.deleteEvents(eventsEntity)
                }else{
                    favoriteViewModel.saveEvents(eventsEntity)
                }
            }
        }
    }
}