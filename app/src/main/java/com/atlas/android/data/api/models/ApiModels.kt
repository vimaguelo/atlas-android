package com.atlas.android.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class MessageResponse(
    @Json(name = "reply") val reply: String,
    @Json(name = "model") val model: String? = null,
    @Json(name = "tokensUsed") val tokensUsed: Int? = null
)

@JsonClass(generateAdapter = true)
data class SessionInfo(
    @Json(name = "sessionKey") val sessionKey: String,
    @Json(name = "agentId") val agentId: String,
    @Json(name = "model") val model: String
)

@JsonClass(generateAdapter = true)
data class SessionsResponse(
    @Json(name = "sessions") val sessions: List<SessionInfo>
)

@JsonClass(generateAdapter = true)
data class StatusResponse(
    @Json(name = "model") val model: String,
    @Json(name = "tokensUsed") val tokensUsed: Int,
    @Json(name = "sessionKey") val sessionKey: String
)
