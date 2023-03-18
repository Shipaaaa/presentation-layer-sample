package ru.shipa.app.presentation.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.shipa.app.R
import ru.shipa.app.databinding.FragmentFilmDetailsBinding
import ru.shipa.app.domain.model.Film
import ru.shipa.app.extension.isNetworkException
import ru.shipa.app.extension.observe
import ru.shipa.app.extension.setImageFromUrl
import ru.shipa.app.extension.viewbinding.viewBinding
import ru.shipa.app.presentation.base.fragment.BaseFragment
import ru.shipa.app.presentation.base.statedelegate.delegate.LceStateDelegate
import ru.shipa.app.presentation.base.viewmodel.event.observe
import ru.shipa.app.presentation.base.viewmodel.state.Content
import ru.shipa.app.presentation.base.viewmodel.state.Error
import ru.shipa.app.presentation.base.viewmodel.state.LceState
import ru.shipa.app.presentation.base.viewmodel.state.Loading
import ru.shipa.app.presentation.base.viewmodel.state.Stub
import ru.shipa.app.presentation.utils.viewModels
import javax.inject.Inject

/**
 * Пример простого экрана, оторажающего детали фильма.
 */
@AndroidEntryPoint
class FilmDetailsFragment : BaseFragment(R.layout.fragment_film_details) {

    private val args: FilmDetailsFragmentArgs by navArgs()


    @Inject
    lateinit var viewModelFactory: FilmDetailsViewModel.Factory

    private val viewModel: FilmDetailsViewModel by viewModels {
        viewModelFactory.create(args.id)
    }

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
            is Stub -> {
                // TODO
            }
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