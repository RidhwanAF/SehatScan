package com.healthy.sehatscan

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.healthy.sehatscan.navigation.NavigationGraph
import com.healthy.sehatscan.navigation.Route
import com.healthy.sehatscan.ui.theme.SehatScanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Splash Screen
        val content: View = findViewById(android.R.id.content)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val alpha = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.ALPHA,
                    1f,
                    0f
                )
                alpha.interpolator = AnticipateInterpolator()
                alpha.duration = 100L

                alpha.doOnEnd { splashScreenView.remove() }

                alpha.start()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(content.rootView) { view, insetsCompat ->
//            val navigationBottomPadding =
//                insetsCompat.getInsets(WindowInsetsCompat.Type.systemGestures()).bottom
            val bottomInset = insetsCompat.getInsets(WindowInsetsCompat.Type.ime()).bottom

//            systemNavPadding.intValue = if (bottomInset > 0) 0 else navigationBottomPadding

            view.updatePadding(bottom = bottomInset)
            insetsCompat
        }

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            SehatScanTheme {
                Scaffold(
                    bottomBar = {
                        BottomBarContent(
                            navHostController = navController,
                            currentDestination = currentDestination
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        innerPadding = innerPadding,
                        startDestination = Route.AuthRoute.Auth.name
//                        startDestination = Route.MainScreen.Home.route
                    )
                }
            }
        }
    }

    @Composable
    private fun BottomBarContent(
        navHostController: NavHostController,
        currentDestination: NavDestination?
    ) {
        val bottomBarMenu = listOf(
            Route.MainScreen.Home,
            Route.MainScreen.History,
            Route.MainScreen.Favorite,
            Route.MainScreen.Profile
        )

        // Show bottom bar only when current destination is in bottomBarMenu
        val showBottomBar by remember(currentDestination, navHostController) {
            derivedStateOf {
                val isShown = bottomBarMenu.any { it.route == currentDestination?.route }
                mutableStateOf(isShown)
            }
        }

        AnimatedVisibility(
            visible = showBottomBar.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            BottomAppBar {
                bottomBarMenu.forEach {
                    val selected = it.route == currentDestination?.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navHostController.navigate(it.route) {
                                popUpTo(bottomBarMenu.first().route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            AnimatedVisibility(
                                visible = selected,
                                enter = scaleIn(),
                                exit = fadeOut()
                            ) {
                                Icon(
                                    painter = painterResource(it.selectedIcon),
                                    contentDescription = stringResource(it.label)
                                )
                            }
                            AnimatedVisibility(
                                visible = !selected,
                                enter = fadeIn(),
                                exit = scaleOut()
                            ) {
                                Icon(
                                    painter = painterResource(it.unselectedIcon),
                                    contentDescription = stringResource(it.label)
                                )
                            }
                        },
                        label = {
                            Text(
                                stringResource(it.label),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }
    }
}
