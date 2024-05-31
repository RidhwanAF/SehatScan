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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Validation
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    var isConfirmPasswordVisible by remember {
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
    val confirmPasswordVisibilityIcon by remember(isConfirmPasswordVisible) {
        derivedStateOf {
            if (isConfirmPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
        }
    }
    val isButtonEnabled by remember(viewModel) {
        derivedStateOf {
            !viewModel.emailError && !viewModel.passwordError &&
                    !viewModel.confirmPasswordError &&
                    viewModel.email.isNotEmpty() &&
                    viewModel.password.isNotEmpty() &&
                    viewModel.confirmPassword.isNotEmpty()
        }
    }

    fun onRegister() {
        focusManager.clearFocus()
        // TODO: connect to API
//        viewModel.register()
        navController.navigateUp()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearValidation()
            viewModel.clearData()
        }
    }

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.register).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.Companion
                                .sharedBounds(
                                    sharedTransitionScope.rememberSharedContentState(key = "text-${R.string.register}"),
                                    animatedVisibilityScope = animatedContentScope,
                                )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                )
            }
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
                        viewModel.onValidatingEmail()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isError = viewModel.emailError,
                    supportingText = {
                        if (viewModel.emailError) {
                            if (viewModel.email.isEmpty()) {
                                Text(text = stringResource(R.string.email_cannot_be_empty))
                            } else if (!viewModel.onValidatingEmail()) {
                                Text(
                                    text = stringResource(R.string.email_format_invalid) + "\n" +
                                            stringResource(R.string.email_example)
                                )
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.email)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                        viewModel.onValidatingPassword()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    supportingText = {
                        if (viewModel.passwordError) {
                            if (viewModel.password.isEmpty()) {
                                Text(text = stringResource(R.string.password_cannot_be_empty))
                            } else if (!viewModel.onValidatingPassword()) {
                                Text(text = stringResource(R.string.password_must_be_at_least_8_characters))
                            }
                        }
                    },
                    isError = viewModel.passwordError,
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
                OutlinedTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = {
                        viewModel.onConfirmPasswordChange(it)
                        viewModel.onValidatingConfirmPassword()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    supportingText = {
                        if (viewModel.confirmPasswordError) {
                            if (viewModel.confirmPassword.isEmpty()) {
                                Text(text = stringResource(R.string.password_cannot_be_empty))
                            } else if (!viewModel.onValidatingConfirmPassword()) {
                                Text(text = stringResource(R.string.password_doesn_t_match))
                            }
                        }
                    },
                    isError = viewModel.confirmPasswordError,
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else passwordVisualTransformation,
                    trailingIcon = {
                        IconButton(onClick = {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        }) {
                            Icon(
                                painter = painterResource(confirmPasswordVisibilityIcon),
                                contentDescription = stringResource(R.string.confirm_password)
                            )
                        }
                    },
                    label = { Text(stringResource(R.string.confirm_password)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { onRegister() },
                    shape = RoundedCornerShape(16.dp),
                    enabled = isButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.register).uppercase())
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.already_have_account))
                    TextButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Text(text = stringResource(R.string.login))
                    }
                }
            }
        }
    }
}