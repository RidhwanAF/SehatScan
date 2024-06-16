package com.healthy.sehatscan.ui.favorite

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthy.sehatscan.R
import com.healthy.sehatscan.utility.LoadingDialog

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val viewModel: FavoriteViewModel = hiltViewModel()

    // Add Remove UI State
    val isAddRemoveFavoriteLoading by viewModel.isAddRemoveLoading.collectAsStateWithLifecycle()
    val addRemoveFavoriteErrorMsg by viewModel.addRemoveErrorMessage.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
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