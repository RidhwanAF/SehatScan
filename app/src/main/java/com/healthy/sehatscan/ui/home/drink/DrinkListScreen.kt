package com.healthy.sehatscan.ui.home.drink

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.remote.drink.response.DrinkItem
import com.healthy.sehatscan.data.remote.drink.response.DrinkRecommendReqBody
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkListScreen(
    navController: NavHostController,
    viewModel: DrinkViewModel,
    fruit: String,
    onItemClicked: (DrinkItem) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()
    val reqBody = DrinkRecommendReqBody(fruit)
    val drinkList = viewModel.drinkListResult

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            if (!viewModel.isDrinkLoading)
                viewModel.getDrinkRecommendation(context, reqBody)
        }
    }
    LaunchedEffect(viewModel.isDrinkLoading) {
        if (viewModel.isDrinkLoading) pullToRefreshState.startRefresh() else pullToRefreshState.endRefresh()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.recommendation_drink))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
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
                if (viewModel.isDrinkLoading) {
                    items(5) {
                        ItemShimmerLoading()
                    }
                } else {
                    if (drinkList.isNotEmpty()) {
                        items(drinkList) { data ->
                            DrinkRecommendItem(data = data) {
                                onItemClicked(data)
                            }
                        }
                    } else {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = viewModel.drinkListErrorMessage ?: "",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.getDrinkRecommendation(
                                            context,
                                            reqBody
                                        )
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
fun DrinkRecommendItem(data: DrinkItem, onItemClicked: () -> Unit) {
    val painter =
        rememberAsyncImagePainter(model = "https://assets.clevelandclinic.org/transform/47cdb246-3c9d-4efb-8b3b-1e6b85567a16/Fruit-Juice-155376375-770x533-1_jpg") // TODO: Change Image Drink

    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onItemClicked() }
        ) {
            Image(
                painter = painter,
                contentDescription = data.drinkName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = data.drinkName ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = data.description ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun ItemShimmerLoading() {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .shimmer()
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground)
                    .size(150.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.onBackground,
                            RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth(0.9f)
                        .height(20.dp)
                )
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.onBackground,
                            RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth(0.45f)
                        .height(20.dp)
                )
                List(2) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onBackground,
                                RoundedCornerShape(8.dp)
                            )
                            .fillMaxWidth(if (it == 0) 0.75f else 0.6f)
                            .height(20.dp)
                    )
                }
            }
        }
    }
}
