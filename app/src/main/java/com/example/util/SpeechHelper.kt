package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class SpeechHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.getDefault()
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(0.95f)
        } else {
            Log.e("SpeechHelper", "TTS Initialization failed")
        }
    }

    fun speak(text: String, onDone: (() -> Unit)? = null) {
        if (!isInitialized || text.isBlank()) {
            onDone?.invoke()
            return
        }

        // Clean out bracketed translations for smoother narration if desired
        val speakableText = text.replace(Regex("\\(.*?\\)"), "").trim()
        val textToUse = if (speakableText.isNotBlank()) speakableText else text

        tts?.speak(textToUse, TextToSpeech.QUEUE_FLUSH, null, "AutoDraftTTS_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
