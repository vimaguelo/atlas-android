package com.atlas.android.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.android.data.repository.MessageRepository
import com.atlas.android.domain.model.AtlasState
import com.atlas.android.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()
    
    init {
        loadStatus()
    }
    
    fun onMessageChange(message: String) {
        _state.update { it.copy(currentMessage = message) }
    }
    
    fun sendMessage() {
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
                            atlasState = AtlasState.IDLE
                        )
                    }
                    loadStatus()
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
