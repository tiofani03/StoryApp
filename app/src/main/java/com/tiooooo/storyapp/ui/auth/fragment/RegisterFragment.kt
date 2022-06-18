package com.tiooooo.storyapp.ui.auth.fragment

import androidx.navigation.Navigation
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentRegisterBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, AuthActivity>(R.layout.fragment_register) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initListener() {
        with(binding) {
            materialButton.setOnClickListener {
                val email = edtEmail.text.toString()
                val name = edtName.text.toString()
                val password = edtPassword.text.toString()
                viewModel.register(email, name, password)
            }
        }
    }

    override fun setSubscribeToLiveData() {
        with(viewModel) {
            register.observe(viewLifecycleOwner) {
                with(binding) {
                    val email = edtEmail.text.toString()
                    val name = edtName.text.toString()
                    val password = edtPassword.text.toString()
                    it?.let {
                        viewModel.login(email, password)
                    }
                }
            }

            registerState.observe(viewLifecycleOwner) {
                Timber.d("registerState: $it")
            }

            login.observe(viewLifecycleOwner) {
                it?.let {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_registerFragment_to_welcomeFragment)
                }
            }
        }
    }
}