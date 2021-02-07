package com.redmadrobot.app.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentFilmDetailsBinding
import com.redmadrobot.app.domain.model.Film
import com.redmadrobot.app.extension.isNetworkException
import com.redmadrobot.app.extension.observe
import com.redmadrobot.app.extension.setImageFromUrl
import com.redmadrobot.app.extension.viewbinding.viewBinding
import com.redmadrobot.app.presentation.base.fragment.BaseFragment
import com.redmadrobot.app.presentation.base.statedelegate.delegate.LceStateDelegate
import com.redmadrobot.app.presentation.base.viewmodel.event.observe
import com.redmadrobot.app.presentation.base.viewmodel.state.Content
import com.redmadrobot.app.presentation.base.viewmodel.state.Error
import com.redmadrobot.app.presentation.base.viewmodel.state.LceState
import com.redmadrobot.app.presentation.base.viewmodel.state.Loading
import dagger.hilt.android.AndroidEntryPoint

/**
 * Пример простого экрана, оторажающего детали фильма.
 */
@AndroidEntryPoint
class FilmDetailsFragment : BaseFragment(R.layout.fragment_film_details) {

    private val args: FilmDetailsFragmentArgs by navArgs()

    private val viewModel: FilmDetailsViewModel by viewModels()

    private val binding: FragmentFilmDetailsBinding by viewBinding()

    private lateinit var screenState: LceStateDelegate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.details, ::handleState)
        observe(viewModel.events, ::handleEvents)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        with(binding) {
            screenState = LceStateDelegate(
                contentView = content,
                loadingView = progressContainer.root,
                errorView = errorView
            )

            errorView.setOnRetryClickListener { viewModel.onRetryClick() }
        }
    }

    private fun handleState(state: LceState<Film>) {
        when (state) {
            is Loading -> showLoading()
            is Content -> handleContent(state.content)
            is Error -> showError(state)
        }
    }

    private fun showLoading() {
        screenState.showLoading()
    }

    private fun handleContent(film: Film) {
        with(binding) {
            image.setImageFromUrl(film.imageUrl)
            textViewTitle.text = film.title
            textViewDescription.text = film.description
        }
        screenState.showContent()
    }

    private fun showError(state: Error<Film>) {
        binding.errorView.showError(state.exception.isNetworkException())
        screenState.showError()
    }
}
