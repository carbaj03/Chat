package com.acv.chat.data.openai.assistant.runs

import com.acv.chat.data.openai.assistant.runs.Run.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Status::class)
object StatusSerializer : KSerializer<Status> {
    override fun serialize(encoder: Encoder, value: Status) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): Status {
        val value = decoder.decodeString()
        return when (value) {
            "queued" -> Status.Queued
            "in_progress" -> Status.InProgress
            "requires_action" -> Status.RequiresAction
            "cancelled" -> Status.Cancelled
            "cancelling" -> Status.Cancelling
            "failed" -> Status.Failed
            "completed" -> Status.Completed
            "expired" -> Status.Expired
            else -> throw IllegalArgumentException("Unknown Status value: $value")
        }
    }
}