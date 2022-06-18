package com.tiooooo.storyapp

import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.storyapp.databinding.ActivityMainBinding
import com.tiooooo.storyapp.ui.add.AddStoryActivity
import com.tiooooo.storyapp.ui.main.MainViewModel
import com.tiooooo.storyapp.ui.main.StoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModel()


    override fun initView() {
        viewModel.getListStories()
        setSupportActionBar(binding.toolbar)
    }

    override fun initListener() {
        with(binding) {
            btnCreateActivity.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }

            swipeStory.setOnRefreshListener {
                viewModel.getListStories()
                binding.swipeStory.isRefreshing = false
            }
        }
    }

    override fun setSubscribeToLiveData() {
        with(viewModel) {
            listStories.observe(this@MainActivity) {
                binding.rvStories.apply {
                    isVisible = it.isNotEmpty()
                    adapter = StoryAdapter(it)
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    val layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                        context,
                        com.tiooooo.core.R.anim.layout_animation_fall_down
                    )
                    this.layoutAnimation = layoutAnimationController
                }
            }

            listState.observe(this@MainActivity){
                binding.shimmerCard.isVisible = it
            }
        }
    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

}