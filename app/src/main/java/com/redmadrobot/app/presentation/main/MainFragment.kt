package com.redmadrobot.app.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentMainBinding
import com.redmadrobot.app.extension.*
import com.redmadrobot.app.extension.viewbinding.viewBinding
import com.redmadrobot.app.presentation.base.fragment.BaseFragment
import com.redmadrobot.app.presentation.base.viewmodel.event.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()

    private val binding: FragmentMainBinding by viewBinding()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.events, ::handleEvents)
        observe(viewModel.login, ::handleLogin)
        observe(viewModel.password, ::handlePassword)
        observe(viewModel.buttonEnabled, ::handleButton)
        observe(viewModel.needShowProgress, ::handleProgress)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        with(binding) {
            login.setOnTextChangeListener { onCredentialsChanged() }
            password.setOnTextChangeListener { onCredentialsChanged() }

            buttonToList.setOnClickListener { onToListButtonClick() }
        }
    }

    private fun onCredentialsChanged() {
        viewModel.onCredentialsChanged(
            binding.login.text(),
            binding.password.text()
        )
    }

    private fun onToListButtonClick() {
        requireActivity().hideKeyboard()
        viewModel.onEnterButtonClick(
            binding.login.text(),
            binding.password.text()
        )
    }

    private fun handleLogin(login: String) {
        binding.login.apply {
            if (text() != login) setText(login)
        }
    }

    private fun handlePassword(password: String) {
        binding.password.apply {
            if (text() != password) setText(password)
        }
    }

    private fun handleButton(buttonEnabled: Boolean) {
        binding.buttonToList.isEnabled = buttonEnabled
    }

    private fun handleProgress(needShowProgress: Boolean) {
        binding.progressContainer.root.makeVisibleOrGone(needShowProgress)
    }

}
