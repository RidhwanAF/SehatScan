package com.healthy.sehatscan.ui.favorite

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val viewModel: FavoriteViewModel = hiltViewModel()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                FavoriteListScreen(viewModel) {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                }
            }
        },
        detailPane = {
            navigator.currentDestination?.content?.let {
                AnimatedPane {
                    FavoriteDetailScreen(it, navigator, viewModel)
                }
            }
        }
    )
}