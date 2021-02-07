package com.redmadrobot.app.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.redmadrobot.app.R
import com.redmadrobot.app.extension.navigateSafe
import com.redmadrobot.app.extension.observe
import com.redmadrobot.app.presentation.base.activity.BaseActivity
import com.redmadrobot.app.presentation.base.viewmodel.event.NavigationEvent
import com.redmadrobot.app.presentation.base.viewmodel.event.ViewEvent
import com.redmadrobot.app.presentation.base.viewmodel.event.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : BaseActivity() {

    companion object {
        /**
         *  Костыль который сбрасывает состояние activity и отключает восстановление fragment,
         *  в случае уничтожения процесса.
         *
         *  Не стоит его добавлять в проект без веской причины.
         *
         *  Правильным решением будет обработать и протестировать
         *  пересоздание процесса, viewModel, di графа и навигации.
         */
        private var isProcessStarted = false
    }

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        if (!isProcessStarted) savedInstanceState?.clear()
        super.onCreate(savedInstanceState)

        isProcessStarted = true
        setContentView(R.layout.activity_main)

        observe(viewModel.startScreen, ::handleStartScreen)
        observe(viewModel.events, ::handleEvents)
    }

    private fun handleStartScreen(startScreen: StartScreenViewState) {
        val navController = Navigation.findNavController(this, R.id.activity_app_container_screens)

        val mainGraph = navController.navInflater.inflate(R.navigation.root_nav_graph).apply {
            startDestination = startScreen.resId
        }

        navController.setGraph(mainGraph, startScreen.args)
    }

    @Suppress("ComplexMethod")
    private fun handleEvents(event: ViewEvent) {
        when (event) {
            is NavigationEvent -> {
                when (event) {
                    is NavigationEvent.ToDirection -> getNavController().navigateSafe(event.direction)
                    is NavigationEvent.ToRes -> getNavController().navigateSafe(event.resId, event.args)
                    is NavigationEvent.Up -> getNavController().navigateUp()
                    is NavigationEvent.Back -> if (!getNavController().popBackStack()) finish()
                    is NavigationEvent.BackTo -> getNavController().popBackStack(event.destinationId, event.inclusive)
                }
            }
        }
    }

    private fun getNavController() = findNavController(R.id.activity_app_container_screens)

}
