package com.acv.chat.util

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import arrow.core.raise.Raise
import com.acv.chat.R
import com.acv.chat.arrow.error.catch
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.Media
import com.acv.chat.util.ComposeFileProvider.Companion.fileProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

interface TempFileProvider {

//  context(Raise<DomainError>)
//  suspend fun getImageUri(): Uri

  context(Raise<DomainError>)
  suspend fun getImage(): Media.Image

  context(Raise<DomainError>)
  suspend fun getDocument(): Media.Pdf
}

class ComposeFileProvider : FileProvider(R.xml.filepaths) {
  companion object {

    val Context.fileProvider: String
      get() = "${packageName}.fileprovider"
  }
}

context(Raise<DomainError>)
suspend fun Media.Image.toUri(context: Context): Uri = suspendCancellableCoroutine { cc ->
  catch(DomainError::UnknownDomainError) {
    cc.resume(FileProvider.getUriForFile(context, context.fileProvider, file))
  }
}

context(Raise<DomainError>)
suspend fun Media.Image.from(context: Context, uri: Uri): Media.Image =
  Media.Image(file.from(context, uri))

context(Raise<DomainError>)
suspend fun Media.Pdf.from(context: Context, uri: Uri): Media.Pdf =
  Media.Pdf(file.from(context, uri))

context(Raise<DomainError>)
suspend fun File.from(context: Context, uri: Uri): File = suspendCancellableCoroutine {
  context.contentResolver.openInputStream(uri)?.use { input ->
    outputStream().use { output ->
      input.copyTo(output)
    }
    it.resume(this)
  } ?: raise(DomainError.UnknownDomainError("Error copying document"))
}

@Composable
fun rememberFileProvider(): TempFileProvider {
  val context = LocalContext.current

  return object : TempFileProvider {

//    context(Raise<DomainError>)
//    override suspend fun getImageUri(): Uri =
//      getImageFile().toUri(context)

    context(Raise<DomainError>)
    override suspend fun getImage(): Media.Image =
      Media.Image(TempFile("images", ".jpg"))

    context(Raise<DomainError>)
    override suspend fun getDocument(): Media.Pdf =
      Media.Pdf(TempFile("docs", ".pdf"))

    context(Raise<DomainError>)
    private suspend fun TempFile(folder: String, suffix: String): File = suspendCancellableCoroutine {
      try {
        val directory = File(context.cacheDir, folder)
        directory.mkdirs()
        val file = File.createTempFile(System.currentTimeMillis().toString(), suffix, directory)
        it.resume(file)
      } catch (e: Exception) {
        raise(DomainError.UnknownDomainError("Error creating file cache"))
      }
    }
  }
}
