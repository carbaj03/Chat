package com.acv.chat.data;

import okio.FileSystem
import okio.Path
import okio.Source

data class FileSource(
  val name: String,
  val source: Source,
) {
  constructor(path: Path, fileSystem: FileSystem) : this(path.name, fileSystem.source(path))
}
