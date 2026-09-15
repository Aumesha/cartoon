package com.example.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun CharacterStudioTab(
    project: AutoDraftProject,
    onUpdateProject: (AutoDraftProject) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCharacterId by remember {
        mutableStateOf(project.characters.firstOrNull()?.id ?: "")
    }

    val characters = project.characters
    val selectedCharacter = characters.find { it.id == selectedCharacterId } ?: characters.firstOrNull()
    var showCreateDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Studio Intro Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Consistent Character Studio",
                                color = Color(0xFFF0F6FC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Preserve facial identity, costume colors & accessories seamlessly across all storyboard scenes.",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Button(
                        onClick = { showCreateDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Character", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        // Horizontal Character Deck Selector
        item {
            Text(
                text = "PROJECT CHARACTER DECK",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(characters) { ch ->
                    val isSelected = ch.id == selectedCharacter?.id
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF1E293B) else Color(0xFF161B22),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFF10B981) else Color(0xFF30363D)
                        ),
                        modifier = Modifier
                            .width(140.dp)
                            .clickable { selectedCharacterId = ch.id }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(ch.accentColorHex))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = ch.name,
                                    color = if (isSelected) Color(0xFF10B981) else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = ch.role,
                                color = Color(0xFF8B949E),
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Selected Character Sheet & Multi-Pose Consistency Preview
        if (selectedCharacter != null) {
            item {
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
                            Column {
                                Text(
                                    text = selectedCharacter.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = selectedCharacter.role,
                                    color = Color(0xFF00E5FF),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF21262D))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = selectedCharacter.artStyle.label,
                                    color = Color(0xFFFFB703),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Turnaround consistency views: Close-up vs Action vs Wide
                        Text(
                            text = "CHARACTER CONSISTENCY MODEL SHEET",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // View 1: Close-up Face
                            val closeUpPanel = StoryboardPanel(
                                id = "preview-1",
                                panelIndex = 1,
                                title = "Face Portrait",
                                visualPrompt = "Close-up portrait of ${selectedCharacter.name}",
                                cameraAngle = CameraAngle.CLOSE_UP,
                                artStyle = selectedCharacter.artStyle,
                                mood = StoryMood.EPIC,
                                characterId = selectedCharacter.id
                            )

                            // View 2: Heroic Stance
                            val heroPanel = StoryboardPanel(
                                id = "preview-2",
                                panelIndex = 2,
                                title = "Full Stance",
                                visualPrompt = "Full heroic stature of ${selectedCharacter.name}",
                                cameraAngle = CameraAngle.LOW_ANGLE,
                                artStyle = selectedCharacter.artStyle,
                                mood = StoryMood.ACTION,
                                characterId = selectedCharacter.id
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                AutoDraftPanelVisual(
                                    panel = closeUpPanel,
                                    character = selectedCharacter,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    showDialogueBubble = false
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Close-Up Face",
                                    color = Color(0xFFE6EDF3),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                AutoDraftPanelVisual(
                                    panel = heroPanel,
                                    character = selectedCharacter,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    showDialogueBubble = false
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Full Hero Stature",
                                    color = Color(0xFFE6EDF3),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Character DNA Attributes Table
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0D1117))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "CONSISTENT DESIGN ATTRIBUTES",
                                color = Color(0xFF10B981),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Row {
                                Text("Hair & Head: ", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text(selectedCharacter.hairDescription, color = Color.White, fontSize = 11.sp)
                            }
                            Row {
                                Text("Costume: ", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text(selectedCharacter.outfitDescription, color = Color.White, fontSize = 11.sp)
                            }
                            Row {
                                Text("Distinctive Relic: ", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text(selectedCharacter.distinctiveFeature, color = Color(0xFF00E5FF), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Modal Dialog to Create a New Character
    if (showCreateDialog) {
        var charName by remember { mutableStateOf("") }
        var charRole by remember { mutableStateOf("") }
        var charHair by remember { mutableStateOf("Jet black wavy adventurer hair") }
        var charOutfit by remember { mutableStateOf("Traditional explorer vest with gold clasps") }
        var charFeature by remember { mutableStateOf("Mystic blue luminous talisman") }
        var charArtStyle by remember { mutableStateOf(ArtStyle.ANIME_2D) }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            containerColor = Color(0xFF161B22),
            title = {
                Text("Create New Consistent Character", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = charName,
                        onValueChange = { charName = it },
                        label = { Text("Character Name (e.g. Karan, Maya)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFF30363D)
                        )
                    )

                    OutlinedTextField(
                        value = charRole,
                        onValueChange = { charRole = it },
                        label = { Text("Archetype / Role (e.g. Explorer, Alchemist)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFF30363D)
                        )
                    )

                    OutlinedTextField(
                        value = charHair,
                        onValueChange = { charHair = it },
                        label = { Text("Hair Style & Color") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFF30363D)
                        )
                    )

                    OutlinedTextField(
                        value = charOutfit,
                        onValueChange = { charOutfit = it },
                        label = { Text("Costume / Attire Details") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFF30363D)
                        )
                    )

                    OutlinedTextField(
                        value = charFeature,
                        onValueChange = { charFeature = it },
                        label = { Text("Distinctive Amulet / Relic / Weapon") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFF30363D)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (charName.isNotBlank()) {
                            val newChar = CharacterProfile(
                                id = UUID.randomUUID().toString(),
                                name = charName,
                                role = if (charRole.isNotBlank()) charRole else "Protagonist",
                                hairDescription = charHair,
                                outfitDescription = charOutfit,
                                distinctiveFeature = charFeature,
                                artStyle = charArtStyle,
                                accentColorHex = listOf(0xFF00E5FF, 0xFFFFB703, 0xFFE63946, 0xFF10B981, 0xFF8338EC).random()
                            )
                            onUpdateProject(project.copy(characters = project.characters + newChar))
                            selectedCharacterId = newChar.id
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    enabled = charName.isNotBlank()
                ) {
                    Text("Save Character", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = Color(0xFF8B949E))
                }
            }
        )
    }
}
