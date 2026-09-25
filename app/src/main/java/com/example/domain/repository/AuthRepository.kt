package com.example.domain.repository

import com.example.core.result.Resource
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserState: Flow<UserProfile?>
    fun getCurrentUser(): UserProfile?
    suspend fun signInWithEmail(email: String, password: String): Resource<UserProfile>
    suspend fun signUpWithEmail(name: String, email: String, password: String): Resource<UserProfile>
    suspend fun signInWithGoogle(idToken: String): Resource<UserProfile>
    suspend fun signInDemoUser(): Resource<UserProfile>
    suspend fun sendPasswordReset(email: String): Resource<Unit>
    suspend fun signOut(): Resource<Unit>
}
