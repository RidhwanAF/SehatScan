package com.healthy.sehatscan.ui.home

import android.Manifest
import android.app.Activity
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.healthy.sehatscan.classification.data.TfLiteFruitClassifier
import com.healthy.sehatscan.classification.domain.Classification
import com.healthy.sehatscan.classification.presentation.CameraPreview
import com.healthy.sehatscan.classification.presentation.FruitImageAnalyzer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScanScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current

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
        Box(
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "menu-scan-fab"),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(ContentScale.Fit),
                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                )
                .fillMaxSize()
        ) {
            CameraPreview(modifier = Modifier.fillMaxSize(), controller = controller)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                classifications.forEach {
                    Text(
                        text = it.name,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}