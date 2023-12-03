package com.acv.chat.data.openai.file

import okio.Buffer
import okio.Source

internal fun String.asSource(): Source {
    val buffer = Buffer()
    buffer.writeUtf8(this)
    return buffer
}
