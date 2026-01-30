package com.atlas.android.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

sealed class VoiceState {
    object Idle : VoiceState()
    object Listening : VoiceState()
    data class Result(val text: String) : VoiceState()
    data class Error(val message: String) : VoiceState()
}

@Singleton
class VoiceManager @Inject constructor(
    private val context: Context
) {
    private var speechRecognizer: SpeechRecognizer? = null
    
    fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }
    
    fun startListening(): Flow<VoiceState> = callbackFlow {
        if (!isAvailable()) {
            trySend(VoiceState.Error("Speech recognition not available"))
            close()
            return@callbackFlow
        }
        
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    trySend(VoiceState.Listening)
                }
                
                override fun onBeginningOfSpeech() {}
                
                override fun onRmsChanged(rmsdB: Float) {}
                
                override fun onBufferReceived(buffer: ByteArray?) {}
                
                override fun onEndOfSpeech() {}
                
                override fun onError(error: Int) {
                    val message = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission denied"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                        SpeechRecognizer.ERROR_SERVER -> "Server error"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                        else -> "Unknown error"
                    }
                    trySend(VoiceState.Error(message))
                    close()
                }
                
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull() ?: ""
                    trySend(VoiceState.Result(text))
                    close()
                }
                
                override fun onPartialResults(partialResults: Bundle?) {}
                
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        
        speechRecognizer?.startListening(intent)
        
        awaitClose {
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }
    
    fun stopListening() {
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}
