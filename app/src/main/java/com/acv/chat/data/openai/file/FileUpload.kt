package com.acv.chat.data.openai.file

import com.acv.chat.data.FileSource
import com.acv.chat.data.openai.assistant.file.Purpose

//512 MB or 2 million tokens
class FileUpload(
  val file: FileSource,
  val purpose: Purpose,
)