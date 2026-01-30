package com.atlas.android.di

import com.atlas.android.data.api.OpenClawApi
import com.atlas.android.data.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideMessageRepository(api: OpenClawApi): MessageRepository {
        return MessageRepository(api)
    }
}
