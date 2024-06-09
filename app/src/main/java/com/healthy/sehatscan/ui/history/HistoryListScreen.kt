package com.healthy.sehatscan.ui.history

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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.healthy.sehatscan.R
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryListScreen(
    onItemClicked: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            ),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(100) {
                HistoryListItem(
                    item = "$it"
                ) {
                    onItemClicked(it)
                }
            }
        }
    }
}

@Composable
fun HistoryListItem(
    modifier: Modifier = Modifier,
    item: String,
    onItemClicked: (String) -> Unit
) {
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
                    text = "Juice $item",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "Apel, Jeruk", maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Jus untuk memenuhi asupan vitamin harianmu",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "23 Feb 2024 12:00",
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}