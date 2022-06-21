package com.tiooooo.core.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tiooooo.core.ui.custom.LoadingDialog.displayLoadingDialog
import com.tiooooo.core.ui.custom.LoadingDialog.hideLoadingDialog
import org.koin.android.ext.android.inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB
    private val context: Context by inject()

    abstract fun getViewBinding(): VB

    protected open fun initView() {}
    protected open fun initListener() {}
    protected open fun setSubscribeToLiveData() {}

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        performViewBinding()
        requestedOrientation.apply {
            ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        initView()
        initListener()
        setSubscribeToLiveData()
    }

    private fun performViewBinding() {
        binding = getViewBinding()
        setContentView(binding.root)
    }

    fun isUsingToolbar(toolbar: androidx.appcompat.widget.Toolbar, isUsing: Boolean? = false) {
        if (isUsing == true) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }


    fun showLoading() = displayLoadingDialog()
    fun hideLoading() = hideLoadingDialog()
    fun populateLoadingDialog(state: Boolean? = false) {
        if (state == true) displayLoadingDialog()
        else hideLoadingDialog()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}