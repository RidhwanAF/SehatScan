package com.healthy.sehatscan.ui.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.healthy.sehatscan.data.remote.drink.response.Drink
import com.healthy.sehatscan.ui.home.drink.ItemShimmerLoading
import com.healthy.sehatscan.utility.formatDate
import com.valentinilk.shimmer.shimmer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryListScreen(
    viewModel: HistoryViewModel,
    onItemClicked: (Drink) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()

    // User Input
    var sortByLatest by rememberSaveable {
        mutableStateOf(true)
    }

    // Data
    val historyList by viewModel.drinkHistoryList.collectAsStateWithLifecycle()
    val historyGroupByDate = historyList.groupBy { it.createdAt?.substringBefore("T") }
    val sortedDates = if (sortByLatest) {
        historyGroupByDate.keys.sortedByDescending {
            LocalDate.parse(
                it,
                DateTimeFormatter.ISO_DATE
            )
        }
    } else {
        historyGroupByDate.keys.sortedBy { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) }
    }

    // UI State
    val isHistoryLoading by viewModel.isHistoryLoading.collectAsStateWithLifecycle()
    val historyErrorMessage by viewModel.historyErrorMessage.collectAsStateWithLifecycle()

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            if (!isHistoryLoading)
                viewModel.getDrinkHistory()
        }
    }
    LaunchedEffect(isHistoryLoading) {
        if (isHistoryLoading) pullToRefreshState.startRefresh() else pullToRefreshState.endRefresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_history).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { sortByLatest = !sortByLatest }) {
                        AnimatedContent(
                            targetState = sortByLatest,
                            label = "Sorted Icon Animation"
                        ) {
                            if (it) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_sort_latest),
                                    contentDescription = stringResource(R.string.sort)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.ic_sort_oldest),
                                    contentDescription = stringResource(R.string.sort)
                                )
                            }
                        }
                    }
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
                if (isHistoryLoading) {
                    items(3) {
                        ItemShimmerLoading()
                    }
                } else {
                    if (sortedDates.isNotEmpty()) {
                        sortedDates.forEach { date ->
                            date?.let {
                                stickyHeader {
                                    Text(
                                        text = formatDate(date),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                    )
                                }
                            }
                            items(historyGroupByDate[date] ?: emptyList()) { historyItem ->
                                historyItem.drinks?.forEach { drink ->
                                    HistoryListItem(
                                        item = drink,
                                    ) {
                                        if (it != null) {
                                            onItemClicked(it)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (historyErrorMessage != null) historyErrorMessage
                                        ?: "" else stringResource(R.string.no_favorite_data),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.getDrinkHistory()
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
    }
}

@Composable
fun HistoryListItem(
    modifier: Modifier = Modifier,
    item: Drink?,
    onItemClicked: (Drink?) -> Unit
) {
    item?.let {
        val ingredientList = item.ingredients?.joinToString(", ") { it.fruitName ?: "" } ?: ""

        Card(
            onClick = { onItemClicked(item) },
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
                    model = "https://thumb.photo-ac.com/13/130ecf0d1b3cbb04e38c509600e5f289_t.jpeg",
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = item.drinkName ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = ingredientList, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}