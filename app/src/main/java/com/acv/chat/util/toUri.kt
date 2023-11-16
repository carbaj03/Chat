package com.acv.chat.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import arrow.core.raise.Raise
import com.acv.chat.arrow.error.catch
import com.acv.chat.domain.DomainError
import java.io.File

context(Raise<DomainError>)
suspend fun File.toUri(context: Context): Uri =
  catch(
    onError = { raise(DomainError.UnknownDomainError("File not found")) }
  ) {
    FileProvider.getUriForFile(context, context.fileProvider, this)
  }