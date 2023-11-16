package com.acv.chat.data.openai.tokenizer

import android.content.Context
import com.acv.chat.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okio.Buffer
import okio.ByteString
import okio.FileSystem
import okio.Path.Companion.toPath

class LocalPbeLoader(
  private val fileSystem: FileSystem,
  private val directory: String? = null
) : BpeLoader {

  override suspend fun loadEncoding(encoding: Encoding): Map<ByteString, Int> {
    val encodingPath = when (encoding) {
      CL100KBase -> "cl100k_base.tiktoken"
    }
    val data = readFile(encodingPath, fileSystem)
    return loadTiktokenBpe(data)
  }

  private fun readFile(file: String, fileSystem: FileSystem): ByteArray {
    val buffer = Buffer()
    val filePath = file.toPath()
    val path = directory?.toPath()?.let { it / filePath } ?: filePath
    fileSystem.read(path) { readAll(buffer) }
    return buffer.readByteArray()
  }
}

class AndroidPbeLoader(
  private val context: Context,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BpeLoader {

  override suspend fun loadEncoding(encoding: Encoding): Map<ByteString, Int> = with(dispatcher) {
    val inputStream = when (encoding) {
      CL100KBase -> context.resources.openRawResource(R.raw.cl100k_base)
    }
    val data: ByteArray = inputStream.readBytes()
    return loadTiktokenBpe(data)
  }
}
