package com.tiooooo.storyapp.ui.auth.fragment

import android.widget.Toast
import androidx.navigation.Navigation
import com.tiooooo.core.ui.base.BaseFragment
import com.tiooooo.core.utils.constant.InfoEnum
import com.tiooooo.core.utils.extensions.TextListener
import com.tiooooo.core.utils.extensions.getTextWatcher
import com.tiooooo.core.utils.extensions.isEmailValid
import com.tiooooo.core.utils.extensions.snackBar
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.FragmentRegisterBinding
import com.tiooooo.storyapp.ui.auth.AuthActivity
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, AuthActivity>(R.layout.fragment_register) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initView() {
        parentActivity.initToolbar(binding.toolbar, "")
    }

    override fun initListener() {
        with(binding) {
            materialButton.setOnClickListener {
                val email = edtEmail.text.toString()
                val name = edtName.text.toString()
                val password = edtPassword.text.toString()
                viewModel.register(email, name, password)
            }

            edtEmail.addTextChangedListener(getTextWatcher { type, s ->
                when (type) {
                    TextListener.AFTER -> {
                        viewModel.textEmailRegister.value = s.isEmailValid()
                        viewModel.validateRegisterButton()
                    }
                }
            })

            edtPassword.addTextChangedListener(getTextWatcher { type, s ->
                when (type) {
                    TextListener.AFTER -> {
                        viewModel.textPasswordRegister.value = s.length >= 6
                        viewModel.validateRegisterButton()
                    }
                }
            })

            edtName.addTextChangedListener(
                getTextWatcher { type, s ->
                    when (type) {
                        TextListener.AFTER -> {
                            viewModel.textNameRegister.value = s.isNotEmpty()
                            viewModel.validateRegisterButton()
                        }
                    }
                })

        }
    }

    override fun setSubscribeToLiveData() {
        with(viewModel) {
            register.observe(viewLifecycleOwner) {
                with(binding) {
                    val email = edtEmail.text.toString()
                    val password = edtPassword.text.toString()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.success_register),
                        Toast.LENGTH_SHORT
                    ).show()
                    it?.let {
                        viewModel.login(email, password)
                    } ?: kotlin.run {
                        Navigation.findNavController(binding.root)
                            .popBackStack()
                    }
                }
            }
            registerState.observe(viewLifecycleOwner) { populateLoadingDialog(it) }
            registerError.observe(viewLifecycleOwner) { snackBar(it, InfoEnum.DANGER) }


            login.observe(viewLifecycleOwner) {
                it?.let {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_registerFragment_to_welcomeFragment)
                }
            }

            btnVerificationRegister.observe(viewLifecycleOwner) {
                Timber.d("btnVerificationRegister $it")
                binding.materialButton.isEnabled = it
            }
        }
    }
}