package com.healthy.sehatscan.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink
import com.healthy.sehatscan.ui.home.drink.ItemShimmerLoading
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteListScreen(
    viewModel: FavoriteViewModel,
    onItemClicked: (Int?) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()

    // Data
    val favoriteList by viewModel.favoriteDrink.collectAsStateWithLifecycle()

    // UI State
    val isFavoriteLoading by viewModel.isFavoriteLoading.collectAsStateWithLifecycle()
    val favoriteErrorMessage by viewModel.favoriteErrorMessage.collectAsStateWithLifecycle()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            if (!isFavoriteLoading)
                viewModel.getFavoriteDrink()
        }
    }
    LaunchedEffect(isFavoriteLoading) {
        if (isFavoriteLoading) pullToRefreshState.startRefresh() else pullToRefreshState.endRefresh()
    }

    // Action
    var removeFavoriteDialog by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorite).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                if (isFavoriteLoading) {
                    items(3) {
                        ItemShimmerLoading()
                    }
                } else {
                    if (favoriteList.isNotEmpty()) {
                        items(
                            items = favoriteList,
                            key = { favoriteItem: FavoriteDrink.FavoriteItem -> favoriteItem.favoriteId }
                        ) { favoriteItem ->
                            FavoriteListItem(
                                item = favoriteItem,
                                onRemoved = {
                                    removeFavoriteDialog = it
                                }
                            ) { id ->
                                onItemClicked(id)
                            }
                        }
                    } else {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (favoriteErrorMessage != null) favoriteErrorMessage
                                        ?: "" else stringResource(R.string.no_favorite_data),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.getFavoriteDrink()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = stringResource(R.string.refresh)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }

        removeFavoriteDialog?.let {
            AlertDialog(
                onDismissRequest = { removeFavoriteDialog = null },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val reqBody = FavoriteDrink.AddRemoveFavoriteReqBody(
                                removeFavoriteDialog ?: 0
                            )
                            removeFavoriteDialog = null
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
                        onClick = { removeFavoriteDialog = null },
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

@Composable
fun FavoriteListItem(
    modifier: Modifier = Modifier,
    item: FavoriteDrink.FavoriteItem,
    onRemoved: (Int?) -> Unit,
    onItemClicked: (Int?) -> Unit
) {
    Card(
        onClick = { onItemClicked(item.favoriteId) },
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            SubcomposeAsyncImage(
                model = "https://thumb.photo-ac.com/13/130ecf0d1b3cbb04e38c509600e5f289_t.jpeg", // TODO: Change Image
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(modifier = Modifier.shimmer()) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.onBackground)
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                    }
                },
                error = {
                    Icon(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = stringResource(
                            R.string.failed_to_load_image
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                },
                clipToBounds = true,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.drink?.drinkName ?: "-",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = item.drink?.ingredients?.joinToString(", ") { it.fruitName ?: "" }
                        ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = { onRemoved(item.drink?.drinkId) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorite_filled),
                        contentDescription = stringResource(R.string.favorite),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}