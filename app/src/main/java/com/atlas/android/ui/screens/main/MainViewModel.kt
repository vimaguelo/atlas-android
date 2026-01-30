package com.atlas.android.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.android.data.repository.MessageRepository
import com.atlas.android.domain.model.AtlasState
import com.atlas.android.domain.model.Message
import com.atlas.android.util.AudioManager
import com.atlas.android.util.AudioState
import com.atlas.android.util.VoiceManager
import com.atlas.android.util.VoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val voiceManager: VoiceManager,
    private val audioManager: AudioManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()
    
    init {
        loadStatus()
    }
    
    fun onMessageChange(message: String) {
        _state.update { it.copy(currentMessage = message) }
    }
    
    fun sendMessage(withVoice: Boolean = false) {
        val message = _state.value.currentMessage.trim()
        if (message.isEmpty()) return
        
        viewModelScope.launch {
            // Add user message
            val userMessage = Message(
                content = message,
                isFromUser = true
            )
            
            _state.update { 
                it.copy(
                    messages = it.messages + userMessage,
                    currentMessage = "",
                    isLoading = true,
                    atlasState = AtlasState.THINKING,
                    error = null
                )
            }
            
            // Send to API
            messageRepository.sendMessage(message)
                .onSuccess { response ->
                    _state.update { 
                        it.copy(
                            messages = it.messages + response,
                            isLoading = false,
                            atlasState = if (withVoice) AtlasState.SPEAKING else AtlasState.IDLE
                        )
                    }
                    
                    // Play voice response if requested
                    if (withVoice) {
                        playVoiceResponse(response.content)
                    } else {
                        loadStatus()
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            atlasState = AtlasState.ERROR,
                            error = error.message ?: "Unknown error"
                        )
                    }
                }
        }
    }
    
    fun startVoiceInput() {
        if (!voiceManager.isAvailable()) {
            _state.update { 
                it.copy(
                    error = "Voice input not available",
                    atlasState = AtlasState.ERROR
                )
            }
            return
        }
        
        viewModelScope.launch {
            voiceManager.startListening().collect { voiceState ->
                when (voiceState) {
                    is VoiceState.Idle -> {
                        _state.update { it.copy(atlasState = AtlasState.IDLE) }
                    }
                    is VoiceState.Listening -> {
                        _state.update { it.copy(atlasState = AtlasState.LISTENING) }
                    }
                    is VoiceState.Result -> {
                        _state.update { it.copy(currentMessage = voiceState.text) }
                        sendMessage(withVoice = true)
                    }
                    is VoiceState.Error -> {
                        _state.update { 
                            it.copy(
                                error = voiceState.message,
                                atlasState = AtlasState.ERROR
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun stopVoice() {
        voiceManager.stopListening()
        audioManager.stop()
        _state.update { it.copy(atlasState = AtlasState.IDLE) }
    }
    
    private fun playVoiceResponse(text: String) {
        viewModelScope.launch {
            messageRepository.getAudioForText(text)
                .onSuccess { audioData ->
                    audioManager.playAudio(audioData).collect { audioState ->
                        when (audioState) {
                            is AudioState.Playing -> {
                                _state.update { it.copy(atlasState = AtlasState.SPEAKING) }
                            }
                            is AudioState.Completed -> {
                                _state.update { it.copy(atlasState = AtlasState.IDLE) }
                                loadStatus()
                            }
                            is AudioState.Error -> {
                                _state.update { 
                                    it.copy(
                                        error = audioState.message,
                                        atlasState = AtlasState.IDLE
                                    )
                                }
                                loadStatus()
                            }
                            else -> {}
                        }
                    }
                }
                .onFailure { error ->
                    // Fallback to text-only if TTS fails
                    _state.update { it.copy(atlasState = AtlasState.IDLE) }
                    loadStatus()
                }
        }
    }
    
    fun loadStatus() {
        viewModelScope.launch {
            messageRepository.getStatus()
                .onSuccess { status ->
                    _state.update { it.copy(statusInfo = status) }
                }
                .onFailure { error ->
                    // Silently fail status updates
                }
        }
    }
    
    fun clearError() {
        _state.update { it.copy(error = null, atlasState = AtlasState.IDLE) }
    }
}
