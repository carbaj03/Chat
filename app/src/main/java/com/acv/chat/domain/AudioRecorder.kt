package com.acv.chat.domain

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import arrow.core.raise.Raise
import com.acv.chat.arrow.error.onError
import java.io.File

interface AudioRecorder {
  val isRecording: Boolean

  context(Raise<DomainError>)
  suspend fun startRecording()

  context(Raise<DomainError>)
  suspend fun stopRecording(): Audio
}

class AudioRecorderMock : AudioRecorder {
  private var _isRecording = false
  override val isRecording: Boolean get() = _isRecording

  context(Raise<DomainError>)
  override suspend fun startRecording() {
    _isRecording = true
  }

  context(Raise<DomainError>)
  override suspend fun stopRecording(): Audio =
    Audio("mock").also { _isRecording = false }
}

class AndroidAudioRecorder(
  private val context: Context
) : AudioRecorder {

  private var recorder: MediaRecorder? = null
  private var file: File? = null

  private fun createFile(): File =
    File.createTempFile(System.currentTimeMillis().toString(), ".mp4", context.cacheDir)

  private fun mediaRecorder(file: File): MediaRecorder =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      MediaRecorder(context)
    } else {
      MediaRecorder()
    }.apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
      setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
      setOutputFile(file)
    }

  override val isRecording: Boolean get() = file != null

  context(Raise<DomainError>)
  override suspend fun startRecording(): Unit = onError(onError = DomainError::UnknownDomainError) {
    val file = createFile()
    recorder = mediaRecorder(file).apply {
      prepare()
      start()
    }
    this.file = file

  }

  context(Raise<DomainError>)
  override suspend fun stopRecording(): Audio = onError(onError = DomainError::UnknownDomainError) {
    file ?: throw Exception("There is not recording to stop")
    recorder!!.stop()
    recorder!!.reset()
    recorder!!.release()
    recorder = null
    if (file!!.length().also { Log.e("Size", it.toString()) } >= 26214400) throw Exception("File is too big")
    Audio(file!!.absolutePath).also { file = null }
  }
}
