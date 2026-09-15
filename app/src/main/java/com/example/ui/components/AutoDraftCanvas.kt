package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import kotlin.math.*

@Composable
fun AutoDraftPanelVisual(
    panel: StoryboardPanel,
    character: CharacterProfile?,
    modifier: Modifier = Modifier,
    isAnimated: Boolean = false,
    zoomScale: Float = 1.0f,
    panOffset: Offset = Offset.Zero,
    showDialogueBubble: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "panelMotion")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val auraAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "aura"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF090D14))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            if (width <= 0 || height <= 0) return@Canvas

            // Apply camera motion transform (Pan and Zoom)
            val currentScale = if (isAnimated) zoomScale * pulseAnim else 1.0f
            val currentPanX = if (isAnimated) panOffset.x * width * 0.05f else 0f
            val currentPanY = if (isAnimated) panOffset.y * height * 0.05f else 0f

            drawContext.canvas.save()
            drawContext.canvas.translate(currentPanX, currentPanY)
            drawContext.canvas.scale(currentScale, currentScale, width / 2f, height / 2f)

            // 1. Draw Environmental Background
            drawAtmosphere(panel.artStyle, panel.mood, panel.seed, width, height, auraAnim)

            // 2. Draw Camera Angle Geometry & Environment Scenery
            drawScenery(panel.artStyle, panel.cameraAngle, panel.seed, width, height)

            // 3. Draw Character if present
            if (character != null || panel.characterId != null) {
                drawCharacterFigure(
                    character = character,
                    cameraAngle = panel.cameraAngle,
                    artStyle = panel.artStyle,
                    width = width,
                    height = height,
                    pulse = if (isAnimated) pulseAnim else 1.0f
                )
            } else {
                // Draw environmental centerpiece (e.g. mystical temple / star gate / horizon)
                drawCenterpiece(panel.artStyle, panel.mood, width, height, pulseAnim)
            }

            // 4. Draw Style Effects (Action lines, Speed streaks, Glows)
            drawStyleEffects(panel.artStyle, panel.mood, panel.cameraAngle, width, height)

            drawContext.canvas.restore()

            // 5. Cinematic Vignette
            drawVignette(width, height)
        }

        // Overlay: Sound Effect Badge (Comic SFX)
        if (panel.soundEffect.isNotBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFFFB703))
                    .border(1.5.dp, Color.Black, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = panel.soundEffect,
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        // Overlay: Camera Angle Pill
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xCC111827))
                .border(0.5.dp, Color(0xFF374151), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "${panel.cameraAngle.label} • ${panel.artStyle.label.take(12)}",
                color = Color(0xFFE5E7EB),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Overlay: Dialogue Speech Bubble
        if (showDialogueBubble && panel.dialogue.isNotBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xE60F172A))
                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column {
                    if (panel.speakerName.isNotBlank()) {
                        Text(
                            text = panel.speakerName.uppercase(),
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                    Text(
                        text = panel.dialogue,
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawAtmosphere(
    artStyle: ArtStyle,
    mood: StoryMood,
    seed: Long,
    width: Float,
    height: Float,
    auraDeg: Float
) {
    val (topColor, midColor, botColor) = when (artStyle) {
        ArtStyle.CYBERPUNK -> Triple(
            Color(0xFF050510),
            Color(0xFF1E0836),
            Color(0xFF0D1B2A)
        )
        ArtStyle.INDIAN_MYTHOLOGY -> Triple(
            Color(0xFF2C0B0E),
            Color(0xFF5A189A),
            Color(0xFFE85D04)
        )
        ArtStyle.ANIME_2D -> when (mood) {
            StoryMood.PEACEFUL -> Triple(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
            StoryMood.MYSTERIOUS -> Triple(Color(0xFF140024), Color(0xFF240046), Color(0xFF3C096C))
            StoryMood.ACTION -> Triple(Color(0xFF370617), Color(0xFF6A040F), Color(0xFF9D0208))
            else -> Triple(Color(0xFF0A1128), Color(0xFF1C2541), Color(0xFF3A86FF))
        }
        ArtStyle.PIXAR_3D -> Triple(
            Color(0xFF1E3C72),
            Color(0xFF2A5298),
            Color(0xFF48CAE4)
        )
        ArtStyle.COMIC_BOOK -> Triple(
            Color(0xFF18181B),
            Color(0xFF27272A),
            Color(0xFF3F3F46)
        )
        ArtStyle.CINEMATIC_REALISTIC -> Triple(
            Color(0xFF0B0F19),
            Color(0xFF1F2937),
            Color(0xFF111827)
        )
    }

    // Sky gradient
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(topColor, midColor, botColor)
        ),
        size = Size(width, height)
    )

    // Celestial celestial body (Moon/Sun/Stargate)
    val sunX = width * (0.3f + (seed % 40) / 100f)
    val sunY = height * (0.25f + (seed % 20) / 100f)
    val sunRadius = min(width, height) * 0.18f

    val celestialColor = when (artStyle) {
        ArtStyle.CYBERPUNK -> Color(0xFFFF007F)
        ArtStyle.INDIAN_MYTHOLOGY -> Color(0xFFFFD166)
        ArtStyle.ANIME_2D -> Color(0xFFE0E7FF)
        else -> Color(0xFFFFF3B0)
    }

    // Glowing aura
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(celestialColor.copy(alpha = 0.5f), Color.Transparent),
            center = Offset(sunX, sunY),
            radius = sunRadius * 2.5f
        ),
        radius = sunRadius * 2.5f,
        center = Offset(sunX, sunY)
    )

    drawCircle(
        color = celestialColor.copy(alpha = 0.85f),
        radius = sunRadius,
        center = Offset(sunX, sunY)
    )
}

private fun DrawScope.drawScenery(
    artStyle: ArtStyle,
    cameraAngle: CameraAngle,
    seed: Long,
    width: Float,
    height: Float
) {
    // Distant mountain or city silhouettes
    val path = Path()
    val baseHorizon = when (cameraAngle) {
        CameraAngle.LOW_ANGLE -> height * 0.75f
        CameraAngle.HIGH_ANGLE -> height * 0.40f
        else -> height * 0.60f
    }

    path.moveTo(0f, height)
    path.lineTo(0f, baseHorizon)

    val segments = 8
    val segmentWidth = width / segments
    for (i in 1..segments) {
        val peakVariation = ((seed * (i + 3)) % 45).toFloat()
        val py = baseHorizon - peakVariation * (if (i % 2 == 0) 1.2f else 0.6f)
        path.lineTo(i * segmentWidth, py)
    }
    path.lineTo(width, height)
    path.close()

    val mountainColor = when (artStyle) {
        ArtStyle.CYBERPUNK -> Color(0xFF0F0B1E)
        ArtStyle.INDIAN_MYTHOLOGY -> Color(0xFF1E0E18)
        else -> Color(0xFF0D1520)
    }

    drawPath(
        path = path,
        color = mountainColor
    )

    // Foreground terrain / ground
    val groundPath = Path()
    val groundY = height * 0.80f
    groundPath.moveTo(0f, height)
    groundPath.lineTo(0f, groundY)
    groundPath.quadraticTo(width * 0.5f, groundY - 15f, width, groundY + 10f)
    groundPath.lineTo(width, height)
    groundPath.close()

    drawPath(
        path = groundPath,
        color = Color(0xFF06090E)
    )
}

private fun DrawScope.drawCenterpiece(
    artStyle: ArtStyle,
    mood: StoryMood,
    width: Float,
    height: Float,
    pulse: Float
) {
    val centerX = width * 0.5f
    val centerY = height * 0.65f

    // Ancient Gateway / Cosmic Lotus / Portal
    val portalColor = when (mood) {
        StoryMood.ACTION -> Color(0xFFFF0055)
        StoryMood.PEACEFUL -> Color(0xFF00F5D4)
        StoryMood.MYSTERIOUS -> Color(0xFF7B2CBF)
        else -> Color(0xFFFFB703)
    }

    // Portal archway
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(portalColor.copy(alpha = 0.7f), Color.Transparent),
            center = Offset(centerX, centerY),
            radius = min(width, height) * 0.25f * pulse
        ),
        radius = min(width, height) * 0.25f * pulse,
        center = Offset(centerX, centerY)
    )

    // Vertical light beam
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, portalColor.copy(alpha = 0.5f), Color.White)
        ),
        topLeft = Offset(centerX - 8f, 0f),
        size = Size(16f, centerY)
    )
}

private fun DrawScope.drawCharacterFigure(
    character: CharacterProfile?,
    cameraAngle: CameraAngle,
    artStyle: ArtStyle,
    width: Float,
    height: Float,
    pulse: Float
) {
    val charColor = Color(character?.accentColorHex ?: 0xFF00E5FF)
    val hairColor = when {
        character?.hairDescription?.contains("black", ignoreCase = true) == true -> Color(0xFF1E293B)
        character?.hairDescription?.contains("silver", ignoreCase = true) == true -> Color(0xFFCBD5E1)
        else -> Color(0xFF334155)
    }

    val isCloseUp = cameraAngle == CameraAngle.CLOSE_UP
    val isLowAngle = cameraAngle == CameraAngle.LOW_ANGLE

    val charCenterX = when (cameraAngle) {
        CameraAngle.OVER_SHOULDER -> width * 0.7f
        else -> width * 0.5f
    }

    val charBaseY = if (isCloseUp) height * 0.95f else height * 0.85f
    val scaleFactor = if (isCloseUp) 1.8f else if (isLowAngle) 1.3f else 1.0f

    val headRadius = min(width, height) * 0.08f * scaleFactor
    val headY = charBaseY - headRadius * 3.5f

    // Character Aura Glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(charColor.copy(alpha = 0.4f), Color.Transparent),
            center = Offset(charCenterX, headY + headRadius),
            radius = headRadius * 3f
        ),
        radius = headRadius * 3f,
        center = Offset(charCenterX, headY + headRadius)
    )

    // Torso / Jacket
    val torsoPath = Path().apply {
        moveTo(charCenterX - headRadius * 1.4f, headY + headRadius * 0.9f)
        lineTo(charCenterX + headRadius * 1.4f, headY + headRadius * 0.9f)
        lineTo(charCenterX + headRadius * 1.8f, charBaseY)
        lineTo(charCenterX - headRadius * 1.8f, charBaseY)
        close()
    }
    drawPath(torsoPath, color = charColor)

    // Neck
    drawRect(
        color = Color(0xFFFFD1BA),
        topLeft = Offset(charCenterX - headRadius * 0.4f, headY + headRadius * 0.7f),
        size = Size(headRadius * 0.8f, headRadius * 0.6f)
    )

    // Face / Head
    drawCircle(
        color = Color(0xFFFFD1BA),
        radius = headRadius,
        center = Offset(charCenterX, headY)
    )

    // Hair
    val hairPath = Path().apply {
        moveTo(charCenterX - headRadius * 1.15f, headY + headRadius * 0.1f)
        quadraticTo(charCenterX - headRadius * 1.3f, headY - headRadius * 1.4f, charCenterX, headY - headRadius * 1.3f)
        quadraticTo(charCenterX + headRadius * 1.3f, headY - headRadius * 1.4f, charCenterX + headRadius * 1.15f, headY + headRadius * 0.1f)
        lineTo(charCenterX + headRadius * 0.9f, headY - headRadius * 0.3f)
        lineTo(charCenterX, headY - headRadius * 0.5f)
        lineTo(charCenterX - headRadius * 0.9f, headY - headRadius * 0.3f)
        close()
    }
    drawPath(hairPath, color = hairColor)

    // Glowing Talisman / Amulet / Staff
    val talismanY = headY + headRadius * 1.8f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0xFF00E5FF)),
            center = Offset(charCenterX, talismanY),
            radius = headRadius * 0.4f
        ),
        radius = headRadius * 0.35f,
        center = Offset(charCenterX, talismanY)
    )
}

private fun DrawScope.drawStyleEffects(
    artStyle: ArtStyle,
    mood: StoryMood,
    cameraAngle: CameraAngle,
    width: Float,
    height: Float
) {
    if (artStyle == ArtStyle.COMIC_BOOK || mood == StoryMood.ACTION) {
        // Dynamic Anime / Comic speed action lines
        val stroke = Stroke(width = 1.5f)
        val center = Offset(width * 0.5f, height * 0.5f)
        val lineCount = 12
        for (i in 0 until lineCount) {
            val angle = (i * (360f / lineCount)) * (PI / 180f)
            val startDist = min(width, height) * 0.4f
            val endDist = max(width, height) * 0.8f
            val sx = center.x + cos(angle).toFloat() * startDist
            val sy = center.y + sin(angle).toFloat() * startDist
            val ex = center.x + cos(angle).toFloat() * endDist
            val ey = center.y + sin(angle).toFloat() * endDist
            drawLine(
                color = Color.White.copy(alpha = 0.18f),
                start = Offset(sx, sy),
                end = Offset(ex, ey),
                strokeWidth = 2f
            )
        }
    }

    if (artStyle == ArtStyle.CYBERPUNK) {
        // Neon grid lines at the base
        val gridY = height * 0.82f
        for (gx in 0..10) {
            val startX = (width / 10f) * gx
            drawLine(
                color = Color(0xFF00E5FF).copy(alpha = 0.3f),
                start = Offset(startX, gridY),
                end = Offset(width * 0.5f + (startX - width * 0.5f) * 2.5f, height),
                strokeWidth = 1.5f
            )
        }
    }
}

private fun DrawScope.drawVignette(width: Float, height: Float) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color(0x99000000)),
            center = Offset(width * 0.5f, height * 0.5f),
            radius = max(width, height) * 0.7f
        ),
        size = Size(width, height)
    )
}
