package com.atlas.android.di

import android.content.Context
import com.atlas.android.data.api.OpenClawApi
import com.atlas.android.data.repository.MessageRepository
import com.atlas.android.util.AudioManager
import com.atlas.android.util.VoiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    
    @Provides
    @Singleton
    fun provideVoiceManager(@ApplicationContext context: Context): VoiceManager {
        return VoiceManager(context)
    }
    
    @Provides
    @Singleton
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager {
        return AudioManager(context)
    }
}
