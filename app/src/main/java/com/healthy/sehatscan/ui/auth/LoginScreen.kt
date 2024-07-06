package com.healthy.sehatscan.ui.auth

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthy.sehatscan.R
import com.healthy.sehatscan.navigation.Route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val userToken = viewModel.userToken

    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val passwordVisualTransformation by remember {
        mutableStateOf(PasswordVisualTransformation())
    }
    val passwordVisibilityIcon by remember(isPasswordVisible) {
        derivedStateOf {
            if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
        }
    }

    fun onLogin() {
        focusManager.clearFocus()
        viewModel.login()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearValidation()
            viewModel.clearData()
        }
    }

    // Validation
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit, viewModel.loginResult, userToken) {
        if (userToken.isNotEmpty()) {
            navController.navigate(Route.MainScreen.Home.route) {
                popUpTo(Route.AuthRoute.Auth.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_login),
                                contentDescription = stringResource(R.string.login)
                            )
                            Text(
                                stringResource(R.string.login).uppercase(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.onEmailChange(it)
                        viewModel.clearValidation()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isError = viewModel.errorMessage != null || viewModel.emailError,
                    supportingText = {
                        if (viewModel.emailError && viewModel.email.isEmpty())
                            Text(text = stringResource(R.string.email_cannot_be_empty))
                    },
                    label = { Text(stringResource(R.string.email)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                        viewModel.clearValidation()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            this.defaultKeyboardAction(imeAction = ImeAction.Done)
                            onLogin()
                        }
                    ),
                    supportingText = {
                        if (viewModel.passwordError && viewModel.password.isEmpty())
                            Text(text = stringResource(R.string.password_cannot_be_empty))
                    },
                    isError = viewModel.errorMessage != null || viewModel.passwordError,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else passwordVisualTransformation,
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = painterResource(passwordVisibilityIcon),
                                contentDescription = stringResource(R.string.password)
                            )
                        }
                    },
                    label = { Text(stringResource(R.string.password)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (!isLoading) onLogin()
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(
                            text = stringResource(R.string.login).uppercase(),
                            modifier = Modifier
                                .sharedBounds(
                                    sharedTransitionScope.rememberSharedContentState(key = "text-${R.string.login}"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                        )
                    }
                }
                TextButton(
                    onClick = {
                        viewModel.clearData()
                        viewModel.clearValidation()
                        navController.navigate(Route.AuthRoute.ForgetPassword.name) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.forget_password),
                        modifier = Modifier.Companion
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.dont_t_have_account))
                    TextButton(
                        onClick = {
                            viewModel.clearData()
                            viewModel.clearValidation()
                            navController.navigate(Route.AuthRoute.Register.name) {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.register),
                            modifier = Modifier.Companion
                                .sharedBounds(
                                    sharedTransitionScope.rememberSharedContentState(key = "text-${R.string.register}"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                        )
                    }
                }
            }

            viewModel.errorMessage?.let {
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
    }
}