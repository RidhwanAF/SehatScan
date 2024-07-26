package com.healthy.sehatscan.ui.history

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.HistoryDrink.DrinkItem
import com.healthy.sehatscan.navigation.Route

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun HistoryDetailScreen(
    drink: DrinkItem?,
    navController: NavHostController,
    navigator: ThreePaneScaffoldNavigator<DrinkItem>,
    viewModel: HistoryViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = drink?.drink?.drinkName ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (drink != null) {
            drink.drink.let {
                val painter = rememberAsyncImagePainter(model = it?.image)

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(innerPadding)
                ) {
                    with(sharedTransitionScope) {
                        Image(
                            painter = painter,
                            contentDescription = it?.drinkName,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .sharedElement(
                                    state = rememberSharedContentState(key = "full-screen-image-${it?.image}"),
                                    animatedVisibilityScope = animatedContentScope,
                                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                                )
                                .fillMaxWidth()
                                .height(250.dp)
                                .clickable {
                                    navController.navigate(
                                        Route.ImageViewer(
                                            it?.image ?: "",
                                            it?.drinkName ?: "history"
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = it?.drinkName ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        it?.description?.let { desc ->
                            Text(
                                text = stringResource(R.string.description),
                                fontWeight = FontWeight.Medium
                            )
                            Text(text = desc)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (it?.ingredients?.isNotEmpty() == true) {
                            Text(
                                text = stringResource(R.string.ingredients),
                                fontWeight = FontWeight.Medium
                            )
                            it.ingredients.forEach { ingredient ->
                                Text(text = "- ${ingredient.fruitName ?: ""}")
                                if (ingredient.listNutrition?.isNotEmpty() == true) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(R.string.nutrition),
                                        fontWeight = FontWeight.Medium
                                    )
                                    ingredient.listNutrition.forEach { nutrition ->
                                        Text(text = "  - ${nutrition.nutritionName ?: ""}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.select_item),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}