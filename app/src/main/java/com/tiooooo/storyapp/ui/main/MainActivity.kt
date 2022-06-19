package com.tiooooo.storyapp.ui.main

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.storyapp.databinding.ActivityMainBinding
import com.tiooooo.storyapp.ui.about.AboutActivity
import com.tiooooo.storyapp.ui.create.CreateStoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModel()


    override fun initView() {
        viewModel.getStories()
        setSupportActionBar(binding.toolbar)
    }

    override fun initListener() {
        with(binding) {
            btnCreateActivity.setOnClickListener {
                routeToCreateStory()
            }

            swipeStory.setOnRefreshListener {
                viewModel.getStories()
                binding.swipeStory.isRefreshing = false
            }

            cvProfile.setOnClickListener {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
            }
        }
    }

    override fun setSubscribeToLiveData() {
        with(viewModel) {
            listStories.observe(this@MainActivity) {
                binding.layoutErrorMain.root.isVisible = false
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

            listState.observe(this@MainActivity) {
                binding.shimmerCard.isVisible = it
                binding.layoutErrorMain.root.isVisible = false
            }

            listError.observe(this@MainActivity) {
                binding.rvStories.visibility = View.GONE
                binding.layoutErrorMain.root.isVisible = it.isNotEmpty()
                binding.layoutErrorMain.apply {
                    tvInfo.text = it
                    btnTryAgain.setOnClickListener {
                        initView()
                    }
                }
            }
        }
    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    private fun routeToCreateStory() {
        val intent = Intent(this, CreateStoryActivity::class.java)
        resultCreateStoryActivity.launch(intent)
    }

    private val resultCreateStoryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CreateStoryActivity.ADD_STORY_CODE) {
            val isSuccess =
                it.data?.getBooleanExtra(CreateStoryActivity.ADD_STORY_STATE, false) ?: false
            if (isSuccess) viewModel.getStories()
        }
    }

}