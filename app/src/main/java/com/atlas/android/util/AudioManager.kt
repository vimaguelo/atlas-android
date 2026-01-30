package com.atlas.android.util

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

sealed class AudioState {
    object Idle : AudioState()
    object Playing : AudioState()
    object Completed : AudioState()
    data class Error(val message: String) : AudioState()
}

@Singleton
class AudioManager @Inject constructor(
    private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    
    /**
     * Play audio from a byte array (e.g., TTS response from API)
     */
    fun playAudio(audioData: ByteArray): Flow<AudioState> = callbackFlow {
        try {
            // Save audio to temp file
            val tempFile = File.createTempFile("atlas_tts", ".mp3", context.cacheDir)
            FileOutputStream(tempFile).use { it.write(audioData) }
            
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile.absolutePath)
                setOnPreparedListener {
                    trySend(AudioState.Playing)
                    it.start()
                }
                setOnCompletionListener {
                    trySend(AudioState.Completed)
                    tempFile.delete()
                    close()
                }
                setOnErrorListener { _, what, extra ->
                    trySend(AudioState.Error("MediaPlayer error: $what, $extra"))
                    tempFile.delete()
                    close()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            trySend(AudioState.Error(e.message ?: "Failed to play audio"))
            close()
        }
        
        awaitClose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    
    /**
     * Play audio from a URL
     */
    fun playAudioUrl(url: String): Flow<AudioState> = callbackFlow {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener {
                    trySend(AudioState.Playing)
                    it.start()
                }
                setOnCompletionListener {
                    trySend(AudioState.Completed)
                    close()
                }
                setOnErrorListener { _, what, extra ->
                    trySend(AudioState.Error("MediaPlayer error: $what, $extra"))
                    close()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            trySend(AudioState.Error(e.message ?: "Failed to play audio"))
            close()
        }
        
        awaitClose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
