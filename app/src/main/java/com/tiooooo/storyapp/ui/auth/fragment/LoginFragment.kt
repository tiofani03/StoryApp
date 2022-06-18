package com.tiooooo.storyapp.ui.auth.fragment


import android.content.Intent
import android.widget.Toast
import androidx.navigation.Navigation
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.storyapp.MainActivity
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentLoginBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding, AuthActivity>(R.layout.fragment_login) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initView() {
        with(binding) {
            tvRegister.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun initListener() {
        with(binding) {
            materialButton.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                viewModel.login(email, password)
            }
        }
    }

    override fun setSubscribeToLiveData() {
        viewModel.login.observe(viewLifecycleOwner) {
            it?.let {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                parentActivity.finish()
            } ?: run {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) {
            showToast("$it")
        }

        viewModel.loginError.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}