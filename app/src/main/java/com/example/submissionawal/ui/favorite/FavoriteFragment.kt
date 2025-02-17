package com.example.submissionawal.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.databinding.FragmentFavoriteBinding

import com.example.submissionawal.ui.event.EventAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var rvFav: RecyclerView
    private lateinit var progressBar: ProgressBar



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        progressBar= binding.progressBar
        rvFav = binding.rvfavorite
        val layoutManagerFinished = LinearLayoutManager(context)
        rvFav.layoutManager =layoutManagerFinished

        val factory: FavViewModelFactory = FavViewModelFactory.getInstance(requireActivity())
        val viewModel: FavViewModel by viewModels {
            factory
        }
        val adapter = EventAdapter()
        viewModel.getFavoritedEvents().observe(viewLifecycleOwner) { users ->

            val items = arrayListOf<ListEventsItem>()
            users.map {
                val item = ListEventsItem(id = Integer.parseInt(it.id),
                    name = it.title,
                    imageLogo = it.imageLogo,
                    summary = it.summary,
                    quota = Integer.parseInt(it.quota),
                    registrants = Integer.parseInt(it.registrants),
                    beginTime = it.beginTime,
                    category = it.category,
                    description = it.description,
                    mediaCover = it.mediaCover,
                    ownerName = it.ownerName,
                    link = it.link)
                items.add(item)
            }
            adapter.submitList(items)
            binding.progressBar.visibility = View.GONE
            Log.e("hasil", users.toString())
            rvFav.adapter = adapter
            if (adapter.itemCount==0){
                binding.tvEmptyFavorite.visibility = View.VISIBLE
            }else{
                binding.tvEmptyFavorite.visibility = View.GONE
            }
        }

        return root
}
}
