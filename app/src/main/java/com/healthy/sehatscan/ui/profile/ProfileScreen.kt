package com.healthy.sehatscan.ui.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.healthy.sehatscan.BuildConfig
import com.healthy.sehatscan.R
import com.healthy.sehatscan.appsetting.domain.AppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    val viewModel: ProfileViewModel = hiltViewModel()

    // App Theme
    val appTheme by viewModel.appTheme
    val appThemeIcon = when (appTheme) {
        AppTheme.LIGHT -> R.drawable.ic_light_mode
        AppTheme.DARK -> R.drawable.ic_dark_mode
        else -> R.drawable.ic_system_theme
    }
    val appThemeText = when (appTheme) {
        AppTheme.LIGHT -> stringResource(R.string.light)
        AppTheme.DARK -> stringResource(R.string.dark)
        else -> stringResource(R.string.system_theme)
    }
    val nextAppTheme = {
        when (appTheme) {
            AppTheme.LIGHT -> {
                viewModel.onAppThemeChange(AppTheme.DARK)
            }

            AppTheme.DARK -> {
                viewModel.onAppThemeChange(AppTheme.SYSTEM)
            }

            else -> {
                viewModel.onAppThemeChange(AppTheme.LIGHT)
            }
        }
    }
    var showAppThemeText by remember(appTheme) {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit, appTheme) {
        showAppThemeText = true
        delay(3000)
        showAppThemeText = false
    }

    val listUserData = listOf(
        stringResource(R.string.name),
        stringResource(R.string.medical_history),
        stringResource(R.string.allergy)
    )

    // Action
    var isOnEdit by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = showAppThemeText,
                            enter = fadeIn() + slideInHorizontally { it },
                            exit = slideOutHorizontally { it } + fadeOut()
                        ) {
                            Text(text = appThemeText, maxLines = 1, fontSize = 14.sp)
                        }
                        IconButton(
                            onClick = { nextAppTheme() }
                        ) {
                            AnimatedContent(
                                targetState = appThemeIcon,
                                transitionSpec = {
                                    scaleIn() togetherWith scaleOut()
                                },
                                label = "theme icon"
                            ) { targetState ->
                                when (targetState) {
                                    R.drawable.ic_light_mode -> {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_light_mode),
                                            contentDescription = stringResource(R.string.light)
                                        )
                                    }

                                    R.drawable.ic_dark_mode -> {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_dark_mode),
                                            contentDescription = stringResource(R.string.dark)
                                        )
                                    }

                                    else -> {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_system_theme),
                                            contentDescription = stringResource(R.string.system_theme)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ElevatedCard(
                modifier = Modifier.padding(16.dp)
            ) {
                listUserData.forEachIndexed { index, title ->
                    ProfileItem(
                        title = title,
                        subtitle = when (index) {
                            0 -> viewModel.userName
                            1 -> viewModel.medicalHistory
                            else -> viewModel.allergy
                        },
                        onClick = { isOnEdit = index }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${stringResource(R.string.app_name)} v.${BuildConfig.VERSION_NAME}",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    isOnEdit?.let {
        ModalBottomSheet(
            onDismissRequest = {
                isOnEdit = null // TODO : ResetData
            }
        ) {
            when (isOnEdit) {
                0 -> NameEditTextField(viewModel = viewModel) {
                    isOnEdit = null
                    // TODO : OnDone Edit
                }

                1 -> MedicalHistoryTextField(viewModel = viewModel) {
                    isOnEdit = null
                    // TODO : OnDone Edit
                }

                else -> AllergyTextField(viewModel = viewModel) {
                    isOnEdit = null
                    // TODO : OnDone Edit
                }
            }
        }
    }
}

@Composable
private fun ProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(text = subtitle)
        }
        IconButton(
            onClick = { onClick() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit)
            )
        }
    }
}

@Composable
fun NameEditTextField(viewModel: ProfileViewModel, onDone: () -> Unit) {
    var isError by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        OutlinedTextField(
            value = viewModel.userName,
            onValueChange = {
                viewModel.onUserNameChange(it)
                isError = viewModel.userName.trim().isEmpty()
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            isError = isError,
            supportingText = {
                if (viewModel.userName.trim().isEmpty() && isError)
                    Text(text = stringResource(R.string.name_cannot_be_empty))
            },
            label = { Text(stringResource(R.string.name)) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onDone() },
            enabled = !isError && viewModel.userName.trim().isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.simpan))
        }
    }
}

@Composable
fun MedicalHistoryTextField(viewModel: ProfileViewModel, onDone: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        OutlinedTextField(
            value = viewModel.medicalHistory,
            onValueChange = {
                viewModel.onMedicalHistoryChange(it)
            },
            minLines = 3,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            label = { Text(stringResource(R.string.medical_history)) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.medical_history_input_info),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
        Button(
            onClick = { onDone() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.simpan))
        }
    }
}

@Composable
fun AllergyTextField(viewModel: ProfileViewModel, onDone: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        OutlinedTextField(
            value = viewModel.allergy,
            onValueChange = {
                viewModel.onAllergyChange(it)
            },
            minLines = 3,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            label = { Text(stringResource(R.string.allergy)) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.allergy_input_info),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
        Button(
            onClick = { onDone() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.simpan))
        }
    }
}
