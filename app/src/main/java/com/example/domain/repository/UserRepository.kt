package com.example.domain.repository

import com.example.core.result.Resource
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ConnectedIntegrations
import com.example.domain.model.ThemeMode
import com.example.domain.model.UserProfile
import com.example.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUserProfile(userId: String): Flow<Resource<UserProfile>>
    suspend fun getUserProfile(userId: String): Resource<UserProfile>
    suspend fun saveUserProfile(profile: UserProfile): Resource<Unit>
    suspend fun updateTheme(userId: String, themeMode: ThemeMode): Resource<Unit>
    suspend fun updateAppearance(
        userId: String,
        themeMode: ThemeMode,
        animationLevel: AnimationLevel,
        animationIntensity: Float,
        reduceMotion: Boolean
    ): Resource<Unit>
    suspend fun updateStats(userId: String, stats: UserStats): Resource<Unit>
    suspend fun updateIntegrations(userId: String, integrations: ConnectedIntegrations): Resource<Unit>
}
