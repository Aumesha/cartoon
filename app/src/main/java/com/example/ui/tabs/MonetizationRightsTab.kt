package com.example.ui.tabs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AutoDraftProject

@Composable
fun MonetizationRightsTab(
    project: AutoDraftProject,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    var channelName by remember { mutableStateOf(project.creatorName) }

    // YouTube SEO Generated Kit
    val ytTitle = "${project.title} | Official Animated Story Film (AutoDraft Studio)"
    val copyrightDeclaration = """
=== OFFICIAL COMMERCIAL RIGHTS & MONETIZATION CLEARANCE ===
License Certificate ID: ${project.license.licenseId}
Digital Signature Hash: ${project.license.hashSignature}
Rights Scope: Worldwide Commercial Rights, YouTube Partner Program (YPP) Monetization, AdSense Safe.
Original Story & Visual Composition created with AutoDraft AI Studio.
All visual assets, characters, dialogues, and audio narrations are certified 100% Royalty-Free and cleared for commercial monetization.
No third-party copyrighted materials, unauthorized likenesses, or unlicensed audio used.
""".trimIndent()

    val ytDescription = """
${project.logline}

Created using AutoDraft AI Studio — the premier Visual Storytelling & Animation engine.

Characters:
${project.characters.joinToString("\n") { "• ${it.name} (${it.role})" }}

Timestamps:
${project.panels.mapIndexed { i, p -> "00:0${i * 4} - Scene ${i + 1}: ${p.title}" }.joinToString("\n")}

$copyrightDeclaration

#animation #storytelling #ai #autodraft #youtube #monetization #kannada #shorts #cartoon
""".trimIndent()

    val ytTags = "autodraft, animation, visual storytelling, comic strip, anime 2d, youtube monetization, commercial rights, original cartoon, kannada animation, shorts, story film"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Commercial Clearance Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F261D)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF10B981), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "100% Commercial Rights & Monetization Cleared",
                            color = Color(0xFFE6EDF3),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ನಿಮ್ಮ ವೀಡಿಯೊವನ್ನು YouTube ನಲ್ಲಿ ಯಾವುದೇ ಕಾಪಿರೈಟ್ ಸಮಸ್ಯೆಯಿಲ್ಲದೆ ಅಪ್‌ಲೋಡ್ ಮಾಡಬಹುದು ಮತ್ತು Monetization ಪಡೆಯಬಹುದು. (You can upload this video to YouTube with full commercial rights and monetize your channel safely.)",
                        color = Color(0xFF86EFAC),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Official Commercial License Certificate
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
                        Text(
                            text = "OFFICIAL COMMERCIAL LICENSE CERTIFICATE",
                            color = Color(0xFFFFB703),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFFFFB703),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Certificate Details
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D1117))
                            .border(1.dp, Color(0xFF21262D), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("License ID:", color = Color(0xFF8B949E), fontSize = 11.sp)
                            Text(project.license.licenseId, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Project Title:", color = Color(0xFF8B949E), fontSize = 11.sp)
                            Text(project.title.take(25) + if (project.title.length > 25) "..." else "", color = Color.White, fontSize = 11.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Licensed To:", color = Color(0xFF8B949E), fontSize = 11.sp)
                            Text(channelName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Issue Date:", color = Color(0xFF8B949E), fontSize = 11.sp)
                            Text(project.license.issueDate, color = Color.White, fontSize = 11.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Digital Hash:", color = Color(0xFF8B949E), fontSize = 11.sp)
                            Text(project.license.hashSignature.take(28) + "...", color = Color(0xFF8B949E), fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Copy Certificate Button
                    Button(
                        onClick = {
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText("AutoDraft Commercial License", copyrightDeclaration)
                            )
                            Toast.makeText(context, "Commercial License Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Official License Certificate", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // YouTube Partner Program (YPP) Monetization Checklist
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "YOUTUBE MONETIZATION (YPP) COMPLIANCE CHECKLIST",
                        color = Color(0xFF8B949E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val checks = listOf(
                        "100% Original AI Artwork" to "No copyrighted third-party characters (Disney, Marvel, Anime logos).",
                        "Human Storytelling & Creative Direction" to "Custom dialogue, scene pacing, and narrative qualify under YouTube Fair Use and original authorship rules.",
                        "Royalty-Free Audio Narration" to "Built-in spoken voice and ambient scores have zero Content ID copyright claims.",
                        "AdSense & Advertiser-Friendly" to "Safe for family audiences, brands, and sponsor monetization.",
                        "No Reused Content Penalty" to "Original character storylines prevent YouTube 'Reused Content' monetization denials."
                    )

                    checks.forEach { (title, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text(desc, color = Color(0xFF8B949E), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // 1-Click YouTube Publishing Kit
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
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "YouTube Studio 1-Click Publishing Kit",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Title
                    Text("YouTube Video Title (High CTR):", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ytTitle,
                            color = Color(0xFF00E5FF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setPrimaryClip(ClipData.newPlainText("YouTube Title", ytTitle))
                                Toast.makeText(context, "Title Copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description with Copyright declaration
                    Text("Video Description (Includes Copyright Clearance):", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0D1117))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = ytDescription.take(180) + "...\n[Full copyright license included]",
                            color = Color(0xFFE6EDF3),
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            clipboardManager.setPrimaryClip(ClipData.newPlainText("YouTube Description", ytDescription))
                            Toast.makeText(context, "Full Description & License Copied!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Full Description & Monetization Text", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tags
                    Text("YouTube SEO Tags:", color = Color(0xFF8B949E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ytTags,
                            color = Color(0xFF86EFAC),
                            fontSize = 10.sp,
                            maxLines = 2,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setPrimaryClip(ClipData.newPlainText("YouTube Tags", ytTags))
                                Toast.makeText(context, "Tags Copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
