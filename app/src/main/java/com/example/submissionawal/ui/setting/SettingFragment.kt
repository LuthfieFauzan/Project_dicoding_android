package com.example.submissionawal.ui.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.submissionawal.MainViewModel
import com.example.submissionawal.databinding.FragmentSettngBinding
import com.example.submissionawal.utils.MyWorker
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private var _binding: FragmentSettngBinding? = null
    private val Context.dataStore by preferencesDataStore("app_preferences")
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private lateinit var workManager: WorkManager

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettngBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val switchTheme = binding.darkmode
        val switchDaily = binding.dailyRemider
        workManager = WorkManager.getInstance(requireContext())
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
        mainViewModel.getNotifSettings().observe(viewLifecycleOwner){isNotifActive: Boolean ->
            if (isNotifActive) {
                dailyreminder(isNotifActive)
                switchDaily.isChecked = true
            } else {
                dailyreminder(isNotifActive)
                switchDaily.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
        switchDaily.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveNotifSetting(isChecked)
        }
        return root
    }

    private fun dailyreminder(checked: Boolean) {
        if (checked){
            val data = Data.Builder()
                .build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniquePeriodicWork("Daily Reminder",
                ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)
        }else{
            try {
                workManager.cancelUniqueWork("Daily Reminder")
            }catch (e:Exception){
                Log.e("workmanager", e.toString())
            }
        }

    }
}