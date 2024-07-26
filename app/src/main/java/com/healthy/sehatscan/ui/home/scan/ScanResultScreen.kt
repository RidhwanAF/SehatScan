package com.healthy.sehatscan.ui.home.scan

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R
import com.healthy.sehatscan.navigation.Route
import com.healthy.sehatscan.utility.bitmapToString
import com.healthy.sehatscan.utility.rotateBitmap
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ScanResultScreen(
    navController: NavHostController,
    viewModel: ScanViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val fruitLoading by viewModel.isFruitLoading.collectAsStateWithLifecycle()
    val fruitErrorMessage by viewModel.fruitDetailErrorMessage.collectAsStateWithLifecycle()
    val fruitDetail by viewModel.fruitDetail.collectAsStateWithLifecycle()

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
                    if (fruitDetail != null) {
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
                    if (fruitLoading) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .shimmer()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.onBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .fillMaxWidth(0.5f)
                                    .height(16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.onBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .fillMaxWidth(0.5f)
                                    .height(16.dp)
                            )
                            List(3) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.onBackground,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .size(16.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.onBackground,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .fillMaxWidth(0.35f)
                                            .height(16.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (fruitDetail != null) {
                                fruitDetail?.let { data ->
                                    Text(
                                        text = data.fruitName ?: "",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if (data.listFruitNutrition?.isNotEmpty() == true) {
                                        Text(
                                            text = stringResource(R.string.nutrition),
                                            fontWeight = FontWeight.Medium
                                        )
                                        data.listFruitNutrition.forEach { nutrition ->
                                            Text(text = "- ${nutrition.nutritionName ?: ""}")
                                        }
                                    }
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(text = stringResource(R.string.no_data))
                                    Button(onClick = {
                                        viewModel.clearFruitDetailState()
                                        navController.navigateUp()
                                    }) {
                                        Text(text = stringResource(R.string.scan_again))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            fruitErrorMessage?.let {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.clearFruitDetailState()
                        navController.navigateUp()
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.getFruitDetail(
                                context,
                                viewModel.scanResult?.name ?: ""
                            )
                        }) {
                            Text(text = stringResource(R.string.try_again))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            viewModel.clearFruitDetailState()
                            navController.navigateUp()
                        }) {
                            Text(text = stringResource(R.string.back))
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = stringResource(R.string.failed)
                        )
                    },
                    title = {
                        Text(text = stringResource(R.string.failed))
                    },
                    text = {
                        Text(text = it)
                    }
                )
            }
        }
    }
}