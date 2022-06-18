package com.tiooooo.storyapp.ui.auth.fragment

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.navigation.Navigation
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.storyapp.MainActivity
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentSplashBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SplashFragment : BaseFragment<FragmentSplashBinding, AuthActivity>(R.layout.fragment_splash) {
    private val vieModel: AuthViewModel by viewModel()

    override fun initView() {
//        vieModel.isLogin()
//        vieModel.getToken()
    }

    override fun setSubscribeToLiveData() {
//        vieModel.token.observe(viewLifecycleOwner) {
//            Timber.d("isLogin : $it")
//            if (it.isNotEmpty()) {
//                parentActivity.startActivity(Intent(requireContext(), MainActivity::class.java))
//                parentActivity.finish()
//            } else {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    Navigation.findNavController(binding.root)
//                        .navigate(R.id.action_splashFragment_to_loginFragment)
//                }, 3000)
//            }
//        }
        vieModel.getToken().observe(viewLifecycleOwner){
                        Timber.d("isLogin : $it")
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