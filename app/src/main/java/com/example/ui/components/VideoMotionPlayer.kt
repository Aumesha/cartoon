package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioOption
import com.example.model.AutoDraftProject
import com.example.util.SpeechHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun VideoMotionPlayer(
    project: AutoDraftProject,
    modifier: Modifier = Modifier,
    onExportVideo: () -> Unit = {}
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechHelper(context) }
    DisposableEffect(Unit) {
        onDispose {
            speechHelper.shutdown()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }
    var currentPanelIndex by remember { mutableStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var aspectRatio by remember { mutableStateOf(project.targetPlatform) }
    var panelProgress by remember { mutableStateOf(0f) }

    val panels = project.panels
    val currentPanel = panels.getOrNull(currentPanelIndex)
    val currentCharacter = project.characters.find { it.id == currentPanel?.characterId }

    val totalDurationSeconds = remember(panels) {
        panels.sumOf { it.durationSeconds }
    }

    // Dynamic Ken Burns Pan & Zoom parameters for the active panel
    val zoomAnim = remember(currentPanelIndex) { Animatable(1.0f) }
    val panXAnim = remember(currentPanelIndex) { Animatable(0f) }
    val panYAnim = remember(currentPanelIndex) { Animatable(0f) }

    // Trigger speech on panel change if playing and not muted
    LaunchedEffect(currentPanelIndex, isPlaying, isMuted) {
        if (isPlaying && !isMuted && currentPanel != null) {
            val speechText = when {
                currentPanel.narration.isNotBlank() && currentPanel.dialogue.isNotBlank() ->
                    "${currentPanel.narration}. ${currentPanel.speakerName}: ${currentPanel.dialogue}"
                currentPanel.dialogue.isNotBlank() -> currentPanel.dialogue
                else -> currentPanel.narration
            }
            if (speechText.isNotBlank()) {
                speechHelper.speak(speechText)
            }
        }
    }

    // Playback loop
    LaunchedEffect(isPlaying, currentPanelIndex, panels.size) {
        if (isPlaying && currentPanel != null) {
            val durationMs = (currentPanel.durationSeconds * 1000L).coerceAtLeast(2000L)
            val startTime = System.currentTimeMillis()

            // Animate Ken Burns camera move
            val targetZoom = if (currentPanelIndex % 2 == 0) 1.18f else 1.05f
            val targetPanX = if (currentPanelIndex % 2 == 0) 0.8f else -0.8f
            val targetPanY = if (currentPanelIndex % 2 == 0) -0.5f else 0.5f

            zoomAnim.snapTo(1.0f)
            panXAnim.snapTo(0f)
            panYAnim.snapTo(0f)

            // Interpolate progress
            while (isActive && isPlaying) {
                val elapsed = System.currentTimeMillis() - startTime
                val fraction = (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)
                panelProgress = fraction
                zoomAnim.snapTo(1.0f + (targetZoom - 1.0f) * fraction)
                panXAnim.snapTo(targetPanX * fraction)
                panYAnim.snapTo(targetPanY * fraction)

                if (elapsed >= durationMs) {
                    if (currentPanelIndex < panels.size - 1) {
                        currentPanelIndex++
                    } else {
                        isPlaying = false
                        panelProgress = 1f
                    }
                    break
                }
                delay(30)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D1117))
            .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        // Top Player Bar: Aspect Ratio & Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying) Color(0xFF10B981) else Color(0xFFEF4444))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isPlaying) "PLAYING ANIMATION" else "PAUSED",
                    color = Color(0xFF8B949E),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ratio Toggle (16:9 vs 9:16 Shorts)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF21262D),
                    modifier = Modifier.clickable {
                        aspectRatio = if (aspectRatio == AspectRatioOption.YOUTUBE_16_9)
                            AspectRatioOption.SHORTS_9_16 else AspectRatioOption.YOUTUBE_16_9
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (aspectRatio == AspectRatioOption.YOUTUBE_16_9) Icons.Default.Tv else Icons.Default.Smartphone,
                            contentDescription = "Aspect Ratio",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (aspectRatio == AspectRatioOption.YOUTUBE_16_9) "16:9 YouTube" else "9:16 Shorts",
                            color = Color(0xFFE6EDF3),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Mute / Unmute Button
                IconButton(
                    onClick = {
                        isMuted = !isMuted
                        if (isMuted) speechHelper.stop()
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Voiceover",
                        tint = if (isMuted) Color(0xFF8B949E) else Color(0xFF10B981),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Canvas Viewport with aspect ratio constraint
        val containerModifier = if (aspectRatio == AspectRatioOption.YOUTUBE_16_9) {
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        } else {
            Modifier
                .fillMaxWidth()
                .height(340.dp)
                .aspectRatio(9f / 16f, matchHeightConstraintsFirst = true)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (currentPanel != null) {
                AutoDraftPanelVisual(
                    panel = currentPanel,
                    character = currentCharacter,
                    modifier = containerModifier,
                    isAnimated = isPlaying,
                    zoomScale = zoomAnim.value,
                    panOffset = Offset(panXAnim.value, panYAnim.value),
                    showDialogueBubble = false
                )
            } else {
                Box(
                    modifier = containerModifier
                        .background(Color(0xFF161B22)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Storyboard Panels Found", color = Color(0xFF8B949E))
                }
            }

            // Commercial Badge Watermark Safe Indicator
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xD90D1117))
                    .border(0.5.dp, Color(0xFF10B981), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "100% Monetizable • No Watermark",
                        color = Color(0xFFE6EDF3),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Live Karaoke / Subtitle Overlay
            if (currentPanel != null && (currentPanel.dialogue.isNotBlank() || currentPanel.narration.isNotBlank())) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xE605070A))
                        .border(1.dp, Color(0xFF30363D), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (currentPanel.dialogue.isNotBlank()) {
                            Text(
                                text = "${if (currentPanel.speakerName.isNotBlank()) "${currentPanel.speakerName}: " else ""}\"${currentPanel.dialogue}\"",
                                color = Color(0xFF00E5FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        } else {
                            Text(
                                text = currentPanel.narration,
                                color = Color(0xFFF0F6FC),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = {
                val base = currentPanelIndex.toFloat() / panels.size.coerceAtLeast(1)
                val segment = (1f / panels.size.coerceAtLeast(1)) * panelProgress
                (base + segment).coerceIn(0f, 1f)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = Color(0xFF00E5FF),
            trackColor = Color(0xFF21262D)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Control Buttons: Previous, Play/Pause, Next, Scrubber
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Panel ${currentPanelIndex + 1} of ${panels.size} (${currentPanel?.durationSeconds ?: 0}s)",
                color = Color(0xFF8B949E),
                fontSize = 11.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Previous
                IconButton(
                    onClick = {
                        if (currentPanelIndex > 0) {
                            currentPanelIndex--
                            panelProgress = 0f
                        }
                    },
                    enabled = currentPanelIndex > 0,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = if (currentPanelIndex > 0) Color.White else Color(0xFF484F58)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Play / Pause Circle Button
                FilledIconButton(
                    onClick = {
                        if (!isPlaying && currentPanelIndex >= panels.size - 1 && panelProgress >= 1f) {
                            currentPanelIndex = 0
                            panelProgress = 0f
                        }
                        isPlaying = !isPlaying
                        if (!isPlaying) speechHelper.stop()
                    },
                    modifier = Modifier.size(42.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color(0xFF6366F1)
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Next
                IconButton(
                    onClick = {
                        if (currentPanelIndex < panels.size - 1) {
                            currentPanelIndex++
                            panelProgress = 0f
                        }
                    },
                    enabled = currentPanelIndex < panels.size - 1,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = if (currentPanelIndex < panels.size - 1) Color.White else Color(0xFF484F58)
                    )
                }
            }

            // Export Video Button
            Button(
                onClick = onExportVideo,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Export MP4",
                    color = Color(0xFF00E5FF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
