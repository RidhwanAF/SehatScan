package com.healthy.sehatscan.ui.home.drink

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.DrinkItem
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DrinkDetailScreen(
    navigator: ThreePaneScaffoldNavigator<DrinkItem>,
    viewModel: DrinkViewModel,
    drinkItem: DrinkItem?
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val painter =
        rememberAsyncImagePainter(model = "https://assets.clevelandclinic.org/transform/47cdb246-3c9d-4efb-8b3b-1e6b85567a16/Fruit-Juice-155376375-770x533-1_jpg") // TODO: Change Image Drink

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
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
            ) {
                Image(
                    painter = painter,
                    contentDescription = drinkItem.drinkName,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = drinkItem.drinkName ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(text = drinkItem.description ?: "")
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