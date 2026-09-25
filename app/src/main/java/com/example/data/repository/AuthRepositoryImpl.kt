package com.example.data.repository

import com.example.core.result.Resource
import com.example.data.firebase.FirebaseManager
import com.example.domain.model.UserProfile
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {

    // In-memory fallback/demo user state for seamless offline & unconfigured run
    private val _demoUserState = MutableStateFlow<UserProfile?>(
        UserProfile(
            uid = "demo_alex_carter_01",
            displayName = "Alex Carter",
            email = "alex@example.com"
        )
    )

    override val currentUserState: Flow<UserProfile?> = callbackFlow {
        val auth = FirebaseManager.auth
        if (auth == null) {
            // Provide in-memory demo state if Firebase is not yet provisioned
            val job = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).run {
                _demoUserState.collect { user ->
                    trySend(user)
                }
            }
            awaitClose { }
            return@callbackFlow
        }

        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { firebaseAuth ->
            val fbUser = firebaseAuth.currentUser
            if (fbUser != null) {
                trySend(
                    UserProfile(
                        uid = fbUser.uid,
                        displayName = fbUser.displayName ?: "Alex Carter",
                        email = fbUser.email ?: "alex@example.com",
                        photoUrl = fbUser.photoUrl?.toString()
                    )
                )
            } else {
                trySend(_demoUserState.value)
            }
        }

        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override fun getCurrentUser(): UserProfile? {
        val fbUser = FirebaseManager.auth?.currentUser
        return if (fbUser != null) {
            UserProfile(
                uid = fbUser.uid,
                displayName = fbUser.displayName ?: "Alex Carter",
                email = fbUser.email ?: "alex@example.com",
                photoUrl = fbUser.photoUrl?.toString()
            )
        } else {
            _demoUserState.value
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Resource<UserProfile> {
        return try {
            val auth = FirebaseManager.auth
            if (auth == null) {
                // In-memory mode
                val user = UserProfile(
                    uid = "demo_user_${email.hashCode()}",
                    displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email
                )
                _demoUserState.value = user
                return Resource.Success(user)
            }

            val result = auth.signInWithEmailAndPassword(email, password).await()
            val fbUser = result.user ?: return Resource.Error("User not found after sign in")
            val profile = UserProfile(
                uid = fbUser.uid,
                displayName = fbUser.displayName ?: email.substringBefore("@"),
                email = fbUser.email ?: email,
                photoUrl = fbUser.photoUrl?.toString()
            )
            _demoUserState.value = profile
            Resource.Success(profile)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Sign-in failed", e)
        }
    }

    override suspend fun signUpWithEmail(
        name: String,
        email: String,
        password: String
    ): Resource<UserProfile> {
        return try {
            val auth = FirebaseManager.auth
            if (auth == null) {
                val user = UserProfile(
                    uid = "demo_user_${email.hashCode()}",
                    displayName = name.ifBlank { "Alex Carter" },
                    email = email
                )
                _demoUserState.value = user
                return Resource.Success(user)
            }

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val fbUser = result.user ?: return Resource.Error("User creation failed")
            
            // Set display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            fbUser.updateProfile(profileUpdates).await()

            val profile = UserProfile(
                uid = fbUser.uid,
                displayName = name,
                email = fbUser.email ?: email,
                photoUrl = fbUser.photoUrl?.toString()
            )
            _demoUserState.value = profile
            Resource.Success(profile)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Account creation failed", e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Resource<UserProfile> {
        return try {
            val auth = FirebaseManager.auth
            if (auth == null) {
                val user = UserProfile(
                    uid = "google_demo_${System.currentTimeMillis()}",
                    displayName = "Alex Carter",
                    email = "alex@example.com"
                )
                _demoUserState.value = user
                return Resource.Success(user)
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val fbUser = result.user ?: return Resource.Error("Google sign in failed")
            val profile = UserProfile(
                uid = fbUser.uid,
                displayName = fbUser.displayName ?: "Alex Carter",
                email = fbUser.email ?: "alex@example.com",
                photoUrl = fbUser.photoUrl?.toString()
            )
            _demoUserState.value = profile
            Resource.Success(profile)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Google sign-in failed", e)
        }
    }

    override suspend fun signInDemoUser(): Resource<UserProfile> {
        val demoUser = UserProfile(
            uid = "demo_alex_carter_01",
            displayName = "Alex Carter",
            email = "alex@example.com"
        )
        _demoUserState.value = demoUser
        return Resource.Success(demoUser)
    }

    override suspend fun sendPasswordReset(email: String): Resource<Unit> {
        return try {
            val auth = FirebaseManager.auth
            auth?.sendPasswordResetEmail(email)?.await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to send reset email", e)
        }
    }

    override suspend fun signOut(): Resource<Unit> {
        return try {
            FirebaseManager.auth?.signOut()
            _demoUserState.value = null
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to sign out", e)
        }
    }
}
