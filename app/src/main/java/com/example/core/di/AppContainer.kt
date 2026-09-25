package com.example.core.di

import android.content.Context
import com.example.core.network.LiveNetworkMonitor
import com.example.core.network.NetworkMonitor
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserRepository

class AppContainer(context: Context) {
    val networkMonitor: NetworkMonitor = LiveNetworkMonitor(context)
    val authRepository: AuthRepository = AuthRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()
}
