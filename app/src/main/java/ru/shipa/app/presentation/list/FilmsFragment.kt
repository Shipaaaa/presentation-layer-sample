package ru.shipa.app.presentation.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import ru.shipa.app.R
import ru.shipa.app.databinding.FragmentFilmsBinding
import ru.shipa.app.extension.isNetworkException
import ru.shipa.app.extension.makeGone
import ru.shipa.app.extension.makeVisible
import ru.shipa.app.extension.observe
import ru.shipa.app.extension.viewbinding.viewBinding
import ru.shipa.app.presentation.base.fragment.BaseFragment
import ru.shipa.app.presentation.base.recycler.item.ProgressItem
import ru.shipa.app.presentation.base.viewmodel.event.observe
import ru.shipa.app.presentation.base.viewmodel.state.Content
import ru.shipa.app.presentation.base.viewmodel.state.Error
import ru.shipa.app.presentation.base.viewmodel.state.Loading
import ru.shipa.app.presentation.list.recycler.FilmsItem

/**
 * Пример сложного экрана с несколькими вложенными состояниями на основе популярных и любимых фильмов.
 */
@AndroidEntryPoint
class FilmsFragment : BaseFragment(R.layout.fragment_films) {

    private val viewModel: FilmsViewModel by viewModels()

    private val binding: FragmentFilmsBinding by viewBinding()

    private val favouriteSection = Section()
    private val popularSection = Section()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.events, ::handleEvents)
        observe(viewModel.filmsViewState, ::handleFilmsViewState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun getMessagesContainer() = R.id.container

    private fun initViews() {
        with(binding.swipeToRefresh) {
            setOnRefreshListener { viewModel.onRefresh() }
        }

        with(binding.recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(favouriteSection)
                add(popularSection)
            }
        }

        binding.errorView.setOnRetryClickListener { viewModel.onRetryClick() }
    }

    private fun handleFilmsViewState(state: FilmsViewState) {
        handleFavouriteFilms(state)
        handlePopularFilms(state)

        with(binding) {
            if (state.favouriteFilmsState is Error && state.popularFilmsState is Error) {
                errorView.makeVisible()
                errorView.showError(state.favouriteFilmsState.exception.isNetworkException())
            } else {
                errorView.makeGone()
            }

            swipeToRefresh.isRefreshing = false
        }
    }

    private fun handleFavouriteFilms(state: FilmsViewState) {

        val favouriteFilmsItems = when (state.favouriteFilmsState) {
            is Loading -> listOf(ProgressItem())

            is Content -> {
                listOf(
                    FilmsItem(
                        title = "Избранные фильмы",
                        films = state.favouriteFilmsState.content,
                        onFilmClick = viewModel::onDetailsClicked
                    )
                )
            }

            else -> emptyList()
        }

        binding.recycler.post {
            favouriteSection.update(favouriteFilmsItems)
        }

    }

    private fun handlePopularFilms(state: FilmsViewState) {

        val popularFilmsItems = when (state.popularFilmsState) {
            is Loading -> listOf(ProgressItem())

            is Content -> {
                listOf(
                    FilmsItem(
                        title = "Популярные фильмы",
                        films = state.popularFilmsState.content,
                        onFilmClick = viewModel::onDetailsClicked
                    )
                )
            }

            else -> emptyList()
        }

        binding.recycler.post {
            popularSection.update(popularFilmsItems)
        }

    }

}
