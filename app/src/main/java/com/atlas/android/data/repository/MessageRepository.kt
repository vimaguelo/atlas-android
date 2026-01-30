package com.atlas.android.data.repository

import com.atlas.android.data.api.OpenClawApi
import com.atlas.android.data.api.models.SendMessageRequest
import com.atlas.android.domain.model.Message
import com.atlas.android.domain.model.StatusInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val api: OpenClawApi
) {
    // TODO: Make session key configurable
    private val defaultSessionKey = "main"
    
    suspend fun sendMessage(message: String): Result<Message> {
        return try {
            val response = api.sendMessage(
                sessionKey = defaultSessionKey,
                request = SendMessageRequest(message)
            )
            Result.success(
                Message(
                    content = response.reply,
                    isFromUser = false
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getStatus(): Result<StatusInfo> {
        return try {
            val response = api.getStatus()
            Result.success(
                StatusInfo(
                    model = response.model,
                    tokensUsed = response.tokensUsed,
                    sessionKey = response.sessionKey
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAudioForText(text: String): Result<ByteArray> {
        return try {
            val response = api.textToSpeech(mapOf("text" to text))
            Result.success(response.bytes())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
