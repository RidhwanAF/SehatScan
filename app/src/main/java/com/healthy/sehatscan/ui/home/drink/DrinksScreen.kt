package com.healthy.sehatscan.ui.home.drink

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.DrinkItem
import com.healthy.sehatscan.data.remote.drink.response.DrinkRecommendReqBody
import com.healthy.sehatscan.utility.LoadingDialog

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DrinksScreen(
    navController: NavHostController,
    fruit: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current
    val viewModel: DrinkViewModel = hiltViewModel()
    val navigator = rememberListDetailPaneScaffoldNavigator<DrinkItem>()

    // Get Data
    var isDataLoaded by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit, fruit, isDataLoaded) {
        val reqBody = DrinkRecommendReqBody(fruit ?: "")
        if (!isDataLoaded) {
            viewModel.getDrinkRecommendation(
                context,
                reqBody
            )
            isDataLoaded = true
        }
    }

    // Add Remove UI State
    val isAddRemoveFavoriteLoading by viewModel.isAddRemoveLoading.collectAsStateWithLifecycle()
    val addRemoveFavoriteErrorMsg by viewModel.addRemoveErrorMessage.collectAsStateWithLifecycle()


    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    DrinkListScreen(
                        navController = navController,
                        viewModel = viewModel,
                        fruit = fruit ?: ""
                    ) {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                    }
                }
            },
            detailPane = {
                val content = navigator.currentDestination?.content
                AnimatedPane {
                    DrinkDetailScreen(
                        navigator,
                        navController,
                        viewModel,
                        content,
                        sharedTransitionScope,
                        animatedContentScope
                    )
                }
            }
        )

        addRemoveFavoriteErrorMsg?.let {
            AlertDialog(
                onDismissRequest = { viewModel.clearErrorMessage() },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearErrorMessage() }) {
                        Text(text = stringResource(R.string.close))
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.warning)
                    )
                },
                title = {
                    Text(text = stringResource(R.string.failed))
                },
                text = {
                    Text(text = addRemoveFavoriteErrorMsg ?: stringResource(R.string.failed))
                }
            )
        }

        if (isAddRemoveFavoriteLoading) {
            LoadingDialog()
        }
    }
}