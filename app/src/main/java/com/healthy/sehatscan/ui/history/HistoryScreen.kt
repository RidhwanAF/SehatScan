package com.healthy.sehatscan.ui.history

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HistoryScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            HistoryListScreen {
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
            }
        },
        detailPane = {
            val content = navigator.currentDestination?.content?.toString() ?: "Select an item"
            HistoryDetailScreen(content)
        }
    )
}