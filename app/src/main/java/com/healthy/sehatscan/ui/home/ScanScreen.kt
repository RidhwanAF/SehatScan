package com.healthy.sehatscan.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.healthy.sehatscan.R
import com.healthy.sehatscan.classification.data.TfLiteFruitClassifier
import com.healthy.sehatscan.classification.domain.Classification
import com.healthy.sehatscan.classification.presentation.CameraPreview
import com.healthy.sehatscan.classification.presentation.FruitImageAnalyzer
import com.healthy.sehatscan.utility.dashedBorder

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Center Dash Animation
    val infiniteTransition = rememberInfiniteTransition(label = "center-dash")
    val scaleDashAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.9f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale Dash Animation"
    )

    // Lock Screen Orientation
    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    if (!hasCameraPermission(context)) {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(Manifest.permission.CAMERA), 0
        )
    }

    var classifications by remember {
        mutableStateOf(emptyList<Classification>())
    }
    val analyzer = remember {
        FruitImageAnalyzer(
            classifier = TfLiteFruitClassifier(context.applicationContext),
            onResults = {
                classifications = it
            }
        )
    }

    val controller = remember {
        LifecycleCameraController(context.applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context.applicationContext),
                analyzer
            )
        }
    }

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_camera_enhance),
                                contentDescription = stringResource(R.string.detecting),
                                modifier = Modifier
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "menu-scan-icon-fab"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                            )
                            Text(text = stringResource(R.string.detecting))
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "menu-scan-fab"),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(ContentScale.Crop),
                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                )
                .fillMaxSize()
        ) { innerPadding ->
            Box {
                CameraPreview(modifier = Modifier.fillMaxSize(), controller = controller)
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .scale(scaleDashAnimation)
                        .dashedBorder(4.dp, MaterialTheme.colorScheme.primaryContainer, 16.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .align(Alignment.Center)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .align(Alignment.BottomCenter),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(classifications) { item ->
                        ElevatedSuggestionChip(
                            onClick = { /*TODO*/ },
                            label = {
                                Text(
                                    text = item.name,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }
    }
}