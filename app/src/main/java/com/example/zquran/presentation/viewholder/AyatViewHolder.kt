package com.example.zquran.presentation.viewholder

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.Ayat
import com.example.zquran.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.io.IOException

class AyatViewHolder(
    private val ayatItemView: View
): RecyclerView.ViewHolder(ayatItemView) {
    private var arabText: MaterialTextView? = null
    private var translationText: MaterialTextView? = null
    private var playButton: MaterialButton? = null
    private var stopButton: MaterialButton? = null
    private var mediaPlayer: MediaPlayer? = null

    fun bindData(data: Ayat) {
        arabText = ayatItemView.findViewById(R.id.item_ayat_arab)
        translationText = ayatItemView.findViewById(R.id.item_ayat_text)
        playButton = ayatItemView.findViewById(R.id.item_ayat_button)
        stopButton = ayatItemView.findViewById(R.id.item_ayat_button_stop)

        arabText?.text = data.text
        translationText?.text = "${data.numberInSurah}. ${data.translationText}"

        playButton?.setOnClickListener {
            playAudio(data.audio!!.toUri())
            playButton?.visibility = View.GONE
            stopButton?.visibility = View.VISIBLE
            Toast.makeText(ayatItemView.context, "Audio started playing..", Toast.LENGTH_SHORT).show()
        }

        stopButton?.setOnClickListener {
            releaseMediaPlayerResources()
            playButton?.visibility = View.VISIBLE
            stopButton?.visibility = View.GONE
        }
    }

    private fun playAudio(uri: Uri) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(ayatItemView.context, uri)
                setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                )
                prepare()
                start()
            }
        } catch (exception: IOException) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun releaseMediaPlayerResources() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
    }
}