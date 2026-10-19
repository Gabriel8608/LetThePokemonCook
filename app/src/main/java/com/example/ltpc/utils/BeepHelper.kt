package com.example.ltpc.utils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sin

class BeepHelper {

    suspend fun playBeepSequence() = withContext(Dispatchers.Default) {
        val sampleRate = 44100
        val freq = 2000.0 // 2000 Hz
        val beepDurationMs = 500
        val silenceDurationMs = 500

        val beepSamples = (sampleRate * beepDurationMs) / 1000
        val silenceSamples = (sampleRate * silenceDurationMs) / 1000

        val totalBeeps = 3
        val totalBufferSamples = (beepSamples + silenceSamples) * totalBeeps
        val buffer = ShortArray(totalBufferSamples)

        var sampleIndex = 0
        for (b in 0 until totalBeeps) {
            // Generate 500ms sound wave
            for (i in 0 until beepSamples) {
                val angle = 2.0 * Math.PI * i / (sampleRate / freq)
                buffer[sampleIndex++] = (sin(angle) * Short.MAX_VALUE * 0.5).toInt().toShort()
            }
            // Generate 500ms silence
            for (i in 0 until silenceSamples) {
                buffer[sampleIndex++] = 0
            }
        }

        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(maxOf(minBufferSize, buffer.size * 2))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
    }
}
