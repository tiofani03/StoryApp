package com.tiooooo.storyapp.ui.auth.fragment

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.storyapp.ui.main.MainActivity
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentWelcomeBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity


class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, AuthActivity>(R.layout.fragment_welcome) {

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(requireContext(), MainActivity::class.java))
            parentActivity.finish()
        }, 2000)
    }

}