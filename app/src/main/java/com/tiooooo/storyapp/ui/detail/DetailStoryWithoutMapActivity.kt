package com.tiooooo.storyapp.ui.detail

import coil.load
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.core.utils.extensions.toDate
import com.tiooooo.core.utils.extensions.toDateString
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityDetailStoryWithoutMapBinding

class DetailStoryWithoutMapActivity : BaseActivity<ActivityDetailStoryWithoutMapBinding>() {
    companion object {
        const val EXTRA_STORY = "EXTRA_STORY"
    }

    private var story: StoryViewParam? = null
    override fun initView() {
        story = intent.getParcelableExtra(EXTRA_STORY)
        with(binding) {
            isUsingToolbar(toolbar, true)
            story?.let {
                tvName.text = it.name
                tvDate.text = getString(R.string.added_at, it.createdAt.toDate().toDateString())
                tvContent.text = it.description

                ivStories.load(it.photoUrl) {
                    placeholder(com.tiooooo.core.R.drawable.ic_image_load)
                }
            }
        }
    }

    override fun getViewBinding() = ActivityDetailStoryWithoutMapBinding.inflate(layoutInflater)
}