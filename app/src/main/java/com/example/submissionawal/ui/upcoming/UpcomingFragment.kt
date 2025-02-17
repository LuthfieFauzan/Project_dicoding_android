package com.example.submissionawal.ui.upcoming

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.CONNECTIVITY_SERVICE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.databinding.FragmentUpcomingBinding
import com.example.submissionawal.ui.event.EventAdapter
import com.example.submissionawal.ui.event.EventViewModel
import com.google.android.material.textfield.TextInputEditText


class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var rvUpcoming: RecyclerView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val connMgr = activity
            ?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connMgr.activeNetworkInfo

        if (networkInfo == null || !networkInfo.isConnected) {
            // display error
            notconnected()
        }


        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val eventViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EventViewModel::class.java]
        eventViewModel.listEventUpcoming.observe(viewLifecycleOwner) { listevent ->
            setEventData(listevent)
        }
        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        progressBar= binding.progressBar
        rvUpcoming = binding.rvUpcomngEvent
        val layoutManagerFinished = LinearLayoutManager(context)
        rvUpcoming.layoutManager =layoutManagerFinished

        val search: TextInputEditText = binding.search
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            eventViewModel.findEvent(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        return root
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

    @SuppressLint("SetTextI18n")
    private fun setEventData(listEvents: List<ListEventsItem>) {
        val tvempty = binding.tvEmpty
        val adapter = EventAdapter()
        adapter.submitList(listEvents)
            rvUpcoming.adapter = adapter
        if (adapter.itemCount==0 && binding.search.text!!.isNotEmpty() ){
            tvempty.visibility = View.VISIBLE
            tvempty.text = "there is no event that containt ${binding.search.text!!}"
        }else if(adapter.itemCount==0){
            tvempty.visibility = View.VISIBLE
            tvempty.text = "There is no upcoming event yet"
        }
        else{
            tvempty.visibility = View.GONE
        }
    }
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        if (isLoading) {
//            progressBar.visibility = View.VISIBLE
//        } else {
//            progressBar.visibility = View.GONE
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}