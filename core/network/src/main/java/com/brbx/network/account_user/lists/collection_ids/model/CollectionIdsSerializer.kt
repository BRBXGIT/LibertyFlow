package com.brbx.network.account_user.lists.collection_ids.model

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

internal object CollectionIdsSerializer : KSerializer<CollectionIds> {
    
    override val descriptor: SerialDescriptor = 
        buildClassSerialDescriptor("CollectionIdsSerializer")

    override fun deserialize(decoder: Decoder): CollectionIds {
        val jsonDecoder = decoder as? JsonDecoder 
            ?: error("This deserializer only supports JSON")
            
        val array = jsonDecoder.decodeJsonElement() as JsonArray
        if (array.isEmpty()) {
            error("Received an empty array, cannot parse TitleItemDto")
        }
        
        val status = array.last().jsonPrimitive.content
        val ids = array.dropLast(n = 1).map { it.jsonPrimitive.int }
        
        return CollectionIds(
            ids = ids,
            status = status,
        )
    }

    override fun serialize(encoder: Encoder, value: CollectionIds) {
        val jsonEncoder = encoder as? JsonEncoder 
            ?: error("This serializer only supports JSON")
            
        val array = buildJsonArray {
            value.ids.forEach { id ->
                add(JsonPrimitive(id)) 
            }
            add(JsonPrimitive(value.status))
        }
        jsonEncoder.encodeJsonElement(array)
    }
}