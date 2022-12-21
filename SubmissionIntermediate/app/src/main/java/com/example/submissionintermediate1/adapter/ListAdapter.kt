package com.example.submissionintermediate1.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionintermediate1.data.database.StoryResponseItem
import com.example.submissionintermediate1.data.response.Stories
import com.example.submissionintermediate1.databinding.ItemListBinding
import com.example.submissionintermediate1.ui.details.DetailsActivity
import com.example.submissionintermediate1.ui.details.DetailsActivity.Companion.EXTRA_STORY
import com.example.submissionintermediate1.utils.DateFormat.setLocalTime
import com.example.submissionintermediate1.utils.DiffUtils

class ListAdapter: PagingDataAdapter<StoryResponseItem, ListAdapter.ListViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(holder.itemView.context, data)
        }
    }

    class ListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, listStories: StoryResponseItem) {
            with(binding) {
                tvUsername.text = listStories.name
                tvDate.text = setLocalTime(listStories.createdAt.toString())
                tvDescription.text = listStories.description
                Glide.with(itemView)
                    .load(listStories.photoUrl)
                    .into(imageStories)
                root.setOnClickListener {
                    val intent = Intent(itemView.context, DetailsActivity::class.java)
                    intent.putExtra(EXTRA_STORY, listStories)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(binding.tvUsername, "username"),
                            Pair(binding.tvDescription, "desc"),
                            Pair(binding.tvDate, "date"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseItem>() {
            override fun areItemsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponseItem, newItem: StoryResponseItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}