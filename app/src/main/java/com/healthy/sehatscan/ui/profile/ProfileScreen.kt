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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import com.healthy.sehatscan.data.remote.user.response.DiseaseDataItem
import com.healthy.sehatscan.ui.auth.AuthErrorAlertDialog
import com.healthy.sehatscan.ui.auth.AuthSuccessAlertDialog
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

    // Menu
    val listUserData = listOf(
        stringResource(R.string.name),
        stringResource(R.string.medical_history),
        stringResource(R.string.allergy)
    )

    // Data
    val listSelectedDisease by remember(viewModel.medicalListId) {
        derivedStateOf {
            viewModel.medicalListId.mapNotNull { id ->
                viewModel.diseaseListResult.find { it?.diseaseId == id }?.diseaseName
            }
        }
    }

    // Action
    var confirmChangePasswordDialog by remember { mutableStateOf(false) }
    var confirmLogoutDialog by remember { mutableStateOf(false) }
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
                ProfileItem(
                    title = stringResource(R.string.email),
                    subtitle = viewModel.email, // TODO: EMAIL
                )
                listUserData.forEachIndexed { index, title ->
                    ProfileItem(
                        title = title,
                        subtitle = when (index) {
                            0 -> viewModel.userName
                            1 -> if (listSelectedDisease.isEmpty()) stringResource(R.string.nothing) else listSelectedDisease.joinToString { it }
                            else -> viewModel.allergy
                        },
                        onClick = {
                            when (index) {
                                0 -> isOnEdit = 0
                                1 -> {
                                    viewModel.getDiseaseList() // TODO: adjust
                                }
                                else -> isOnEdit = 2
                            }
                            isOnEdit = index
                        }
                    )
                }
            }
            TextButton(
                onClick = {
                    if (!viewModel.isForgetPassLoading)
                        confirmChangePasswordDialog = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (viewModel.isForgetPassLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(R.string.change_password))
                }
            }
            Button(
                onClick = { confirmLogoutDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = stringResource(R.string.logout))
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

        viewModel.forgetPasswordResult?.let {
            AuthSuccessAlertDialog(
                message = it.meta?.message ?: stringResource(R.string.password_reset_sent),
                onDismiss = {
                    viewModel.clearResult()
                },
                onClicked = {
                    viewModel.clearResult()
                }
            )
        }

        viewModel.forgetPassErrorMessage?.let {
            AuthErrorAlertDialog(
                message = it,
                onDismiss = {
                    viewModel.clearResult()
                },
                onClicked = {
                    viewModel.clearResult()
                }
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

    if (confirmChangePasswordDialog) {
        AlertDialog(onDismissRequest = { confirmChangePasswordDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onForgetPassword()
                        confirmChangePasswordDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmChangePasswordDialog = false }
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.logout)
                )
            },
            title = {
                Text(text = stringResource(R.string.change_password))
            },
            text = {
                Text(text = stringResource(R.string.change_password_confirmation))
            }
        )
    }

    if (confirmLogoutDialog) {
        AlertDialog(onDismissRequest = { confirmLogoutDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        confirmLogoutDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmLogoutDialog = false }
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.logout),
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(text = stringResource(R.string.logout))
            },
            text = {
                Text(text = stringResource(R.string.logout_confirmation))
            }
        )
    }
}

@Composable
private fun ProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null
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
        if (onClick != null) {
            IconButton(
                onClick = {
                    onClick()
                },
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
    var searchValue by remember { mutableStateOf("") }
    val diseaseList by remember(searchValue) {
        derivedStateOf {
            if (searchValue.isEmpty()) {
                viewModel.diseaseListResult
            } else {
                viewModel.diseaseListResult.filter {
                    it?.diseaseName?.contains(searchValue, ignoreCase = true) ?: false
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.medical_history_input_info),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
        OutlinedTextField(
            value = searchValue,
            onValueChange = {
                searchValue = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            trailingIcon = {
                if (searchValue.isNotEmpty()) {
                    IconButton(onClick = { searchValue = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            },
            label = { Text(stringResource(R.string.search_medical_history)) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.isDiseaseLoading) {
                item {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator()
                    }
                }
            } else if (viewModel.diseaseListErrorMessage != null) {
                item {
                    Text(
                        text = viewModel.diseaseListErrorMessage ?: "",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                if (diseaseList.isNotEmpty()) {
                    items(diseaseList) {
                        val isChecked = remember(it) {
                            derivedStateOf {
                                viewModel.medicalListId.contains(it?.diseaseId ?: "")
                            }
                        }
                        DiseaseItem(it, isChecked.value) {
                            it?.diseaseId?.let { id -> viewModel.onMedicalHistoryChange(id) }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = stringResource(R.string.no_data),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiseaseItem(
    data: DiseaseDataItem? = null,
    isChecked: Boolean,
    onItemClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClicked() }
    ) {
        Checkbox(checked = isChecked, onCheckedChange = { onItemClicked() })
        Column {
            Text(text = data?.diseaseName ?: "", fontWeight = FontWeight.Bold)
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                stickyHeader {
                    Text(
                        text = "${stringResource(R.string.avoid)}:",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.errorContainer,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 4.dp)
                    )
                }
                itemsIndexed(data?.diseaseRestrictions ?: emptyList()) { index, item ->
                    Text(text = item?.fruit?.fruitName ?: "", fontWeight = FontWeight.Light)
                    if (index != data?.diseaseRestrictions?.lastIndex) {
                        Text(text = ", ", fontWeight = FontWeight.Light)
                    }
                }
            }
            Text(text = data?.description ?: "", maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}
