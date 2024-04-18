package com.indisp.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.indisp.crypto.coin.ui.CoinsListScreen
import com.indisp.crypto.coin.ui.CoinsListViewModel
import com.indisp.crypto.coin.ui.CoinsSearchScreen
import com.indisp.crypto.coin.ui.CoinsSearchViewModel
import com.indisp.crypto.navigation.Route
import com.indisp.designsystem.theme.CryptoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ANIM_DURATION = 300
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Route.COINS_LIST.name,
                    enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) },
                    exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) },
                    popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
                    popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
                ) {
                    composable(Route.COINS_LIST.name) {
                        val coinsListViewModel: CoinsListViewModel by viewModel()
                        CoinsListScreen(
                            screenStateFlow = coinsListViewModel.screenStateFlow,
                            sideEffectFlow = coinsListViewModel.sideEffectFlow,
                            processEvent = coinsListViewModel::processEvent,
                            navigateToSearchScreen = { navController.navigate(Route.COINS_SEARCH.name) }
                        )
                    }

                    composable(Route.COINS_SEARCH.name) {
                        val coinsSearchViewModel: CoinsSearchViewModel by viewModel()
                        CoinsSearchScreen(
                            screenStateFlow = coinsSearchViewModel.screenStateFlow,
                            processEvent = coinsSearchViewModel::processEvent,
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}