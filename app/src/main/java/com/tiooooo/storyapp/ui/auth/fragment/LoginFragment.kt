package com.tiooooo.storyapp.ui.auth.fragment


import android.content.Intent
import android.widget.Toast
import androidx.navigation.Navigation
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.core.utils.constant.InfoEnum
import com.tiooooo.core.utils.extensions.TextListener
import com.tiooooo.core.utils.extensions.getTextWatcher
import com.tiooooo.core.utils.extensions.isEmailValid
import com.tiooooo.core.utils.extensions.snackBar
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentLoginBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import com.tiooooo.storyapp.ui.main.MainActivity
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

            edtEmail.addTextChangedListener(getTextWatcher { type, s ->
                when (type) {
                    TextListener.AFTER -> {
                        viewModel.textEmailLogin.value = s.isEmailValid()
                        viewModel.validateLoginButton()
                    }
                }
            })

            edtPassword.addTextChangedListener(getTextWatcher { type, s ->
                when (type) {
                    TextListener.AFTER -> {
                        viewModel.textPasswordLogin.value = s.length >= 6
                        viewModel.validateLoginButton()
                    }
                }
            })

        }
    }

    override fun setSubscribeToLiveData() {
        viewModel.login.observe(viewLifecycleOwner) {
            it?.let {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_success),
                    Toast.LENGTH_SHORT
                ).show()
                parentActivity.finish()
            } ?: run {
                snackBar(getString(R.string.login_failed), InfoEnum.DANGER)
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) {
            populateLoadingDialog(it)
        }

        viewModel.loginError.observe(viewLifecycleOwner) {
            snackBar(it, InfoEnum.DANGER)
        }

        viewModel.btnVerificationLogin.observe(viewLifecycleOwner) {
            binding.materialButton.isEnabled = it
        }
    }
}