package com.atlas.android.data.api

import com.atlas.android.data.api.models.MessageResponse
import com.atlas.android.data.api.models.SendMessageRequest
import com.atlas.android.data.api.models.SessionsResponse
import com.atlas.android.data.api.models.StatusResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OpenClawApi {
    
    @POST("/api/sessions/{key}/messages")
    suspend fun sendMessage(
        @Path("key") sessionKey: String,
        @Body request: SendMessageRequest
    ): MessageResponse
    
    @GET("/api/sessions")
    suspend fun getSessions(): SessionsResponse
    
    @GET("/api/status")
    suspend fun getStatus(): StatusResponse
    
    @POST("/api/tts")
    suspend fun textToSpeech(
        @Body request: Map<String, String>
    ): ResponseBody
}
