package com.acv.chat.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import arrow.core.raise.Raise
import com.acv.chat.R
import com.acv.chat.arrow.error.catch
import com.acv.chat.domain.DomainError
import java.io.File

class ComposeFileProvider : FileProvider(R.xml.filepaths) {
  companion object {

    context(Raise<DomainError>)
    fun getImageUri(context: Context): Uri =
      catch(onError = { raise(DomainError.UnknownDomainError("Photo not taken")) }) {
        val file = getImageFile(context)
        val authority = context.fileProvider
        return getUriForFile(context, authority, file)
      }

    context(Raise<DomainError>)
    fun getImageFile(context: Context): File =
      catch(
        onError = { raise(DomainError.UnknownDomainError("Photo not taken")) }
      ) {
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()
        return File.createTempFile(System.currentTimeMillis().toString(), ".jpg", directory)
      }
  }
}