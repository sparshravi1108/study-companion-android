package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.result.Resource
import com.example.domain.model.ThemeMode
import com.example.domain.model.UserProfile
import com.example.navigation.StudyNavHost
import com.example.presentation.auth.AuthViewModel
import com.example.ui.theme.StudyCompanionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as StudyCompanionApp).container

        setContent {
            val authViewModel: AuthViewModel = viewModel {
                AuthViewModel(appContainer.authRepository)
            }

            val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()

            val userId = currentUser?.uid ?: "demo_alex_carter_01"

            val profileResource by appContainer.userRepository
                .observeUserProfile(userId)
                .collectAsStateWithLifecycle(initialValue = Resource.Success(UserProfile(uid = userId)))

            val userProfile = (profileResource as? Resource.Success)?.data ?: UserProfile(uid = userId)

            var activeTheme by remember(userProfile.preferences.themeMode) {
                mutableStateOf(userProfile.preferences.themeMode)
            }
            var activeLevel by remember(userProfile.preferences.animationLevel) {
                mutableStateOf(userProfile.preferences.animationLevel)
            }
            var activeIntensity by remember(userProfile.preferences.animationIntensity) {
                mutableFloatStateOf(userProfile.preferences.animationIntensity)
            }
            var activeReduceMotion by remember(userProfile.preferences.reduceMotion) {
                mutableStateOf(userProfile.preferences.reduceMotion)
            }

            StudyCompanionTheme(
                themeMode = activeTheme,
                animationLevel = activeLevel,
                animationIntensity = activeIntensity,
                reduceMotion = activeReduceMotion
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudyNavHost(
                        authViewModel = authViewModel,
                        userRepository = appContainer.userRepository,
                        networkMonitor = appContainer.networkMonitor,
                        currentUserProfile = userProfile.copy(
                            displayName = currentUser?.displayName ?: userProfile.displayName,
                            email = currentUser?.email ?: userProfile.email,
                            preferences = userProfile.preferences.copy(
                                themeMode = activeTheme,
                                animationLevel = activeLevel,
                                animationIntensity = activeIntensity,
                                reduceMotion = activeReduceMotion
                            )
                        ),
                        onAppearanceChanged = { newTheme, newLevel, newIntensity, newReduceMotion ->
                            activeTheme = newTheme
                            activeLevel = newLevel
                            activeIntensity = newIntensity
                            activeReduceMotion = newReduceMotion
                            scope.launch {
                                appContainer.userRepository.updateAppearance(
                                    userId,
                                    newTheme,
                                    newLevel,
                                    newIntensity,
                                    newReduceMotion
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
