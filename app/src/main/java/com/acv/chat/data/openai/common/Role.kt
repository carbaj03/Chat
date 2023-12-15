package com.acv.chat.data.openai.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RoleSerializer::class)
sealed class Role(val role: String) {
  data object System : Role("system")
  data object User : Role("user")
  data object Assistant : Role("assistant")
  data object Function : Role("function")
  data object Tool : Role("tool")
}

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