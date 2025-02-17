package com.example.submissionawal.ui.home

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.CONNECTIVITY_SERVICE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.databinding.FragmentHomeBinding
import com.example.submissionawal.ui.event.EventAdapter
import com.example.submissionawal.ui.event.EventViewModel
import com.example.submissionawal.ui.favorite.FavViewModel
import com.example.submissionawal.ui.favorite.FavViewModelFactory
import com.example.submissionawal.ui.upcoming.UpcomingAdapter


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var rvFinished: RecyclerView
    private lateinit var rvUpcoming: RecyclerView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val connMgr = activity
            ?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connMgr.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            isconnected()
        } else {
            notconnected()
        }
        setdatabase()
        return root

    }

    private fun setdatabase() {
        val factory: FavViewModelFactory = FavViewModelFactory.getInstance(requireActivity())
        val viewModel: FavViewModel by viewModels {
            factory
        }
        viewModel.getHeadlineEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is com.example.submissionawal.data.Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is com.example.submissionawal.data.Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is com.example.submissionawal.data.Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun notconnected() {
        val builder = context?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle("Failed to load data")
            builder.setIcon(R.drawable.ic_launcher_foreground)
            builder.setMessage("please connect to internet to view the event")
            val alert = builder.create()
            alert.show()
        }
    }

    private fun isconnected() {
        val eventViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EventViewModel::class.java]
        eventViewModel.listEventFinish.observe(viewLifecycleOwner) { listevent ->
            setEventData(listevent, "0")
        }

        eventViewModel.listEventUpcoming.observe(viewLifecycleOwner) { listevent ->
            setEventData(listevent, "1")
        }
        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        rvUpcoming= binding.rvUpcomingEvent
        rvFinished= binding.rvFinishedEvent
        progressBar= binding.progressBar


        val layoutManagerFinished = LinearLayoutManager(context)
        rvFinished.layoutManager =layoutManagerFinished

        val layoutManagerUpcoming = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvUpcoming.layoutManager = layoutManagerUpcoming
    }


    private fun setEventData(listEvents: List<ListEventsItem>, aktif: String) {

        if (aktif=="0"){
            val adapter = EventAdapter()
            adapter.submitList(listEvents)
            adapter.test=5
            rvFinished.adapter = adapter

        }else if (aktif=="1"){
            val adapter = UpcomingAdapter()
            adapter.submitList(listEvents)
            adapter.test=5
            if (adapter.itemCount==0){
                binding.tvEmptyUpcoming.visibility = View.VISIBLE
            }else{
                binding.tvEmptyUpcoming.visibility = View.GONE
            }
            rvUpcoming.adapter = adapter
        }
    }


    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}