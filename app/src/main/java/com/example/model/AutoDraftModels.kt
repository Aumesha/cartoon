package com.example.model

import java.util.UUID

enum class CameraAngle(val label: String, val description: String) {
    WIDE_SHOT("Wide Cinematic", "Full scene landscape view"),
    CLOSE_UP("Dramatic Close-up", "Focus on character emotion and face"),
    LOW_ANGLE("Hero Low Angle", "Looking up from below, powerful stature"),
    HIGH_ANGLE("Bird's Eye / High", "Overhead perspective showing depth"),
    OVER_SHOULDER("Over The Shoulder", "Conversational depth perspective"),
    DUTCH_ANGLE("Tilted Action Dutch", "Dynamic tension and motion")
}

enum class ArtStyle(val label: String, val visualPromptModifier: String) {
    ANIME_2D("2D Anime Studio", "Makoto Shinkai aesthetic, vibrant lighting, anime cel shading, high detail"),
    PIXAR_3D("3D Pixar Animation", "Pixar Disney 3D render style, subsurface scattering, expressive 3D character, soft lighting"),
    CINEMATIC_REALISTIC("Cinematic Film", "35mm anamorphic film photography, cinematic color grading, moody atmosphere"),
    COMIC_BOOK("Graphic Comic / Webtoon", "Bold ink line art, vibrant comic book screentone, dynamic action shadows"),
    INDIAN_MYTHOLOGY("Epic Mythological Art", "Indian epic miniature painting fusion, golden divine lighting, regal ornaments"),
    CYBERPUNK("Cyberpunk Neon", "Glowing neon signs, dark wet rainy streets, synthwave aesthetics, futuristic tech")
}

enum class StoryMood(val label: String, val hexColor: Long) {
    EPIC("Epic & Grand", 0xFFFFB703),
    MYSTERIOUS("Mysterious", 0xFF8338EC),
    ACTION("High Action", 0xFFE63946),
    PEACEFUL("Serene & Calm", 0xFF2A9D8F),
    DRAMATIC("Dramatic Tension", 0xFF3A86FF)
}

enum class AspectRatioOption(val label: String, val ratioText: String, val widthRatio: Float, val heightRatio: Float) {
    YOUTUBE_16_9("YouTube Standard", "16:9 Widescreen", 16f, 9f),
    SHORTS_9_16("YouTube Shorts / Reels", "9:16 Vertical", 9f, 16f)
}

enum class CharacterPose(val label: String) {
    HEROIC_STAND("Heroic Standing"),
    ACTION_BATTLE("Dynamic Action / Battle"),
    SPEAKING("Conversational Speaking"),
    SHOCKED_EXPRESSIVE("Shocked / Expressive"),
    MEDITATING("Thoughtful / Meditating")
}

data class CharacterProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val role: String,
    val hairDescription: String = "Spiky dark hair",
    val outfitDescription: String = "Crimson traveler jacket with gold trims",
    val distinctiveFeature: String = "Glowing talisman around neck",
    val defaultPose: CharacterPose = CharacterPose.HEROIC_STAND,
    val artStyle: ArtStyle = ArtStyle.ANIME_2D,
    val accentColorHex: Long = 0xFF6366F1
)

data class StoryboardPanel(
    val id: String = UUID.randomUUID().toString(),
    val panelIndex: Int,
    val title: String,
    val visualPrompt: String,
    val dialogue: String = "",
    val speakerName: String = "",
    val narration: String = "",
    val soundEffect: String = "",
    val cameraAngle: CameraAngle = CameraAngle.WIDE_SHOT,
    val artStyle: ArtStyle = ArtStyle.ANIME_2D,
    val mood: StoryMood = StoryMood.EPIC,
    val durationSeconds: Int = 4,
    val characterId: String? = null,
    val seed: Long = (1000..99999).random().toLong(),
    val imageUrl: String? = null
)

data class CommercialLicense(
    val licenseId: String,
    val projectName: String,
    val licenseeName: String,
    val issueDate: String,
    val rightsScope: String = "Full Worldwide Commercial Distribution & YouTube Monetization",
    val hashSignature: String,
    val isMonetizationReady: Boolean = true
)

data class AutoDraftProject(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val genre: String,
    val logline: String,
    val creatorName: String = "YouTube Creator",
    val targetPlatform: AspectRatioOption = AspectRatioOption.YOUTUBE_16_9,
    val characters: List<CharacterProfile> = emptyList(),
    val panels: List<StoryboardPanel> = emptyList(),
    val license: CommercialLicense
)
