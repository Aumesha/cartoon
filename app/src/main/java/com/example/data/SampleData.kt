package com.example.data

import com.example.model.*
import java.text.SimpleDateFormat
import java.util.*

object SampleData {

    private val currentDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val defaultCharacterKaran = CharacterProfile(
        id = "char-karan",
        name = "Karan",
        role = "Young Explorer & Inventor",
        hairDescription = "Midnight black wind-swept hair",
        outfitDescription = "Aero-leather scout jacket with copper buckles",
        distinctiveFeature = "Luminescent sapphire chronometer amulet",
        defaultPose = CharacterPose.HEROIC_STAND,
        artStyle = ArtStyle.ANIME_2D,
        accentColorHex = 0xFF00E5FF
    )

    val defaultCharacterMaya = CharacterProfile(
        id = "char-maya",
        name = "Maya",
        role = "Forest Guardian & Alchemist",
        hairDescription = "Long braided dark hair with emerald ribbon",
        outfitDescription = "Silken woodland robe with silver leaf embroidery",
        distinctiveFeature = "Carved jade staff that pulses with green energy",
        defaultPose = CharacterPose.SPEAKING,
        artStyle = ArtStyle.ANIME_2D,
        accentColorHex = 0xFF10B981
    )

    val defaultCharacterGuru = CharacterProfile(
        id = "char-guru",
        name = "Acharya Veda",
        role = "Ancient Mystic & Astronomer",
        hairDescription = "Silver grey hair tied in traditional ascetic topknot",
        outfitDescription = "Deep saffron and gold bordered drape",
        distinctiveFeature = "Celestial brass astrolabe ring",
        defaultPose = CharacterPose.MEDITATING,
        artStyle = ArtStyle.INDIAN_MYTHOLOGY,
        accentColorHex = 0xFFFFB703
    )

    fun createInitialProject(): AutoDraftProject {
        val license = CommercialLicense(
            licenseId = "AD-COMMERCIAL-YT-${UUID.randomUUID().toString().take(8).uppercase()}",
            projectName = "The Legend of the Western Ghats: Secret of the Golden Lotus",
            licenseeName = "Creator Studio Pro",
            issueDate = currentDate,
            rightsScope = "Irrevocable Commercial Clearance • 100% Monetizable on YouTube, Shorts, TikTok & Web",
            hashSignature = "SHA256: 4f8b9e1a2c3d0f7b6e5d8a9c1b2e3f4a5c6e7d8b9a0c1d2e3f4a5b6c7d8e9f0a",
            isMonetizationReady = true
        )

        val panels = listOf(
            StoryboardPanel(
                id = "p-1",
                panelIndex = 1,
                title = "Dawn Over the Misty Canopy",
                visualPrompt = "Wide scenic vista of ancient misty rainforest mountains in Western Ghats, golden sunrise breaking through morning fog, distant temple silhouette",
                narration = "Deep within the Sahyadri mountains, an ancient secret lay untouched for three millennia...",
                dialogue = "",
                speakerName = "",
                soundEffect = "WIND WHISPER",
                cameraAngle = CameraAngle.WIDE_SHOT,
                artStyle = ArtStyle.ANIME_2D,
                mood = StoryMood.MYSTERIOUS,
                durationSeconds = 4,
                characterId = null,
                seed = 10421
            ),
            StoryboardPanel(
                id = "p-2",
                panelIndex = 2,
                title = "Karan Awakens the Chronometer",
                visualPrompt = "Young explorer Karan holding an ancient glowing brass amulet in his palm, sapphire glow illuminating his determined expression, dense bamboo background",
                narration = "The compass had remained dormant—until today.",
                dialogue = "ಇದು... ಇದು ಸಾಧ್ಯವೇ? ದಿಕ್ಸೂಚಿ ಜೀವಂತವಾಗಿದೆ! (Is this... really possible? The compass has awakened!)",
                speakerName = "Karan",
                soundEffect = "HUMM-CHIME",
                cameraAngle = CameraAngle.CLOSE_UP,
                artStyle = ArtStyle.ANIME_2D,
                mood = StoryMood.EPIC,
                durationSeconds = 5,
                characterId = "char-karan",
                seed = 20844
            ),
            StoryboardPanel(
                id = "p-3",
                panelIndex = 3,
                title = "Guardian of the Sacred Springs",
                visualPrompt = "Forest guardian Maya stepping gracefully through waterfalls, jade staff glowing radiant emerald green, butterflies fluttering around glowing orchids",
                narration = "The guardian of the spring was already waiting for him.",
                dialogue = "ನಿನ್ನ ಕೈಯಲ್ಲಿರುವ ಬೆಳಕು ಸಾಮಾನ್ಯವಲ್ಲ, ಕರಣ್. ನಿನ್ನ ಪಯಣ ಈಗ ಶುರುವಾಗಿದೆ. (The light in your hands is no ordinary spark, Karan. Your true journey begins now.)",
                speakerName = "Maya",
                soundEffect = "SPLASH & GLOW",
                cameraAngle = CameraAngle.LOW_ANGLE,
                artStyle = ArtStyle.ANIME_2D,
                mood = StoryMood.PEACEFUL,
                durationSeconds = 4,
                characterId = "char-maya",
                seed = 30129
            ),
            StoryboardPanel(
                id = "p-4",
                panelIndex = 4,
                title = "The Celestial Gateway Unfolds",
                visualPrompt = "Epic stone carved ancient temple gateway opening in the cliffside, beam of golden celestial starlight shooting up into the night sky, dramatic wind",
                narration = "As the two relics touched, the forgotten portal of the sages roared open.",
                dialogue = "ನೋಡು ಮಾಯಾ! ನಮ್ಮ ಇತಿಹಾಸದ ಸತ್ಯ ನಮ್ಮ ಮುಂದಿದೆ! (Look Maya! The truth of our ancestors stands before us!)",
                speakerName = "Karan & Maya",
                soundEffect = "THUNDER ROAR",
                cameraAngle = CameraAngle.DUTCH_ANGLE,
                artStyle = ArtStyle.ANIME_2D,
                mood = StoryMood.ACTION,
                durationSeconds = 5,
                characterId = "char-karan",
                seed = 40591
            )
        )

        return AutoDraftProject(
            id = "proj-default",
            title = "The Legend of the Western Ghats: Secret of the Golden Lotus",
            genre = "Mythological Fantasy & Adventure",
            logline = "A daring explorer and a forest guardian unlock an ancient celestial portal using a forgotten Vedic chronometer.",
            creatorName = "AutoDraft Creator",
            targetPlatform = AspectRatioOption.YOUTUBE_16_9,
            characters = listOf(defaultCharacterKaran, defaultCharacterMaya, defaultCharacterGuru),
            panels = panels,
            license = license
        )
    }

    val storyTemplates = listOf(
        StoryTemplate(
            title = "The Chrono Compass (ಕಾಲ ದಿಕ್ಸೂಚಿ)",
            genre = "Mythological Adventure",
            prompt = "A young traveler in South India discovers a Vedic brass clock that bends time, guarded by a river nymph."
        ),
        StoryTemplate(
            title = "Cyber Bengaluru 2099",
            genre = "Sci-Fi Cyberpunk",
            prompt = "A drone courier and an AI hacker uncover a corporate conspiracy hiding beneath the neon skyways of modern India."
        ),
        StoryTemplate(
            title = "The Whispering Haveli",
            genre = "Supernatural Mystery",
            prompt = "A detective investigates an old palace where portraits change expressions whenever the clock strikes midnight."
        ),
        StoryTemplate(
            title = "The Cosmic Chariot",
            genre = "Epic Sci-Fi Mythology",
            prompt = "Deep space explorers discover ancient flying Vimana vessels orbiting an alien solar system."
        )
    )
}

data class StoryTemplate(
    val title: String,
    val genre: String,
    val prompt: String
)
