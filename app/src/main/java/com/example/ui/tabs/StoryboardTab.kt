package com.example.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.AutoDraftPanelVisual
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryboardTab(
    project: AutoDraftProject,
    onUpdateProject: (AutoDraftProject) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPanelIndex by remember { mutableStateOf(0) }
    val panels = project.panels
    val selectedPanel = panels.getOrNull(selectedPanelIndex)
    val characters = project.characters

    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Studio Status Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF30363D), RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.title,
                            color = Color(0xFFF0F6FC),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${panels.size} Storyboard Panels • ${project.genre} • AutoDraft Canvas Engine",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp
                        )
                    }

                    // Add New Panel Button
                    Button(
                        onClick = {
                            val newPanel = StoryboardPanel(
                                id = UUID.randomUUID().toString(),
                                panelIndex = panels.size + 1,
                                title = "Scene ${panels.size + 1}: The Next Revelation",
                                visualPrompt = "Hero stepping forward into golden dawn sunlight, dramatic cinematic composition",
                                narration = "The path ahead was now clearer than ever.",
                                dialogue = "ಮುಂದಿನ ಹೆಜ್ಜೆ ನಮ್ಮ ಭವಿಷ್ಯವನ್ನು ನಿರ್ಧರಿಸುತ್ತದೆ. (The next step decides our destiny.)",
                                speakerName = characters.firstOrNull()?.name ?: "Narrator",
                                cameraAngle = CameraAngle.WIDE_SHOT,
                                artStyle = selectedPanel?.artStyle ?: ArtStyle.ANIME_2D,
                                mood = StoryMood.EPIC,
                                durationSeconds = 4,
                                characterId = characters.firstOrNull()?.id,
                                seed = (1000..99999).random().toLong()
                            )
                            onUpdateProject(project.copy(panels = panels + newPanel))
                            selectedPanelIndex = panels.size
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Panel", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Horizontal Storyboard Panels Reel (Mini Timeline)
        item {
            Text(
                text = "STORYBOARD TIMELINE",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(panels) { index, p ->
                    val isSelected = index == selectedPanelIndex
                    val char = characters.find { it.id == p.characterId }

                    Column(
                        modifier = Modifier
                            .width(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF1E293B) else Color(0xFF161B22))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF30363D),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedPanelIndex = index }
                            .padding(6.dp)
                    ) {
                        AutoDraftPanelVisual(
                            panel = p,
                            character = char,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp),
                            showDialogueBubble = false
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "P${index + 1}: ${p.title}",
                            color = if (isSelected) Color(0xFF00E5FF) else Color(0xFFE6EDF3),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Text(
                            text = "${p.durationSeconds}s • ${p.cameraAngle.label.take(8)}",
                            color = Color(0xFF8B949E),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        // Selected Panel Inspector / Canvas Preview
        if (selectedPanel != null) {
            item {
                val assignedChar = characters.find { it.id == selectedPanel.characterId }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Panel ${selectedPanelIndex + 1}: ${selectedPanel.title}",
                                color = Color(0xFFF0F6FC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Row {
                                // Re-roll / Regenerate Visual Seed
                                IconButton(
                                    onClick = {
                                        val updatedPanels = panels.toMutableList()
                                        updatedPanels[selectedPanelIndex] = selectedPanel.copy(
                                            seed = (1000..99999).random().toLong()
                                        )
                                        onUpdateProject(project.copy(panels = updatedPanels))
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Regenerate Visual",
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Edit Panel Details
                                IconButton(
                                    onClick = { showEditDialog = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Details",
                                        tint = Color(0xFF6366F1),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Delete Panel (if more than 1)
                                if (panels.size > 1) {
                                    IconButton(
                                        onClick = {
                                            val updatedPanels = panels.toMutableList()
                                            updatedPanels.removeAt(selectedPanelIndex)
                                            onUpdateProject(project.copy(panels = updatedPanels))
                                            selectedPanelIndex = selectedPanelIndex.coerceAtMost(updatedPanels.size - 1)
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete Panel",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Large Canvas Visual
                        AutoDraftPanelVisual(
                            panel = selectedPanel,
                            character = assignedChar,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            showDialogueBubble = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Prompt & Scene Metadata
                        Text(
                            text = "AI Visual Prompt:",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = selectedPanel.visualPrompt,
                            color = Color(0xFFE6EDF3),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (selectedPanel.narration.isNotBlank()) {
                            Text(
                                text = "Voiceover Narration:",
                                color = Color(0xFF8B949E),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = selectedPanel.narration,
                                color = Color(0xFFF0F6FC),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Quick Selectors for Camera & Style
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Camera Angle Tag
                            AssistChip(
                                onClick = { showEditDialog = true },
                                label = { Text(selectedPanel.cameraAngle.label, fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Videocam,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color(0xFF00E5FF)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF21262D),
                                    labelColor = Color(0xFFE6EDF3)
                                )
                            )

                            // Art Style Tag
                            AssistChip(
                                onClick = { showEditDialog = true },
                                label = { Text(selectedPanel.artStyle.label, fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color(0xFFFFB703)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF21262D),
                                    labelColor = Color(0xFFE6EDF3)
                                )
                            )

                            // Assigned Character Tag
                            AssistChip(
                                onClick = { showEditDialog = true },
                                label = { Text(assignedChar?.name ?: "No Character", fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color(0xFF10B981)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF21262D),
                                    labelColor = Color(0xFFE6EDF3)
                                )
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Panel Editor Modal Dialog
    if (showEditDialog && selectedPanel != null) {
        var editTitle by remember { mutableStateOf(selectedPanel.title) }
        var editPrompt by remember { mutableStateOf(selectedPanel.visualPrompt) }
        var editDialogue by remember { mutableStateOf(selectedPanel.dialogue) }
        var editSpeaker by remember { mutableStateOf(selectedPanel.speakerName) }
        var editNarration by remember { mutableStateOf(selectedPanel.narration) }
        var editSfx by remember { mutableStateOf(selectedPanel.soundEffect) }
        var editAngle by remember { mutableStateOf(selectedPanel.cameraAngle) }
        var editStyle by remember { mutableStateOf(selectedPanel.artStyle) }
        var editMood by remember { mutableStateOf(selectedPanel.mood) }
        var editCharId by remember { mutableStateOf(selectedPanel.characterId) }
        var editDuration by remember { mutableStateOf(selectedPanel.durationSeconds.toString()) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = Color(0xFF161B22),
            title = {
                Text(
                    text = "Edit Storyboard Panel ${selectedPanelIndex + 1}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Panel Title") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF00E5FF),
                                unfocusedBorderColor = Color(0xFF30363D)
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = editPrompt,
                            onValueChange = { editPrompt = it },
                            label = { Text("Visual Prompt (Scene Description)") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF00E5FF),
                                unfocusedBorderColor = Color(0xFF30363D)
                            )
                        )
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = editSpeaker,
                                onValueChange = { editSpeaker = it },
                                label = { Text("Speaker") },
                                modifier = Modifier.weight(0.4f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF00E5FF),
                                    unfocusedBorderColor = Color(0xFF30363D)
                                )
                            )
                            OutlinedTextField(
                                value = editDialogue,
                                onValueChange = { editDialogue = it },
                                label = { Text("Dialogue (Kannada/Eng)") },
                                modifier = Modifier.weight(0.6f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF00E5FF),
                                    unfocusedBorderColor = Color(0xFF30363D)
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = editNarration,
                            onValueChange = { editNarration = it },
                            label = { Text("Voiceover Narration") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF00E5FF),
                                unfocusedBorderColor = Color(0xFF30363D)
                            )
                        )
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = editSfx,
                                onValueChange = { editSfx = it },
                                label = { Text("Comic SFX") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF00E5FF),
                                    unfocusedBorderColor = Color(0xFF30363D)
                                )
                            )
                            OutlinedTextField(
                                value = editDuration,
                                onValueChange = { editDuration = it },
                                label = { Text("Duration (sec)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF00E5FF),
                                    unfocusedBorderColor = Color(0xFF30363D)
                                )
                            )
                        }
                    }

                    // Camera Angle Selector
                    item {
                        Text("Camera Angle:", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(CameraAngle.values().size) { idx ->
                                val angle = CameraAngle.values()[idx]
                                FilterChip(
                                    selected = editAngle == angle,
                                    onClick = { editAngle = angle },
                                    label = { Text(angle.label, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF6366F1),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF21262D),
                                        labelColor = Color(0xFFE6EDF3)
                                    )
                                )
                            }
                        }
                    }

                    // Art Style Selector
                    item {
                        Text("Art Style:", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(ArtStyle.values().size) { idx ->
                                val style = ArtStyle.values()[idx]
                                FilterChip(
                                    selected = editStyle == style,
                                    onClick = { editStyle = style },
                                    label = { Text(style.label, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF00E5FF),
                                        selectedLabelColor = Color.Black,
                                        containerColor = Color(0xFF21262D),
                                        labelColor = Color(0xFFE6EDF3)
                                    )
                                )
                            }
                        }
                    }

                    // Assigned Character
                    item {
                        Text("Attach Character:", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Option: None
                            item {
                                FilterChip(
                                    selected = editCharId == null,
                                    onClick = { editCharId = null },
                                    label = { Text("None (Scene Only)", fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF10B981),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF21262D),
                                        labelColor = Color(0xFFE6EDF3)
                                    )
                                )
                            }
                            items(characters.size) { idx ->
                                val ch = characters[idx]
                                FilterChip(
                                    selected = editCharId == ch.id,
                                    onClick = { editCharId = ch.id },
                                    label = { Text(ch.name, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF10B981),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF21262D),
                                        labelColor = Color(0xFFE6EDF3)
                                    )
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val duration = editDuration.toIntOrNull()?.coerceAtLeast(1) ?: 4
                        val updatedPanels = panels.toMutableList()
                        updatedPanels[selectedPanelIndex] = selectedPanel.copy(
                            title = editTitle,
                            visualPrompt = editPrompt,
                            dialogue = editDialogue,
                            speakerName = editSpeaker,
                            narration = editNarration,
                            soundEffect = editSfx,
                            cameraAngle = editAngle,
                            artStyle = editStyle,
                            mood = editMood,
                            characterId = editCharId,
                            durationSeconds = duration
                        )
                        onUpdateProject(project.copy(panels = updatedPanels))
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                ) {
                    Text("Save Changes", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = Color(0xFF8B949E))
                }
            }
        )
    }
}
