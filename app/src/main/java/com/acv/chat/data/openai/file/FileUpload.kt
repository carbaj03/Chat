package com.acv.chat.data.openai.file

import com.acv.chat.data.openai.assistant.file.Purpose
import com.acv.chat.data.openai.FileSource

class FileUpload(
  val file: FileSource,
  val purpose: Purpose,
)