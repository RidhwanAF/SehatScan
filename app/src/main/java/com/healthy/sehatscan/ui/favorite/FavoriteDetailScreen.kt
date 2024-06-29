package com.healthy.sehatscan.ui.favorite

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteDetailScreen(
    id: Int,
    navigator: ThreePaneScaffoldNavigator<Int>,
    viewModel: FavoriteViewModel
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    // Data
    val favoriteList by viewModel.favoriteDrink.collectAsStateWithLifecycle()
    val favoriteData = favoriteList.find { it.favoriteId == id }?.drink

    val painter =
        rememberAsyncImagePainter(model = "https://thumb.photo-ac.com/13/130ecf0d1b3cbb04e38c509600e5f289_t.jpeg") // TODO: Change Image Drink

    // Action
    var showRemoveFavoriteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            favoriteData?.drinkName?.let {
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
                            onClick = { showRemoveFavoriteDialog = true }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_favorite_filled),
                                contentDescription = stringResource(R.string.favorite),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (favoriteData != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
            ) {
                Image(
                    painter = painter,
                    contentDescription = favoriteData.drinkName,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = favoriteData.drinkName ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(text = favoriteData.description ?: "")
                    favoriteData.ingredients?.forEach {
                        Text(text = it.fruitName ?: "")
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

        if (showRemoveFavoriteDialog) {
            AlertDialog(
                onDismissRequest = { showRemoveFavoriteDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val reqBody = FavoriteDrink.AddRemoveFavoriteReqBody(
                                favoriteData?.drinkId ?: 0
                            )
                            showRemoveFavoriteDialog = false
                            navigator.navigateBack()
                            viewModel.removeFavorite(context, reqBody)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showRemoveFavoriteDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                    ) {
                        Text(text = stringResource(R.string.no))
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.remove_favorite),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(text = stringResource(R.string.remove_favorite))
                },
                text = {
                    Text(text = stringResource(R.string.remove_favorite_list))
                }
            )
        }
    }
}