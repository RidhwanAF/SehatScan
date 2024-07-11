package com.healthy.sehatscan.ui.home.drink

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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.DrinkItem
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink
import com.healthy.sehatscan.navigation.Route

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun DrinkDetailScreen(
    navigator: ThreePaneScaffoldNavigator<DrinkItem>,
    navController: NavHostController,
    viewModel: DrinkViewModel,
    drinkItem: DrinkItem?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Data
    val favoriteList by viewModel.favoriteDrink.collectAsStateWithLifecycle()
    val isFavorite = remember(drinkItem?.drinkId, favoriteList) {
        derivedStateOf {
            favoriteList.any { it.drink?.drinkId == drinkItem?.drinkId }
        }
    }

    // Action
    var showAddRemoveFavoriteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            drinkItem?.drinkName?.let {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = it)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showAddRemoveFavoriteDialog = true }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (isFavorite.value) R.drawable.ic_favorite_filled
                                    else R.drawable.ic_favorite
                                ),
                                contentDescription = stringResource(
                                    if (isFavorite.value) R.string.remove_favorite_list
                                    else R.string.add_favorite_list
                                ),
                                tint = if (isFavorite.value) MaterialTheme.colorScheme.error else LocalContentColor.current
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (drinkItem != null) {
            val urlImg =
                "https://thumb.photo-ac.com/13/130ecf0d1b3cbb04e38c509600e5f289_t.jpeg" //TODO
            val painter =
                rememberAsyncImagePainter(model = urlImg) // TODO: Change Image Drink

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
                        contentDescription = drinkItem.drinkName,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "full-screen-image-${urlImg}"),
                                animatedVisibilityScope = animatedContentScope,
                                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                            )
                            .fillMaxWidth()
                            .height(250.dp)
                            .clickable {
                                navController.navigate(
                                    Route.ImageViewer(
                                        urlImg,
                                        drinkItem.drinkName ?: "drink"
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
                        text = drinkItem.drinkName ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    drinkItem.description?.let {
                        Text(
                            text = stringResource(R.string.description),
                            fontWeight = FontWeight.Medium
                        )
                        Text(text = it)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (drinkItem.ingredients?.isNotEmpty() == true) {
                        Text(
                            text = stringResource(R.string.ingredients),
                            fontWeight = FontWeight.Medium
                        )
                        drinkItem.ingredients.forEach { ingredient ->
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

        if (showAddRemoveFavoriteDialog) {
            AlertDialog(
                onDismissRequest = { showAddRemoveFavoriteDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val reqBody = FavoriteDrink.AddRemoveFavoriteReqBody(
                                drinkItem?.drinkId ?: 0
                            )
                            showAddRemoveFavoriteDialog = false
                            navigator.navigateBack()
                            if (isFavorite.value) {
                                viewModel.removeFavorite(context, reqBody)
                            } else {
                                viewModel.addFavorite(context, reqBody)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (isFavorite.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showAddRemoveFavoriteDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                    ) {
                        Text(text = stringResource(R.string.no))
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isFavorite.value) Icons.Default.Delete else Icons.Default.AddCircle,
                        contentDescription = stringResource(
                            if (isFavorite.value) R.string.remove_favorite else R.string.add_to_favorite
                        ),
                        tint = if (isFavorite.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                },
                title = {
                    Text(
                        text = stringResource(
                            if (isFavorite.value) R.string.remove_favorite else R.string.add_to_favorite
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            if (isFavorite.value) R.string.remove_favorite_list else R.string.add_favorite_list
                        )
                    )
                }
            )
        }
    }
}