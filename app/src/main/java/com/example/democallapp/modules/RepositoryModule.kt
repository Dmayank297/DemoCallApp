package com.example.democallapp.modules

import com.example.democallapp.domain.repos.CallLogRepository
import com.example.democallapp.domain.repos.CallStateRepository
import com.example.democallapp.domain.repos.ContactRepository
import com.example.democallapp.domain.repos.impl.CallLogRepositoryImpl
import com.example.democallapp.domain.repos.impl.CallStateRepositoryImpl
import com.example.democallapp.domain.repos.impl.ContactRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository

    @Binds
    @Singleton
    abstract fun bindCallLogRepository(impl: CallLogRepositoryImpl): CallLogRepository

    @Binds
    @Singleton
    abstract fun bindCallStateRepository(impl: CallStateRepositoryImpl): CallStateRepository
}