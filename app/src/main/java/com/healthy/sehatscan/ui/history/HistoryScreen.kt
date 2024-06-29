package com.healthy.sehatscan.ui.history

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.Drink

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HistoryScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Drink>()

    val viewModel: HistoryViewModel = hiltViewModel()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                HistoryListScreen(viewModel) {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                }
            }
        },
        detailPane = {
            navigator.currentDestination?.content?.let {
                AnimatedPane {
                    HistoryDetailScreen(it, navigator, viewModel)
                }
            }
        }
    )
}