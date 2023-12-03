package com.acv.chat.data.openai.assistant

import com.acv.chat.data.openai.assistant.thread.Role
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Role::class)
object RoleSerializer : KSerializer<Role> {
  override fun serialize(encoder: Encoder, value: Role) {
    encoder.encodeString(value.role)
  }

  override fun deserialize(decoder: Decoder): Role {
    val role = decoder.decodeString()
    return when (role) {
      "system" -> Role.System
      "user" -> Role.User
      "assistant" -> Role.Assistant
      "function" -> Role.Function
      "tool" -> Role.Tool
      else -> throw IllegalArgumentException("Unknown Role value: $role")
    }
  }
}