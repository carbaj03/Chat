package com.acv.chat.domain

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import arrow.core.raise.Raise
import com.acv.chat.util.ComposeFileProvider
import com.acv.chat.util.toUri
import kotlinx.coroutines.CompletableDeferred
import java.io.File

interface PhotoService {

  context(Raise<DomainError>)
  suspend fun takePhoto(): File
}

@Composable
fun rememberPhotoLauncher(): PhotoService {
  var def: CompletableDeferred<Unit?> by remember { mutableStateOf(CompletableDeferred()) }
  val context = LocalContext.current

  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture(),
    onResult = {
      if (it) def.complete(Unit) else def.complete(null)
    }
  )

  return remember {
    object : PhotoService {

      context(Raise<DomainError>)
      override suspend fun takePhoto(): File {
        val file = ComposeFileProvider.getImageFile(context)
        val imageUri = file.toUri(context)
        cameraLauncher.launch(imageUri)
        return def.await()?.let { file }.also { def = CompletableDeferred() } ?: raise(DomainError.UnknownDomainError("Photo not taken"))
      }
    }
  }
}
