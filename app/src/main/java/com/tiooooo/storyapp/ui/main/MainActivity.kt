package com.tiooooo.storyapp.ui.main

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityMainBinding
import com.tiooooo.storyapp.ui.about.AboutActivity
import com.tiooooo.storyapp.ui.create.CreateStoryActivity
import com.tiooooo.storyapp.ui.map.MapActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var pagingAdapter: StoryAdapter


    override fun initView() {
        viewModel.getStories()
        setSupportActionBar(binding.toolbar)

        pagingAdapter = StoryAdapter()
        binding.rvStories.apply {
            this.adapter = pagingAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
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

            cvMap.setOnClickListener {
                startActivity(Intent(this@MainActivity, MapActivity::class.java))
            }
        }
    }

    override fun setSubscribeToLiveData() {
            viewModel.listStories.observe(this@MainActivity) {
                lifecycleScope.launch {
                    pagingAdapter.submitData(it)
                }
            }

            lifecycleScope.launch {
                pagingAdapter.loadStateFlow.collectLatest {
                    val isEmpty =
                        it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && pagingAdapter.itemCount < 1
                    with(binding) {
                        shimmerCard.isVisible = it.refresh is LoadState.Loading
                        pbLoadMore.isVisible = it.append is LoadState.Loading
                        layoutErrorMain.root.isVisible = it.refresh is LoadState.Error
                        rvStories.isVisible = it.refresh is LoadState.NotLoading && !isEmpty

                        layoutErrorMain.apply {
                            tvInfo.text = getString(R.string.cannot_fetch_data)
                            btnTryAgain.setOnClickListener {
                                viewModel.getStories()
                            }
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