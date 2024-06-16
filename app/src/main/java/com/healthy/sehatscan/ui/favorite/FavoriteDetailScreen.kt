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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.healthy.sehatscan.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteDetailScreen(
    id: Int,
    navigator: ThreePaneScaffoldNavigator<Int>,
    viewModel: FavoriteViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    // Data
    val favoriteList by viewModel.favoriteDrink.collectAsStateWithLifecycle()
    val favoriteData = favoriteList.find { it.favoriteId == id }?.drink

    val painter =
        rememberAsyncImagePainter(model = "https://thumb.photo-ac.com/13/130ecf0d1b3cbb04e38c509600e5f289_t.jpeg") // TODO: Change Image Drink

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