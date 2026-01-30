package com.atlas.android.ui.screens.main

import com.atlas.android.domain.model.AtlasState
import com.atlas.android.domain.model.Message
import com.atlas.android.domain.model.StatusInfo

data class MainState(
    val messages: List<Message> = emptyList(),
    val currentMessage: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val atlasState: AtlasState = AtlasState.IDLE,
    val statusInfo: StatusInfo? = null
)
