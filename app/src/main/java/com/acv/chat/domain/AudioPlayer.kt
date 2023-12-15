package com.acv.chat.domain

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import arrow.core.raise.Raise
import com.acv.chat.arrow.error.catch
import java.io.File

interface AudioPlayer {
  context(Raise<DomainError>)
  suspend fun Audio.play()

  context(Raise<DomainError>)
  suspend fun stopAudio()
}

class AudioPlayerMock : AudioPlayer {

  context(Raise<DomainError>)
  override suspend fun Audio.play(): Unit = Unit

  context(Raise<DomainError>)
  override suspend fun stopAudio() = Unit
}

class AndroidAudioPlayer(
  private val context: Context
) : AudioPlayer {

  private var mediaPlayer: MediaPlayer? = null

  context(Raise<DomainError>)
  override suspend fun Audio.play(): Unit = catch(onError = DomainError::UnknownDomainError) {
    mediaPlayer = MediaPlayer.create(context, Uri.fromFile(File(path))) ?: throw Exception("Error creating MediaPlayer")
    mediaPlayer?.setOnCompletionListener { }
    mediaPlayer?.start()
  }

  context(Raise<DomainError>)
  override suspend fun stopAudio(): Unit = catch(onError = DomainError::UnknownDomainError) {
    mediaPlayer?.stop()
    mediaPlayer?.release()
    mediaPlayer = null
  }
}