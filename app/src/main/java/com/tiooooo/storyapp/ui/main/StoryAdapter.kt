package com.tiooooo.storyapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiooooo.core.model.StoriesViewParam
import com.tiooooo.core.utils.extensions.toDate
import com.tiooooo.core.utils.extensions.toDateString
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ItemStoryBinding
import com.tiooooo.storyapp.ui.detail.DetailStoryActivity

class StoryAdapter(private val listStory: ArrayList<StoriesViewParam>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

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
        holder.bindItem(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")

//        private var ivStories: ImageView = itemView.findViewById(R.id.ivStories)
//        private var tvName: TextView = itemView.findViewById(R.id.tvName)
//        private var tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bindItem(storiesViewParam: StoriesViewParam) {

            with(binding) {
                tvName.text = storiesViewParam.name
                tvDate.text =
                    "Ditambahkan pada " + storiesViewParam.createdAt.toDate().toDateString()

                ivStories.load(storiesViewParam.photoUrl) {
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
                            storiesViewParam
                        ), optionsCompat.toBundle()
                    )

//                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
//                intent.putExtra(DetailStoryActivity.EXTRA_STORY, storiesViewParam)
//
//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(ivStories, "ivStories"),
//                        Pair(tvName, "tvName"),
//                        Pair(tvDate, "tvDate"),
//                    )
//                itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }
}