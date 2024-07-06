package com.healthy.sehatscan.ui.home.scan

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R
import com.healthy.sehatscan.navigation.Route
import com.healthy.sehatscan.utility.bitmapToString
import com.healthy.sehatscan.utility.rotateBitmap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ScanResultScreen(
    navController: NavHostController,
    viewModel: ScanViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = viewModel.scanResult?.name
                                ?: stringResource(R.string.fruit)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                viewModel.scanResult?.let {
                    ExtendedFloatingActionButton(
                        onClick = {
                            navController.navigate(Route.Drink(it.name)) {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.get_recommendation))
                    }
                }
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
            ) {
                viewModel.scanResult?.let {
                    val imgBitmap = rotateBitmap(it.image, 90f)
                    val painter = rememberAsyncImagePainter(model = imgBitmap)
                    val bitmapToString = imgBitmap?.let { it1 -> bitmapToString(it1) }

                    Box(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "menu-scan-fab"),
                                animatedVisibilityScope = animatedContentScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(
                                    ContentScale.Crop
                                ),
                                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                            )
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = it.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(key = "full-screen-image-${bitmapToString}"),
                                    animatedVisibilityScope = animatedContentScope,
                                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                                )
                                .clickable {
                                    bitmapToString?.let { bitmap ->
                                        navController.navigate(
                                            Route.ImageViewer(
                                                bitmap,
                                                it.name,
                                                true
                                            )
                                        ) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                        )
                    }
                    Text(
                        text = it.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}