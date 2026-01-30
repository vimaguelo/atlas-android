package com.atlas.android.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.atlas.android.domain.model.AtlasState
import com.atlas.android.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AtlasAvatar(
    state: AtlasState,
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            AtlasState.IDLE -> IdleAvatar(size)
            AtlasState.LISTENING -> ListeningAvatar(size)
            AtlasState.THINKING -> ThinkingAvatar(size)
            AtlasState.SPEAKING -> SpeakingAvatar(size)
            AtlasState.ERROR -> ErrorAvatar(size)
        }
    }
}

@Composable
private fun IdleAvatar(size: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "idle")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idle-scale"
    )
    
    Canvas(modifier = Modifier.size(size.dp)) {
        val radius = size.dp.toPx() / 2 * scale
        drawCircle(
            color = AvatarIdle,
            radius = radius,
            center = center,
            alpha = 0.3f
        )
        drawCircle(
            color = AvatarIdle,
            radius = radius * 0.7f,
            center = center
        )
    }
}

@Composable
private fun ListeningAvatar(size: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "listening")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "listening-scale"
    )
    
    val ringProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "listening-ring"
    )
    
    Canvas(modifier = Modifier.size(size.dp)) {
        // Expanding rings
        for (i in 0..2) {
            val ringScale = (ringProgress + i * 0.33f) % 1f
            drawCircle(
                color = AvatarListening,
                radius = size.dp.toPx() / 2 * ringScale,
                center = center,
                alpha = 1f - ringScale
            )
        }
        
        // Core pulsing circle
        drawCircle(
            color = AvatarListening,
            radius = size.dp.toPx() / 3 * scale,
            center = center
        )
    }
}

@Composable
private fun ThinkingAvatar(size: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "thinking")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "thinking-rotation"
    )
    
    Canvas(modifier = Modifier.size(size.dp)) {
        val radius = size.dp.toPx() / 2
        
        // Central circle
        drawCircle(
            color = AvatarThinking,
            radius = radius * 0.5f,
            center = center,
            alpha = 0.5f
        )
        
        // Rotating arcs/dots
        for (i in 0..5) {
            val angle = (rotation + i * 60f) * (Math.PI / 180f).toFloat()
            val x = center.x + cos(angle) * radius * 0.7f
            val y = center.y + sin(angle) * radius * 0.7f
            
            drawCircle(
                color = AvatarThinking,
                radius = radius * 0.15f,
                center = Offset(x, y),
                alpha = 0.8f
            )
        }
    }
}

@Composable
private fun SpeakingAvatar(size: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "speaking")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speaking-scale"
    )
    
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speaking-alpha"
    )
    
    Canvas(modifier = Modifier.size(size.dp)) {
        val radius = size.dp.toPx() / 2
        
        // Outer pulse
        drawCircle(
            color = AvatarSpeaking,
            radius = radius * scale,
            center = center,
            alpha = pulseAlpha
        )
        
        // Core circle
        drawCircle(
            color = AvatarSpeaking,
            radius = radius * 0.6f,
            center = center
        )
    }
}

@Composable
private fun ErrorAvatar(size: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "error")
    val shake by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "error-shake"
    )
    
    Canvas(modifier = Modifier.size(size.dp)) {
        drawCircle(
            color = AvatarError,
            radius = size.dp.toPx() / 2,
            center = Offset(center.x + shake, center.y),
            alpha = 0.8f
        )
    }
}
