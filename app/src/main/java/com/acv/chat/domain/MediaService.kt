package com.acv.chat.domain

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import arrow.core.raise.Raise
import com.acv.chat.util.TempFileProvider
import com.acv.chat.util.from
import com.acv.chat.util.rememberDeferrable
import java.io.File

interface MediaService {

  context(Raise<DomainError>)
  suspend fun pickFromGallery(): Media.Image
}

class MediaServiceMock : MediaService {

  override suspend fun pickFromGallery(): Media.Image =
    Media.Image(File(""))
}

@Composable
fun rememberGalleryLauncher(fileProvider: TempFileProvider): MediaService {
  val result = rememberDeferrable<Uri?>()
  val context = LocalContext.current

  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = { result.complete(it) }
  )

  return remember {
    object : MediaService {

      context(Raise<DomainError>)
      override suspend fun pickFromGallery(): Media.Image {
        galleryLauncher.launch(PickVisualMediaRequest(ImageOnly))

        val uri = result.await() ?: raise(DomainError.UnknownDomainError("Error picking document"))

        return fileProvider.getImage().from(context, uri)
      }
    }
  }
}