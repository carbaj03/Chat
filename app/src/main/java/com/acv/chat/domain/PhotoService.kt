package com.acv.chat.domain

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import arrow.core.raise.Raise
import com.acv.chat.util.TempFileProvider
import com.acv.chat.util.rememberDeferrable
import com.acv.chat.util.toUri
import java.io.File

@Composable
fun rememberPhotoLauncher(fileProvider: TempFileProvider): PhotoService {
  val result = rememberDeferrable<Unit?>()
  val context = LocalContext.current

  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture(),
    onResult = { result.complete(if (it) Unit else null) }
  )

  return remember {
    object : PhotoService {

      context(Raise<DomainError>)
      override suspend fun takePhoto(): Media.Image {
        val image = fileProvider.getImage()
        val imageUri = image.toUri(context)
        cameraLauncher.launch(imageUri)

        return result.await()
          ?.let { image }
          ?: raise(DomainError.UnknownDomainError("Photo not taken"))
      }
    }
  }
}

interface PhotoService {
  context(Raise<DomainError>)
  suspend fun takePhoto(): Media.Image
}


class PhotoServiceMock(private val file: File? = File("")) : PhotoService {

  context(Raise<DomainError>)
  override suspend fun takePhoto(): Media.Image =
    file?.let { Media.Image(it) }
      ?: raise(DomainError.UnknownDomainError("Photo not taken"))
}