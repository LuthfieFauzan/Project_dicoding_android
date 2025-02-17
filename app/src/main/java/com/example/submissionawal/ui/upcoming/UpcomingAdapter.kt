package com.example.submissionawal.ui.upcoming

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionawal.data.remote.response.ListEventsItem
import com.example.submissionawal.databinding.ItemEventUpcomingBinding
import com.example.submissionawal.ui.event.EventDetail
import com.squareup.picasso.Picasso


class UpcomingAdapter : ListAdapter<ListEventsItem, UpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {
     var test:Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    class MyViewHolder(private val binding: ItemEventUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListEventsItem){
            binding.tvItemName.text = review.name
            Picasso.get()
                .load(review.imageLogo)
                .into(binding.imgItemPhoto)
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, EventDetail::class.java).apply {
                    putExtra("key_event", (review) as Parcelable)
                }
                itemView.context.startActivity(intentDetail)
            }
        }
    }


     override fun getItemCount(): Int {
        if (test==5){
            if(super.getItemCount()<5){
                return super.getItemCount()
            }
            return test
        }else {
            return super.getItemCount()
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}