package com.acv.chat.domain

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import arrow.core.raise.Raise
import com.acv.chat.util.TempFileProvider
import com.acv.chat.util.from
import com.acv.chat.util.rememberDeferrable
import java.io.File

interface DocumentService {

  context(Raise<DomainError>)
  suspend fun pickDocument(): Media.Pdf
}

class DocumentServiceMock : DocumentService {

  context(Raise<DomainError>)
  override suspend fun pickDocument(): Media.Pdf {
    return Media.Pdf(File(""))
  }
}

@Composable
fun rememberDocumentLauncher(fileProvider: TempFileProvider): DocumentService {
  val context = LocalContext.current
  val result = rememberDeferrable<Uri?>()
  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument(),
    onResult = { uri -> result.complete(uri) }
  )

  return remember {
    object : DocumentService {

      context(Raise<DomainError>)
      override suspend fun pickDocument(): Media.Pdf {
        galleryLauncher.launch(arrayOf("application/pdf"))

        val uri = result.await() ?: raise(DomainError.UnknownDomainError("Error picking document"))

        return fileProvider.getDocument().from(context, uri)
      }
    }
  }
}

sealed interface Media {
  val file: File

  data class Pdf(override val file: File) : Media
  data class Image(override val file: File) : Media
}