package com.tiooooo.storyapp.ui.detail

import coil.load
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.core.utils.extensions.toDate
import com.tiooooo.core.utils.extensions.toDateString
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : BaseActivity<ActivityDetailStoryBinding>() {

    override fun initView() {
        val story = intent.getParcelableExtra<StoryViewParam>(EXTRA_STORY)
        with(binding) {
            isUsingToolbar(toolbar, true)
            story?.let {
                tvName.text = it.name
                tvDate.text = getString(R.string.added_at, it.createdAt.toDate().toDateString())
                tvContent.text = it.description

                ivStories.load(it.photoUrl) {
                    placeholder(R.drawable.ic_launcher_background)
                }
            }
        }
    }


    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun getViewBinding() = ActivityDetailStoryBinding.inflate(layoutInflater)
}