package com.tiooooo.storyapp.ui.auth.fragment

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.navigation.Navigation
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentSplashBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import com.tiooooo.storyapp.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : BaseFragment<FragmentSplashBinding, AuthActivity>(R.layout.fragment_splash) {
    private val vieModel: AuthViewModel by viewModel()

    override fun setSubscribeToLiveData() {
        vieModel.getToken().observe(viewLifecycleOwner){
            if (it.isNotEmpty()) {
                parentActivity.startActivity(Intent(requireContext(), MainActivity::class.java))
                parentActivity.finish()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_splashFragment_to_loginFragment)
                }, 3000)
            }
        }
    }
}