package com.acv.chat.domain

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import arrow.core.raise.Raise
import com.acv.chat.util.ComposeFileProvider
import kotlinx.coroutines.CompletableDeferred
import java.io.File

interface MediaService {

  context(Raise<DomainError>)
  suspend fun pickFromGallery(): File
}

@Composable
fun rememberGalleryLauncher(): MediaService {
  var def: CompletableDeferred<Uri?> by remember { mutableStateOf(CompletableDeferred()) }
  val context = LocalContext.current

  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = {
      it?.let { def.complete(it) } ?: def.complete(null)
    }
  )

  return remember {
    object : MediaService {

      context(Raise<DomainError>)
      override suspend fun pickFromGallery(): File {
        galleryLauncher.launch(PickVisualMediaRequest(ImageOnly))
        return def.await()?.let {
          context.contentResolver.openInputStream(it)?.use { input ->
            ComposeFileProvider.getImageFile(context).apply {
              outputStream().use { output ->
                input.copyTo(output)
              }
            }
          }
        }.also {
          def = CompletableDeferred()
        } ?: raise(DomainError.UnknownDomainError("Photo not taken"))
      }
    }
  }
}