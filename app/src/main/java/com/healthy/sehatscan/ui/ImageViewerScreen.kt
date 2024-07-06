package com.healthy.sehatscan.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.healthy.sehatscan.R
import com.healthy.sehatscan.navigation.Route
import com.healthy.sehatscan.ui.theme.backgroundDark
import com.healthy.sehatscan.ui.theme.backgroundLight
import com.healthy.sehatscan.utility.stringToBitmap
import com.smarttoolfactory.zoom.animatedZoom
import com.smarttoolfactory.zoom.rememberAnimatedZoomState
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(
    navController: NavHostController,
    data: Route.ImageViewer,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val animatedZoomState = rememberAnimatedZoomState(
        maxZoom = 2f
    )
    val image: Any? = if (data.isImageBitmap) stringToBitmap(data.image) else data.image

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = data.label ?: "")
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
                        scrolledContainerColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(backgroundDark)
                    .fillMaxSize()
                    .animatedZoom(animatedZoomState = animatedZoomState)
            ) {
                Spacer(modifier = Modifier.padding(innerPadding))
                SubcomposeAsyncImage(
                    model = image,
                    contentDescription = data.label,
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(backgroundLight)
                                    .fillMaxSize()
                            )
                        }
                    },
                    error = {
                        Icon(
                            painter = painterResource(R.drawable.ic_broken_image),
                            contentDescription = stringResource(R.string.failed_to_load_image),
                            tint = backgroundLight
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .sharedElement(
                            state = rememberSharedContentState(key = "full-screen-image-${data.image}"),
                            animatedVisibilityScope = animatedContentScope,
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                        )
                )
            }
        }
    }
}