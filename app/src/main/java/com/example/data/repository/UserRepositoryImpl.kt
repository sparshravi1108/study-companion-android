package com.example.data.repository

import com.example.core.result.Resource
import com.example.data.firebase.FirebaseManager
import com.example.data.firebase.FirestoreCollections
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ConnectedIntegrations
import com.example.domain.model.ThemeMode
import com.example.domain.model.UserProfile
import com.example.domain.model.UserPreferences
import com.example.domain.model.UserStats
import com.example.domain.repository.UserRepository
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    // Cloud-only requirement: in-memory runtime cache for seamless operation
    private val inMemoryProfiles = MutableStateFlow<Map<String, UserProfile>>(
        mapOf(
            "demo_alex_carter_01" to UserProfile(
                uid = "demo_alex_carter_01",
                displayName = "Alex Carter",
                email = "alex@example.com",
                joinedDate = "Apr 2026",
                timezone = "Asia/Kolkata",
                stats = UserStats(
                    level = 1,
                    levelTitle = "Beginner",
                    currentXp = 0,
                    nextLevelXp = 500,
                    streakDays = 0,
                    totalStudyMinutes = 0,
                    focusPercentage = 0
                ),
                preferences = UserPreferences(
                    themeMode = ThemeMode.INK_ANIME,
                    animationLevel = AnimationLevel.MEDIUM,
                    animationIntensity = 1.0f,
                    reduceMotion = false
                ),
                integrations = ConnectedIntegrations()
            )
        )
    )

    override fun observeUserProfile(userId: String): Flow<Resource<UserProfile>> = callbackFlow {
        trySend(Resource.Loading)

        val firestore = FirebaseManager.firestore
        if (firestore == null) {
            val cached = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
            trySend(Resource.Success(cached))
            val job = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).run {
                inMemoryProfiles.collect { map ->
                    val user = map[userId] ?: UserProfile(uid = userId)
                    trySend(Resource.Success(user))
                }
            }
            awaitClose { }
            return@callbackFlow
        }

        val docRef = firestore.collection(FirestoreCollections.USERS).document(userId)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Fallback to in-memory cache on network/permission error
                val fallback = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
                trySend(Resource.Success(fallback))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val profile = mapSnapshotToProfile(userId, snapshot.data ?: emptyMap())
                updateMemoryCache(profile)
                trySend(Resource.Success(profile))
            } else {
                val defaultUser = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
                trySend(Resource.Success(defaultUser))
            }
        }

        awaitClose {
            registration.remove()
        }
    }

    override suspend fun getUserProfile(userId: String): Resource<UserProfile> {
        val firestore = FirebaseManager.firestore
        if (firestore == null) {
            val user = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
            return Resource.Success(user)
        }

        return try {
            val snapshot = firestore.collection(FirestoreCollections.USERS).document(userId).get().await()
            if (snapshot.exists()) {
                val profile = mapSnapshotToProfile(userId, snapshot.data ?: emptyMap())
                updateMemoryCache(profile)
                Resource.Success(profile)
            } else {
                val defaultProfile = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
                Resource.Success(defaultProfile)
            }
        } catch (e: Exception) {
            val fallback = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
            Resource.Success(fallback)
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile): Resource<Unit> {
        updateMemoryCache(profile)
        val firestore = FirebaseManager.firestore ?: return Resource.Success(Unit)

        return try {
            val map = mapProfileToMap(profile)
            firestore.collection(FirestoreCollections.USERS)
                .document(profile.uid)
                .set(map, SetOptions.merge())
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to save profile to Firestore", e)
        }
    }

    override suspend fun updateTheme(userId: String, themeMode: ThemeMode): Resource<Unit> {
        val current = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
        val updated = current.copy(
            preferences = current.preferences.copy(themeMode = themeMode)
        )
        return saveUserProfile(updated)
    }

    override suspend fun updateAppearance(
        userId: String,
        themeMode: ThemeMode,
        animationLevel: AnimationLevel,
        animationIntensity: Float,
        reduceMotion: Boolean
    ): Resource<Unit> {
        val current = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
        val updated = current.copy(
            preferences = current.preferences.copy(
                themeMode = themeMode,
                animationLevel = animationLevel,
                animationIntensity = animationIntensity,
                reduceMotion = reduceMotion
            )
        )
        return saveUserProfile(updated)
    }

    override suspend fun updateStats(userId: String, stats: UserStats): Resource<Unit> {
        val current = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
        val updated = current.copy(stats = stats)
        return saveUserProfile(updated)
    }

    override suspend fun updateIntegrations(
        userId: String,
        integrations: ConnectedIntegrations
    ): Resource<Unit> {
        val current = inMemoryProfiles.value[userId] ?: UserProfile(uid = userId)
        val updated = current.copy(integrations = integrations)
        return saveUserProfile(updated)
    }

    private fun updateMemoryCache(profile: UserProfile) {
        val currentMap = inMemoryProfiles.value.toMutableMap()
        currentMap[profile.uid] = profile
        inMemoryProfiles.value = currentMap
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapSnapshotToProfile(uid: String, data: Map<String, Any>): UserProfile {
        val statsMap = data["stats"] as? Map<String, Any> ?: emptyMap()
        val prefsMap = data["preferences"] as? Map<String, Any> ?: emptyMap()
        val integMap = data["integrations"] as? Map<String, Any> ?: emptyMap()

        val themeStr = prefsMap["themeMode"] as? String ?: ThemeMode.INK_ANIME.name
        val themeMode = try {
            ThemeMode.valueOf(themeStr)
        } catch (e: Exception) {
            ThemeMode.INK_ANIME
        }

        val animLevelStr = prefsMap["animationLevel"] as? String ?: AnimationLevel.MEDIUM.name
        val animationLevel = try {
            AnimationLevel.valueOf(animLevelStr)
        } catch (e: Exception) {
            AnimationLevel.MEDIUM
        }

        return UserProfile(
            uid = uid,
            displayName = data["displayName"] as? String ?: "Alex Carter",
            email = data["email"] as? String ?: "alex@example.com",
            photoUrl = data["photoUrl"] as? String,
            joinedDate = data["joinedDate"] as? String ?: "Apr 2026",
            timezone = data["timezone"] as? String ?: "Asia/Kolkata",
            stats = UserStats(
                level = (statsMap["level"] as? Number)?.toInt() ?: 1,
                levelTitle = statsMap["levelTitle"] as? String ?: "Beginner",
                currentXp = (statsMap["currentXp"] as? Number)?.toInt() ?: 0,
                nextLevelXp = (statsMap["nextLevelXp"] as? Number)?.toInt() ?: 500,
                streakDays = (statsMap["streakDays"] as? Number)?.toInt() ?: 0,
                totalStudyMinutes = (statsMap["totalStudyMinutes"] as? Number)?.toInt() ?: 0,
                focusPercentage = (statsMap["focusPercentage"] as? Number)?.toInt() ?: 0
            ),
            preferences = UserPreferences(
                themeMode = themeMode,
                animationLevel = animationLevel,
                animationIntensity = (prefsMap["animationIntensity"] as? Number)?.toFloat() ?: 1.0f,
                reduceMotion = prefsMap["reduceMotion"] as? Boolean ?: false,
                soundEffectsEnabled = prefsMap["soundEffectsEnabled"] as? Boolean ?: true,
                notificationsEnabled = prefsMap["notificationsEnabled"] as? Boolean ?: true
            ),
            integrations = ConnectedIntegrations(
                googleDriveSync = integMap["googleDriveSync"] as? Boolean ?: true,
                googleCalendarSync = integMap["googleCalendarSync"] as? Boolean ?: true,
                googleTasksSync = integMap["googleTasksSync"] as? Boolean ?: true,
                googleKeepSync = integMap["googleKeepSync"] as? Boolean ?: true,
                gmailSync = integMap["gmailSync"] as? Boolean ?: true
            )
        )
    }

    private fun mapProfileToMap(profile: UserProfile): Map<String, Any> {
        return mapOf(
            "uid" to profile.uid,
            "displayName" to profile.displayName,
            "email" to profile.email,
            "photoUrl" to (profile.photoUrl ?: ""),
            "joinedDate" to profile.joinedDate,
            "timezone" to profile.timezone,
            "stats" to mapOf(
                "level" to profile.stats.level,
                "levelTitle" to profile.stats.levelTitle,
                "currentXp" to profile.stats.currentXp,
                "nextLevelXp" to profile.stats.nextLevelXp,
                "streakDays" to profile.stats.streakDays,
                "totalStudyMinutes" to profile.stats.totalStudyMinutes,
                "focusPercentage" to profile.stats.focusPercentage
            ),
            "preferences" to mapOf(
                "themeMode" to profile.preferences.themeMode.name,
                "animationLevel" to profile.preferences.animationLevel.name,
                "animationIntensity" to profile.preferences.animationIntensity,
                "reduceMotion" to profile.preferences.reduceMotion,
                "soundEffectsEnabled" to profile.preferences.soundEffectsEnabled,
                "notificationsEnabled" to profile.preferences.notificationsEnabled
            ),
            "integrations" to mapOf(
                "googleDriveSync" to profile.integrations.googleDriveSync,
                "googleCalendarSync" to profile.integrations.googleCalendarSync,
                "googleTasksSync" to profile.integrations.googleTasksSync,
                "googleKeepSync" to profile.integrations.googleKeepSync,
                "gmailSync" to profile.integrations.gmailSync
            )
        )
    }
}
