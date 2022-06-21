package com.tiooooo.storyapp.ui.about

import android.content.Intent
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.storyapp.BuildConfig
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityAboutBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.utils.getCurrentVersion
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutActivity : BaseActivity<ActivityAboutBinding>() {
    override fun getViewBinding() = ActivityAboutBinding.inflate(layoutInflater)
    private val viewModel: AboutViewModel by viewModel()

    override fun initView() {
        isUsingToolbar(binding.toolbar, true)
        binding.tvAppVersion.text =
            getString(R.string.text_version_app, getCurrentVersion(this), BuildConfig.BUILD_TYPE)
    }

    override fun setSubscribeToLiveData() {
        viewModel.getUser().observe(this) {
            binding.tvUserName.text = it.name
        }
    }

    override fun initListener() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
    }
}