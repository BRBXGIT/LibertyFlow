package com.brbx.network.account_user.lists.collections.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

internal object CollectionsIdsSerializer : KSerializer<Map<String, List<Int>>> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("CollectionsMapSerializer")

    override fun deserialize(decoder: Decoder): Map<String, List<Int>> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("This deserializer only supports JSON")

        val rootArray = jsonDecoder.decodeJsonElement() as? JsonArray
            ?: error("Expected root element to be a JsonArray")

        val resultMap = mutableMapOf<String, List<Int>>()

        for (element in rootArray) {
            val innerArray = element as? JsonArray
                ?: error("Expected inner element to be a JsonArray")

            if (innerArray.isEmpty()) {
                error("Received an empty array, cannot parse collection")
            }

            val collectionName = innerArray.last().jsonPrimitive.content

            val ids = innerArray.dropLast(n = 1).map { it.jsonPrimitive.int }

            resultMap[collectionName] = ids
        }

        return resultMap
    }

    override fun serialize(encoder: Encoder, value: Map<String, List<Int>>) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: error("This serializer only supports JSON")

        val rootArray = buildJsonArray {
            value.forEach { (collection, ids) ->
                add(buildJsonArray {
                    // Сначала добавляем все ID
                    ids.forEach { id -> add(JsonPrimitive(id)) }
                    // В конец всегда добавляем название коллекции
                    add(JsonPrimitive(collection))
                })
            }
        }

        jsonEncoder.encodeJsonElement(rootArray)
    }
}