package com.healthy.sehatscan.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.healthy.sehatscan.MainViewModel
import com.healthy.sehatscan.R
import com.healthy.sehatscan.navigation.Route
import com.healthy.sehatscan.utility.healthyFruitTips
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()
    val mutableInteractionSource = remember {
        mutableStateOf(MutableInteractionSource())
    }
    val scope = rememberCoroutineScope()

    // Healthy Tips
    val pagerState = rememberPagerState(pageCount = { healthyFruitTips.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000)
            val nextPage = (pagerState.currentPage + 1) % healthyFruitTips.size
            scope.launch {
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(1000)
                )
            }
        }
    }

    val mainViewModel: MainViewModel = hiltViewModel()

    // FAB animation
    val fabColor = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.primaryContainer
        )
    )
    val infiniteTransition = rememberInfiniteTransition(label = "fab-color-animation")
    val fabColorRotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(7000, 0, LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fab-color-rotation-animation"
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navController.navigate(Route.ScanRoute.Scan.name)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.camera_permission_request_message),
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            with(sharedTransitionScope) {
                Box(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "menu-scan-fab"),
                            animatedVisibilityScope = animatedContentScope,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(ContentScale.FillBounds),
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                        )
                        .clickable(
                            interactionSource = mutableInteractionSource.value,
                            indication = null
                        ) {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .shadow(2.dp, RoundedCornerShape(16.dp), clip = true)
                        .drawWithContent {
                            rotate(fabColorRotationAnimation) {
                                drawCircle(
                                    brush = fabColor,
                                    radius = size.width,
                                    blendMode = BlendMode.SrcIn,
                                )
                            }
                            drawContent()
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_camera_enhance),
                            contentDescription = stringResource(R.string.start_scan),
                            tint = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            text = stringResource(R.string.start_scan),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 28.sp,
                            brush = Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        ),
                    ) {
                        append(
                            stringResource(
                                R.string.welcome_with_args,
                                mainViewModel.userName
                            )
                        )
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        ),
                    ) { append(stringResource(R.string.greeting_message)) }
                },
                lineHeight = 28.sp,
                modifier = Modifier.padding(16.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lightbulb_circle),
                        contentDescription = stringResource(R.string.healthy_tips)
                    )
                    Text(
                        text = stringResource(R.string.healthy_tips),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(16.dp),
                        pageSpacing = 16.dp,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = healthyFruitTips[page].split(":").first(),
                                fontWeight = FontWeight.Medium
                            )
                            Text(text = healthyFruitTips[page].split(": ").last())
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(72.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.start_scan_to_get_drink_recommendation),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun hasCameraPermission(context: Context) = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED