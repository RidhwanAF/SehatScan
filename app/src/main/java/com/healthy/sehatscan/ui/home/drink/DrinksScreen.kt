package com.healthy.sehatscan.ui.home.drink

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.healthy.sehatscan.data.remote.drink.response.DrinkItem
import com.healthy.sehatscan.data.remote.drink.response.DrinkRecommendReqBody

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DrinksScreen(
    navController: NavHostController,
    fruit: String?
) {
    val context = LocalContext.current
    val viewModel: DrinkViewModel = hiltViewModel()
    val navigator = rememberListDetailPaneScaffoldNavigator<DrinkItem>()

    // Get Data
    LaunchedEffect(Unit, fruit) {
        val reqBody = DrinkRecommendReqBody(fruit ?: "")
        viewModel.getDrinkRecommendation(
            context,
            reqBody
        )
    }

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

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
                DrinkDetailScreen(navigator, viewModel, content)
            }
        }
    )
}