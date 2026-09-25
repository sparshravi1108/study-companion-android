package com.example.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.domain.model.AiCompanionState
import com.example.presentation.components.AiOrb
import com.example.presentation.components.CyberOutlinedButton
import com.example.presentation.components.InkBackground
import com.example.presentation.components.NeonButton
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val extendedColors = StudyTheme.extendedColors
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    var isSignUpMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("alex@example.com") }
    var password by remember { mutableStateOf("studycompanion123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showEmailForm by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onAuthSuccess()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    InkBackground(
        modifier = modifier.testTag("login_screen"),
        particlePreset = ParticlePreset.SUBTLE
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = StudySpacing.lg, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Anime Hero Avatar Header with double glowing ring
            Box(
                modifier = Modifier
                    .size(115.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        extendedColors.accentGradient,
                        CircleShape
                    )
                    .background(extendedColors.surface),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_avatar),
                    contentDescription = "Hero Anime Student",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // AI Orb Mini Emblem
            AiOrb(
                state = AiCompanionState.FOCUS,
                size = 38.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "STUDY COMPANION",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = "Your AI-Powered Study Partner",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textSecondary,
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Continue with Google Button (Prominent Neon CTA)
            NeonButton(
                text = "Continue with Google",
                onClick = { viewModel.signInWithDemo() },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color(0xFF4285F4),
                                fontWeight = FontWeight.Black
                            )
                        )
                    }
                },
                testTag = "google_sign_in_button"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // "or" Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = extendedColors.border
                )
                Text(
                    text = "  or  ",
                    style = MaterialTheme.typography.bodySmall.copy(color = extendedColors.textTertiary)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = extendedColors.border
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!showEmailForm) {
                // Secondary Sign in with email button
                CyberOutlinedButton(
                    text = "Sign in with email",
                    onClick = { showEmailForm = true },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = extendedColors.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    testTag = "toggle_email_form_button"
                )
            } else {
                // Expanded Email & Password Fields
                AnimatedVisibility(visible = isSignUpMode) {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = extendedColors.primary
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = extendedColors.borderHighlight,
                                unfocusedBorderColor = extendedColors.border,
                                focusedLabelColor = extendedColors.primary,
                                unfocusedLabelColor = extendedColors.textSecondary,
                                focusedTextColor = extendedColors.textPrimary,
                                unfocusedTextColor = extendedColors.textPrimary
                            ),
                            shape = RoundedCornerShape(StudyRadii.medium),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input_field")
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter your email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = extendedColors.primary
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = extendedColors.borderHighlight,
                        unfocusedBorderColor = extendedColors.border,
                        focusedLabelColor = extendedColors.primary,
                        unfocusedLabelColor = extendedColors.textSecondary,
                        focusedTextColor = extendedColors.textPrimary,
                        unfocusedTextColor = extendedColors.textPrimary
                    ),
                    shape = RoundedCornerShape(StudyRadii.medium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("email_input_field")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = extendedColors.primary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = extendedColors.textSecondary
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (isSignUpMode) {
                                viewModel.signUpWithEmail(name, email, password)
                            } else {
                                viewModel.signInWithEmail(email, password)
                            }
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = extendedColors.borderHighlight,
                        unfocusedBorderColor = extendedColors.border,
                        focusedLabelColor = extendedColors.primary,
                        unfocusedLabelColor = extendedColors.textSecondary,
                        focusedTextColor = extendedColors.textPrimary,
                        unfocusedTextColor = extendedColors.textPrimary
                    ),
                    shape = RoundedCornerShape(StudyRadii.medium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input_field")
                )

                Spacer(modifier = Modifier.height(14.dp))

                NeonButton(
                    text = if (isSignUpMode) "Sign Up" else "Sign In",
                    onClick = {
                        focusManager.clearFocus()
                        if (isSignUpMode) {
                            viewModel.signUpWithEmail(name, email, password)
                        } else {
                            viewModel.signInWithEmail(email, password)
                        }
                    },
                    isLoading = uiState.isLoading,
                    testTag = "submit_auth_button"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isSignUpMode) "Already have an account? " else "Don't have an account? ",
                        style = MaterialTheme.typography.bodySmall.copy(color = extendedColors.textSecondary)
                    )
                    Text(
                        text = if (isSignUpMode) "Sign In" else "Sign Up",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = extendedColors.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .testTag("toggle_auth_mode_button")
                            .clickable { isSignUpMode = !isSignUpMode }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Signature Anime Slogan
            Text(
                text = "“Better Study,\nBigger Dreams.”",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textSecondary.copy(alpha = 0.8f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Guest / Offline Demo
            Text(
                text = "⚡ Instant Demo / Offline Access",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textTertiary,
                    fontSize = 11.sp
                ),
                modifier = Modifier
                    .testTag("demo_mode_button")
                    .clickable { viewModel.signInWithDemo() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
