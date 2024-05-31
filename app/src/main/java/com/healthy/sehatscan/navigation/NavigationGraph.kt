package com.healthy.sehatscan.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import com.healthy.sehatscan.ui.auth.AuthViewModel
import com.healthy.sehatscan.ui.auth.ForgetPasswordScreen
import com.healthy.sehatscan.ui.auth.LoginScreen
import com.healthy.sehatscan.ui.auth.RegisterScreen
import com.healthy.sehatscan.ui.favorite.FavoriteScreen
import com.healthy.sehatscan.ui.history.HistoryScreen
import com.healthy.sehatscan.ui.home.HomeScreen
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Auth
            navigation(
                route = Route.Auth.Auth.name,
                startDestination = Route.Auth.Login.name
            ) {
                composable(
                    route = Route.Auth.Register.name,
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
                    route = Route.Auth.ForgetPassword.name,
                    enterTransition = { slideInVertically { it } },
                    exitTransition = { slideOutVertically { it } },
                    popEnterTransition = { slideInVertically { it } },
                    popExitTransition = { slideOutVertically { it } }
                ) {
                    val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                    ForgetPasswordScreen(
                        navController = navController,
                        viewModel = viewModel,
                        this@SharedTransitionLayout,
                        this@composable
                    )
                }
                composable(
                    route = Route.Auth.Login.name,
                    enterTransition = { scaleIn(initialScale = 0.85f) },
                    exitTransition = { scaleOut(targetScale = 0.85f) },
                    popEnterTransition = { scaleIn(initialScale = 0.85f) },
                    popExitTransition = { scaleOut(targetScale = 0.85f) }
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
                route = Route.MainScreen.Home.route
            ) {
                HomeScreen(navController = navController)
            }
            composable(route = Route.MainScreen.History.route) {
                HistoryScreen(navController = navController)
            }
            composable(route = Route.MainScreen.Favorite.route) {
                FavoriteScreen(navController = navController)
            }
            composable(route = Route.MainScreen.Profile.route) {
                ProfileScreen(navController = navController)
            }
        }
    }
}