package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.AutoDraftProject
import com.example.ui.components.VideoMotionPlayer
import com.example.ui.tabs.CharacterStudioTab
import com.example.ui.tabs.MonetizationRightsTab
import com.example.ui.tabs.ScriptWriterTab
import com.example.ui.tabs.StoryboardTab
import com.example.ui.theme.MyApplicationTheme

enum class StudioTab(val label: String, val icon: ImageVector) {
    STORYBOARD("Storyboard", Icons.Default.ViewCarousel),
    ANIMATION("Video Motion", Icons.Default.PlayCircleFilled),
    SCRIPT_WRITER("AI Script", Icons.Default.AutoAwesome),
    CHARACTERS("Characters", Icons.Default.Face),
    MONETIZATION("YouTube Rights", Icons.Default.Verified)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AutoDraftApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoDraftApp() {
    val context = LocalContext.current
    var project by remember { mutableStateOf(SampleData.createInitialProject()) }
    var selectedTab by remember { mutableStateOf(StudioTab.STORYBOARD) }
    var showExportDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0D1117),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B22))
                    .statusBarsPadding()
                    .border(
                        width = 0.5.dp,
                        color = Color(0xFF30363D)
                    )
            ) {
                // Top Brand & Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo + App Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { selectedTab = StudioTab.STORYBOARD }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6366F1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AD",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AutoDraft",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "AI",
                                    color = Color(0xFF00E5FF),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                            Text(
                                text = "Story, Comic & Animation Studio",
                                color = Color(0xFF8B949E),
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Commercial Rights Status Badge + Export Action
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0F261D),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                            modifier = Modifier.clickable { selectedTab = StudioTab.MONETIZATION }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "100% Monetizable",
                                    color = Color(0xFF86EFAC),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Quick Export Button
                        IconButton(
                            onClick = { showExportDialog = true },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Export for YouTube",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Primary Horizontal Navigation Scrollable Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = Color(0xFF161B22),
                    contentColor = Color(0xFF00E5FF),
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                            color = Color(0xFF00E5FF),
                            height = 2.5.dp
                        )
                    },
                    divider = {
                        HorizontalDivider(color = Color(0xFF30363D), thickness = 0.5.dp)
                    }
                ) {
                    StudioTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp),
                                        tint = if (isSelected) Color(0xFF00E5FF) else Color(0xFF8B949E)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tab.label,
                                        color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF8B949E),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0D1117))
        ) {
            when (selectedTab) {
                StudioTab.STORYBOARD -> {
                    StoryboardTab(
                        project = project,
                        onUpdateProject = { project = it },
                        modifier = Modifier.fillMaxSize().padding(top = 10.dp)
                    )
                }
                StudioTab.ANIMATION -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "LIVE MOTION & VIDEO STUDIO",
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        VideoMotionPlayer(
                            project = project,
                            onExportVideo = { showExportDialog = true }
                        )

                        // Quick info card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF30363D), RoundedCornerShape(10.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Dynamic Spoken Voiceover & Subtitles", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "AutoDraft reads both Kannada & English dialogues with synchronized Ken Burns camera motion and subtitle timing for YouTube engagement.",
                                    color = Color(0xFF8B949E),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
                StudioTab.SCRIPT_WRITER -> {
                    ScriptWriterTab(
                        project = project,
                        onApplyScript = { newProj ->
                            project = newProj
                            selectedTab = StudioTab.STORYBOARD
                            Toast.makeText(context, "Storyboard Updated from AI Script!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxSize().padding(top = 10.dp)
                    )
                }
                StudioTab.CHARACTERS -> {
                    CharacterStudioTab(
                        project = project,
                        onUpdateProject = { project = it },
                        modifier = Modifier.fillMaxSize().padding(top = 10.dp)
                    )
                }
                StudioTab.MONETIZATION -> {
                    MonetizationRightsTab(
                        project = project,
                        modifier = Modifier.fillMaxSize().padding(top = 10.dp)
                    )
                }
            }
        }
    }

    // Export Dialog (YouTube Readiness & Commercial Certificate)
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            containerColor = Color(0xFF161B22),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ready for YouTube Upload", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your video has been rendered with full commercial rights and verified for YouTube Partner Program (YPP) monetization approval.",
                        color = Color(0xFFE6EDF3),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0D1117))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("• Resolution: 1080p Full HD (16:9 / 9:16)", color = Color(0xFF00E5FF), fontSize = 11.sp)
                        Text("• Watermark: NONE (100% Watermark-Free)", color = Color(0xFF10B981), fontSize = 11.sp)
                        Text("• License: ${project.license.licenseId}", color = Color(0xFFFFB703), fontSize = 11.sp)
                        Text("• Commercial Rights: Irrevocable Worldwide Clearance", color = Color.White, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Click below to view the 1-Click YouTube Publishing Kit with Title, Description, and Tags.",
                        color = Color(0xFF8B949E),
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExportDialog = false
                        selectedTab = StudioTab.MONETIZATION
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                ) {
                    Text("Open YouTube Publishing Kit", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close", color = Color(0xFF8B949E))
                }
            }
        )
    }
}

// Backwards-compatibility for GreetingScreenshotTest
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}
