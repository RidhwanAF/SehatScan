package com.healthy.sehatscan.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.healthy.sehatscan.ui.auth.AuthViewModel
import com.healthy.sehatscan.ui.auth.ForgetPasswordScreen
import com.healthy.sehatscan.ui.auth.LoginScreen
import com.healthy.sehatscan.ui.auth.RegisterScreen
import com.healthy.sehatscan.ui.favorite.FavoriteScreen
import com.healthy.sehatscan.ui.history.HistoryScreen
import com.healthy.sehatscan.ui.home.HomeScreen
import com.healthy.sehatscan.ui.home.ScanScreen
import com.healthy.sehatscan.ui.home.drink.DrinksScreen
import com.healthy.sehatscan.ui.profile.ProfileScreen
import com.healthy.sehatscan.utility.sharedViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Auth
            navigation(
                route = Route.AuthRoute.Auth.name,
                startDestination = Route.AuthRoute.Login.name
            ) {
                composable(
                    route = Route.AuthRoute.Register.name,
                    enterTransition = { slideInVertically { it } },
                    exitTransition = { slideOutVertically { it } },
                    popEnterTransition = { slideInVertically { it } },
                    popExitTransition = { slideOutVertically { it } }
                ) {
                    val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                    RegisterScreen(
                        navController = navController,
                        viewModel = viewModel,
                        this@SharedTransitionLayout,
                        this@composable
                    )
                }
                composable(
                    route = Route.AuthRoute.ForgetPassword.name,
                    enterTransition = { slideInVertically { it } },
                    exitTransition = { slideOutVertically { it } },
                    popEnterTransition = { slideInVertically { it } },
                    popExitTransition = { slideOutVertically { it } }
                ) {
                    val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                    ForgetPasswordScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = Route.AuthRoute.Login.name,
                    enterTransition = { fadeIn() + scaleIn(initialScale = 0.85f) },
                    exitTransition = { scaleOut(targetScale = 0.85f) + fadeOut() },
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() }
                ) {
                    val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                    LoginScreen(
                        navController = navController,
                        viewModel = viewModel,
                        this@SharedTransitionLayout,
                        this@composable
                    )
                }
            }

            // MainScreen
            composable(
                route = Route.MainScreen.Home.route,
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }
            ) {
                HomeScreen(
                    navController = navController,
                    this@SharedTransitionLayout,
                    this@composable
                )
            }
            composable(route = Route.MainScreen.History.route) {
                HistoryScreen()
            }
            composable(route = Route.MainScreen.Favorite.route) {
                FavoriteScreen()
            }
            composable(route = Route.MainScreen.Profile.route) {
                ProfileScreen()
            }

            // Other
            composable(
                route = Route.ScreenRoute.ImageScan.name,
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { it } },
                popEnterTransition = { slideInVertically { it } },
                popExitTransition = { slideOutVertically { it } }
            ) {
                ScanScreen(
                    navController = navController,
                    this@SharedTransitionLayout,
                    this@composable
                )
            }
            composable<Route.Drink>(
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { it } },
                popEnterTransition = { slideInVertically { it } },
                popExitTransition = { slideOutVertically { it } }
            ) {
                val args = it.toRoute<Route.Drink>()
                DrinksScreen(navController = navController, args.fruit)
            }
        }
    }
}