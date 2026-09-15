package com.example.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.SampleData
import com.example.data.StoryTemplate
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ScriptWriterTab(
    project: AutoDraftProject,
    onApplyScript: (AutoDraftProject) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var storyPrompt by remember {
        mutableStateOf("ಕರ್ನಾಟಕದ ಕಾಡಿನಲ್ಲಿ ಅಡಗಿರುವ ಪ್ರಾಚೀನ ಮಾಂತ್ರಿಕ ದಿಕ್ಸೂಚಿಯನ್ನು ಹುಡುಕುವ ಸಾಹಸಿ ಕರಣ್ ಮತ್ತು ವನ ರಕ್ಷಕಿ ಮಾಯಾ ಅವರ ಕಥೆ.")
    }
    var selectedGenre by remember { mutableStateOf("Mythological Fantasy") }
    var selectedArtStyle by remember { mutableStateOf(ArtStyle.ANIME_2D) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedPreviewProject by remember { mutableStateOf<AutoDraftProject?>(null) }
    var generationSuccessMessage by remember { mutableStateOf<String?>(null) }

    val genres = listOf(
        "Mythological Fantasy",
        "Cyberpunk Sci-Fi",
        "Ancient Mystery",
        "Action Adventure",
        "Supernatural Horror",
        "Comedy & Folklore"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // AI Script Generator Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AutoDraft AI Script & Storyboard Engine",
                            color = Color(0xFFF0F6FC),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Turn your concept into production-ready YouTube animation scenes with character consistency, Kannada & English dialogue, and commercial license.",
                        color = Color(0xFF8B949E),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Quick Preset Templates
        item {
            Text(
                text = "POPULAR STORY TEMPLATES",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SampleData.storyTemplates) { t: StoryTemplate ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF21262D),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D)),
                        modifier = Modifier.clickable {
                            storyPrompt = t.prompt
                            selectedGenre = t.genre
                        }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Text(
                                text = t.title,
                                color = Color(0xFF00E5FF),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = t.genre,
                                color = Color(0xFF8B949E),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Prompt Input Field
        item {
            OutlinedTextField(
                value = storyPrompt,
                onValueChange = { storyPrompt = it },
                label = { Text("Story Idea or Script Concept (Kannada / English)") },
                placeholder = { Text("Enter your idea here...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00E5FF),
                    unfocusedBorderColor = Color(0xFF30363D),
                    focusedContainerColor = Color(0xFF0D1117),
                    unfocusedContainerColor = Color(0xFF0D1117)
                )
            )
        }

        // Genre Selector
        item {
            Text(
                text = "SELECT GENRE",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(genres) { g ->
                    FilterChip(
                        selected = selectedGenre == g,
                        onClick = { selectedGenre = g },
                        label = { Text(g, fontSize = 11.sp) },
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
            Text(
                text = "DEFAULT ART STYLE",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(ArtStyle.values().size) { idx ->
                    val s = ArtStyle.values()[idx]
                    FilterChip(
                        selected = selectedArtStyle == s,
                        onClick = { selectedArtStyle = s },
                        label = { Text(s.label, fontSize = 11.sp) },
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

        // Generate Button
        item {
            Button(
                onClick = {
                    coroutineScope.launch {
                        isGenerating = true
                        generationSuccessMessage = null
                        delay(1200) // Simulating AI processing & scene assembly

                        // Build structured story project from prompt
                        val isSciFi = selectedGenre.contains("Sci-Fi") || storyPrompt.contains("Bengaluru")
                        val isHorror = selectedGenre.contains("Horror") || storyPrompt.contains("Mystery")

                        val charHero = CharacterProfile(
                            id = "ai-char-hero",
                            name = if (isSciFi) "Dev (Zero-One)" else "Karan",
                            role = if (isSciFi) "Cyber Hacker & Courier" else "Explorer of the Sahyadris",
                            hairDescription = if (isSciFi) "Neon blue shaved fade" else "Windswept dark adventurer hair",
                            outfitDescription = if (isSciFi) "Graphene tech hoodie with fiberoptics" else "Traveler leather coat with copper compass clip",
                            distinctiveFeature = if (isSciFi) "Cybernetic ocular implant" else "Vedic sun medallion",
                            defaultPose = CharacterPose.HEROIC_STAND,
                            artStyle = selectedArtStyle,
                            accentColorHex = if (isSciFi) 0xFF00E5FF else 0xFFFFB703
                        )

                        val charCompanion = CharacterProfile(
                            id = "ai-char-companion",
                            name = if (isSciFi) "Nisha" else "Maya",
                            role = if (isSciFi) "Autonomous AI Pilot" else "Guardian of Ancient Springs",
                            hairDescription = "Long braided dark hair with jade ribbons",
                            outfitDescription = if (isSciFi) "Sleek magnetic exoskeleton" else "Silk woodland vestments",
                            distinctiveFeature = "Glowing resonance rod",
                            defaultPose = CharacterPose.SPEAKING,
                            artStyle = selectedArtStyle,
                            accentColorHex = 0xFF10B981
                        )

                        val newPanels = listOf(
                            StoryboardPanel(
                                id = UUID.randomUUID().toString(),
                                panelIndex = 1,
                                title = "Act I: The Discovery",
                                visualPrompt = "Wide establishing shot of ${if (isSciFi) "neon illuminated skyways of Bengaluru 2099" else "the mystical rainforest waterfalls in Western Ghats"}, atmospheric dusk lighting",
                                narration = "Every great saga begins with a spark the world was never ready for.",
                                dialogue = "",
                                speakerName = "",
                                soundEffect = if (isSciFi) "SYNTH HUM" else "NATURE WIND",
                                cameraAngle = CameraAngle.WIDE_SHOT,
                                artStyle = selectedArtStyle,
                                mood = StoryMood.MYSTERIOUS,
                                durationSeconds = 4,
                                characterId = null
                            ),
                            StoryboardPanel(
                                id = UUID.randomUUID().toString(),
                                panelIndex = 2,
                                title = "Act II: Encounter with Destiny",
                                visualPrompt = "${charHero.name} holding ancient relic, divine light shining up into eyes, expressive dramatic face",
                                narration = "The artifact responded only to pure intent.",
                                dialogue = "ಇದು ಕೇವಲ ಕಥೆಯಲ್ಲ, ಇದು ನಮ್ಮ ಭವಿಷ್ಯದ ದಾರಿ! (This is no mere folklore, this is our path!)",
                                speakerName = charHero.name,
                                soundEffect = "CHIME-PULSE",
                                cameraAngle = CameraAngle.CLOSE_UP,
                                artStyle = selectedArtStyle,
                                mood = StoryMood.EPIC,
                                durationSeconds = 5,
                                characterId = charHero.id
                            ),
                            StoryboardPanel(
                                id = UUID.randomUUID().toString(),
                                panelIndex = 3,
                                title = "Act III: Guardian's Challenge",
                                visualPrompt = "${charCompanion.name} standing majestically before ancient stone gate, elemental aura swirling",
                                narration = "To cross the threshold, the guardian's riddle had to be solved.",
                                dialogue = "ಧೈರ್ಯ ಮಾತ್ರ ಸಾಲದು, ಜ್ಞಾನವೂ ಬೇಕು. ನೀನು ಸಿದ್ಧನಿದ್ದೀಯಾ? (Courage alone is not enough. Are you truly prepared?)",
                                speakerName = charCompanion.name,
                                soundEffect = "ELEMENTAL ROAR",
                                cameraAngle = CameraAngle.LOW_ANGLE,
                                artStyle = selectedArtStyle,
                                mood = StoryMood.DRAMATIC,
                                durationSeconds = 5,
                                characterId = charCompanion.id
                            ),
                            StoryboardPanel(
                                id = UUID.randomUUID().toString(),
                                panelIndex = 4,
                                title = "Act IV: The Portal Awakes",
                                visualPrompt = "Enormous beam of cosmic light erupting from gateway into starry night sky, dramatic cinematic climax",
                                narration = "And in that moment, the eternal realm opened its doors.",
                                dialogue = "ಬಾ, ನಾವು ಒಟ್ಟಾಗಿ ನವ ಯುಗವನ್ನು ಸೃಷ್ಟಿಸೋಣ! (Come, together we shall build a new age!)",
                                speakerName = "${charHero.name} & ${charCompanion.name}",
                                soundEffect = "COSMIC BURST",
                                cameraAngle = CameraAngle.DUTCH_ANGLE,
                                artStyle = selectedArtStyle,
                                mood = StoryMood.ACTION,
                                durationSeconds = 5,
                                characterId = charHero.id
                            )
                        )

                        val generatedProj = project.copy(
                            title = if (storyPrompt.length > 40) storyPrompt.take(40) + "..." else storyPrompt,
                            genre = selectedGenre,
                            logline = "An original visual narrative created with AutoDraft AI, fully cleared for commercial YouTube monetization.",
                            characters = listOf(charHero, charCompanion),
                            panels = newPanels
                        )

                        generatedPreviewProject = generatedProj
                        isGenerating = false
                        generationSuccessMessage = "Generated 4-panel cinematic script with character continuity!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                enabled = !isGenerating && storyPrompt.isNotBlank()
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AutoDraft AI Generating Script...", color = Color.White)
                } else {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color(0xFFFFB703),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Generate Storyboard Script (ಕಥೆ ರಚಿಸಿ)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Generated Screenplay Preview Box
        if (generatedPreviewProject != null) {
            val gen = generatedPreviewProject!!
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFF10B981), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SCREENPLAY DRAFT GENERATED",
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 0.8.sp
                            )

                            // Apply to Studio Button
                            Button(
                                onClick = {
                                    onApplyScript(gen)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Apply to Storyboard Studio",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = gen.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Characters Created: ${gen.characters.joinToString { it.name }}",
                            color = Color(0xFF00E5FF),
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        gen.panels.forEachIndexed { i, p ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "P${i + 1}",
                                    color = Color(0xFFFFB703),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.width(28.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = p.title,
                                        color = Color(0xFFE6EDF3),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                    if (p.dialogue.isNotBlank()) {
                                        Text(
                                            text = "\"${p.dialogue}\"",
                                            color = Color(0xFF8B949E),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
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
}
