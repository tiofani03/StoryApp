package com.tiooooo.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.extensions.toDate
import com.tiooooo.core.utils.extensions.toDateString
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ItemStoryBinding
import com.tiooooo.storyapp.ui.detail.DetailStoryActivity

class StoryAdapter :
    PagingDataAdapter<StoryViewParam, StoryAdapter.StoryViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<StoryViewParam>() {
        override fun areItemsTheSame(oldItem: StoryViewParam, newItem: StoryViewParam): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryViewParam, newItem: StoryViewParam): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let { bindItem(it) }
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(storyViewParam: StoryViewParam) {

            with(binding) {
                tvName.text = storyViewParam.name
                tvDate.text =
                    itemView.context.getString(
                        R.string.added_at,
                        storyViewParam.createdAt.toDate().toDateString()
                    )

                ivStories.load(storyViewParam.photoUrl) {
                    placeholder(R.drawable.ic_launcher_background)
                }


                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivStories, "ivStories"),
                            Pair(tvName, "tvName"),
                            Pair(tvDate, "tvDate"),
                            Pair(tvContent, "tvContent")
                        )
                    (itemView.context as Activity).startActivity(
                        Intent(itemView.context, DetailStoryActivity::class.java).putExtra(
                            DetailStoryActivity.EXTRA_STORY,
                            storyViewParam
                        ), optionsCompat.toBundle()
                    )
                }
            }
        }

    }
}